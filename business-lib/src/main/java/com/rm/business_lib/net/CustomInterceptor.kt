package com.rm.business_lib.net

import android.os.Build
import android.text.TextUtils
import com.google.gson.Gson
import com.rm.baselisten.BuildConfig
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.util.*
import com.rm.business_lib.ACCESS_TOKEN
import com.rm.business_lib.ACCESS_TOKEN_INVALID_TIMESTAMP
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.business_lib.bean.RefreshTokenBean
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.helpter.parseToken
import com.rm.business_lib.utils.DeviceUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * desc   : 业务网络拦截器
 * date   : 2020/09/01
 * version: 1.0
 */
class CustomInterceptor : Interceptor {

    private val TAG = "CustomInterceptor"

    // 服务器固定的 Android端app_key
    private val APP_KEY = "5d25eb5bf85ef867e78172d0d0df5ef0"

    // 需要刷新token服务器返回的code
    private val CODE_REFRESH_TOKEN = 1004

    // 被强制登出了(被挤下线了)
    private val CODE_LOGIN_OUT = 1204

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            RefreshApiService::class.java
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // 请求拦截处理
        val originalResponse = requestIntercept(chain)
        // 返回参数拦截处理
        responseIntercept(originalResponse, chain)
        return originalResponse[0]
    }

    /**
     * 拦截请求数据，判断访问token是否已过期
     * @return Boolean
     */
    private fun requestIntercept(chain: Interceptor.Chain): Array<Response> {
        if (TextUtils.isEmpty(ACCESS_TOKEN.getStringMMKV())
            || ACCESS_TOKEN_INVALID_TIMESTAMP.getLongMMKV(0) > System.currentTimeMillis()/1000
            || chain.request().url.toUri().path.contains("auth/refresh")
        ) {
            // 证明未登陆 或者 访问令牌token还未过期 或者 是刷新token的接口， 则不去判断token是否过期了,直接请求
            return buildRequestResponse(chain)
        }

        DLog.d(TAG, "请求时，验证token已过期，刷新token")
        // 到这里，说明刷新token已经过期了，则去刷新接口
        // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常
        val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
        return if (result?.code == 0) {
            // 刷新token成功，保存最新token
            updateLocalToken(result.data.access, result.data.refresh)
            // 再去构造真正用户需要请求的接口请求信息
            buildRequestResponse(chain)
        } else {
            // 刷新token失败，则还是让用户去请求，提示他token过期
            buildRequestResponse(chain)
        }
    }

    /**
     * 构建之前的请求
     * @param chain Chain
     * @return Array<Response>
     */
    private fun buildRequestResponse(chain: Interceptor.Chain): Array<Response> {
        val request: Request = getRequestHeaderBuilder(chain.request().newBuilder()).build()
        return arrayOf(chain.proceed(request))
    }

    /**
     * 拦截返回数据，判断是否需要刷新token等操作
     * @param originalResponse Response
     * @return Response
     */
    private fun responseIntercept(originalResponse: Array<Response>, chain: Interceptor.Chain) {
        //保存body的相关信息，用于后续构造新的Response
        val responseBody = originalResponse[0].body ?: return
        val contentType = responseBody.contentType()
        val responseStr = responseBody.string()
        responseBody.close()
        if (TextUtils.isEmpty(responseStr)) {
            // 没有返回信息，说明没有网络，未请求成功，则原封不动的返回数据
            originalResponse[0] = originalResponse[0].newBuilder().code(originalResponse[0].code)
                .body(responseStr.toResponseBody(contentType)).build()
            return
        }
        val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)
        if (baseResponse.code == CODE_REFRESH_TOKEN) {
            // 当前token过期，需要刷新token
            DLog.d(TAG, "响应时，token已过期，刷新token")
            // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常
            val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
            if (result?.code == 0) {
                // 刷新token成功，保存最新token
                updateLocalToken(result.data.access, result.data.refresh)
                // 将之前拦截的接口重新请求并下发
                val request: Request.Builder = getRequestHeaderBuilder(chain.request().newBuilder())
                try {
                    originalResponse[0] = chain.proceed(request.build())
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 再次请求之前拦截的接口失败，就下发之前请求到的数据到具体位置
                    originalResponse[0] = originalResponse[0].newBuilder().code(200)
                        .body(responseStr.toResponseBody(contentType)).build()
                }
            } else {
                // 刷新token失败，强制退出当前登陆
                loginOut()
                // TODO 如果刷新token失败，将之前请求的数据同样下发到具体位置，不过是否需要自己组一个账户已退出的消息数据进行下发？？？
                originalResponse[0] = originalResponse[0].newBuilder().code(200)
                    .body(responseStr.toResponseBody(contentType)).build()
            }
        } else if (baseResponse.code == CODE_LOGIN_OUT) {
            // 被挤下线，强制退出了
            loginOut()
            originalResponse[0] = originalResponse[0].newBuilder().code(200)
                .body(responseStr.toResponseBody(contentType)).build()
        } else {
            // 正常请求,下发数据到具体请求位置
            originalResponse[0] = originalResponse[0].newBuilder().code(200)
                .body(responseStr.toResponseBody(contentType)).build()
        }
    }


    /**
     * 更新本地token相关信息
     * @param access String
     * @param refresh String
     */
    private fun updateLocalToken(access: String, refresh: String) {
        ACCESS_TOKEN.putMMKV(access)
        REFRESH_TOKEN.putMMKV(refresh)
        // 更新访问token的过期时间
        ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(parseToken(access))
    }


    /**
     * 添加公共的 header
     * @param requestBuilder Builder
     * @return Request.Builder
     */
    private fun getRequestHeaderBuilder(requestBuilder: Request.Builder): Request.Builder {
        // 时间戳
        val timestamp = System.currentTimeMillis() / 1000
        // 随机生成的16位数字符
        val nonce = generateNonce()
        val signSource = StringBuilder().append(timestamp).append(nonce).append(APP_KEY).toString()
        requestBuilder.addHeader("APP-DEVICE", DeviceUtils.uniqueDeviceId)
        requestBuilder.addHeader("APP-DEVICE-MODEL", Build.MODEL)
        requestBuilder.addHeader("APP-PLATFORM", "1")
        requestBuilder.addHeader("APP-TIMESTAMP", "$timestamp")
        requestBuilder.addHeader("APP-NONCE", nonce)
        requestBuilder.addHeader("APP-SIGN", encodeMD5(signSource))
        requestBuilder.addHeader("APP-VERSION", BuildConfig.VERSION_NAME)
        requestBuilder.addHeader("APP-CHANNEL", "0")
        requestBuilder.addHeader("Authorization", ACCESS_TOKEN.getStringMMKV())
        return requestBuilder
    }

    /**
     * 随机生成16位字符的字符串
     */
    private fun generateNonce(): String {
        val nonceScope = "1234567890abcdefghijklmnopqrstuvwxyz"
        val nonceItem: (Int) -> Char = { nonceScope[(nonceScope.length * Math.random()).toInt()] }
        return Array(16, nonceItem).joinToString("")
    }
}


interface RefreshApiService {
    /**
     * 刷新token(令牌)
     * @param refreshToken String
     */
    @POST("auth/refresh")
    fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): Call<BaseResponse<RefreshTokenBean>>
}