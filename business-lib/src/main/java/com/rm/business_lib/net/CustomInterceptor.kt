package com.rm.business_lib.net

import android.os.Build
import com.google.gson.Gson
import com.rm.baselisten.BuildConfig
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.encodeMD5
import com.rm.baselisten.util.getStringMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.*
import com.rm.business_lib.bean.RefreshTokenBean
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
        val original = chain.request()
        val request: Request = getRequestHeaderBuilder(original.newBuilder()).build()
        val originalResponse = arrayOf(chain.proceed(request))
        // 返回参数拦截处理
        responseIntercept(originalResponse, chain)
        return originalResponse[0]
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
        val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)
        if (baseResponse.code == CODE_REFRESH_TOKEN) {
            // 当前token过期，需要刷新token
            DLog.d(TAG, "token过期，刷新token")
            // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常
            val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
            if (result?.code == 0) {
                // 刷新token成功，保存最新token
                ACCESS_TOKEN.putMMKV(result.data.access)
                REFRESH_TOKEN.putMMKV(result.data.refresh)
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
        }else if(baseResponse.code == CODE_LOGIN_OUT){
            // 被挤下线，强制退出了
            loginOut()
            originalResponse[0] = originalResponse[0].newBuilder().code(200)
                .body(responseStr.toResponseBody(contentType)).build()
        }else {
            // 正常请求,下发数据到具体请求位置
            originalResponse[0] = originalResponse[0].newBuilder().code(200)
                .body(responseStr.toResponseBody(contentType)).build()
        }
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

/**
 * 登出
 */
fun loginOut() {
    // 保存登陆信息到本地
    ACCESS_TOKEN.putMMKV("")
    REFRESH_TOKEN.putMMKV("")
    LOGIN_USER_INFO.putMMKV("")

    // 改变当前是否用户登陆状态 和 登陆的用户信息
    isLogin.value = false
    loginUser.value = null
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