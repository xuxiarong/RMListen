package com.rm.business_lib.net

import android.os.Build
import android.text.TextUtils
import com.google.gson.Gson
import com.rm.baselisten.BuildConfig
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.encodeMD5
import com.rm.baselisten.util.getStringMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.ACCESS_TOKEN
import com.rm.business_lib.ACCESS_TOKEN_INVALID_TIMESTAMP
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.helpter.parseToken
import com.rm.business_lib.net.api.BusinessApiService
import com.rm.business_lib.utils.DeviceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.notifyAll
import okhttp3.internal.wait
import java.util.concurrent.atomic.AtomicBoolean

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

    // 用户未登陆
    private val CODE_NOT_LOGIN = 1013

    // 被强制登出了(被挤下线了)
    private val CODE_LOGIN_OUT = 1204

    // 刷新token失败
    private val CODE_REFRESH_TOKEN_FAILED = 1014

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            BusinessApiService::class.java
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // 请求拦截处理
//        val originalResponse = requestIntercept(chain)
        // 构建请求
        val originalResponse = buildRequestResponse(chain)
        // 返回参数拦截处理
        responseIntercept(originalResponse, chain)
        return originalResponse[0]
    }

//    /**
//     * 拦截请求数据，判断访问token是否已过期
//     * @return Boolean
//     */
//    private fun requestIntercept(chain: Interceptor.Chain): Array<Response> {
//        if (TextUtils.isEmpty(ACCESS_TOKEN.getStringMMKV())
//            || ACCESS_TOKEN_INVALID_TIMESTAMP.getLongMMKV(0) > System.currentTimeMillis()/1000
//            || chain.request().url.toUri().path.contains("auth/refresh")
//        ) {
//            // 证明未登陆 或者 访问令牌token还未过期 或者 是刷新token的接口， 则不去判断token是否过期了,直接请求
//            return buildRequestResponse(chain)
//        }
//
//        DLog.d(TAG, "请求时，验证token已过期，刷新token")
//        // 到这里，说明刷新token已经过期了，则去刷新接口
//        // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常
//        val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
//        return if (result?.code == 0) {
//            // 刷新token成功，保存最新token
//            updateLocalToken(result.data.access, result.data.refresh)
//            // 再去构造真正用户需要请求的接口请求信息
//            buildRequestResponse(chain)
//        } else {
//            // 刷新token失败，则还是让用户去请求，提示他token过期
//            buildRequestResponse(chain)
//        }
//    }

    /**
     * 构建之前的请求
     * @param chain Chain
     * @return Array<Response>
     */
    private fun buildRequestResponse(chain: Interceptor.Chain): Array<Response> {
        // TODO: 12/5/20  部分请求，不需要toekn，请进行过滤
        val url = chain.request().url
        DLog.i("===========>>>>", "url:$url")

        var filter = false;
        return if (filter) {
            val request: Request = getRequestHeaderBuilder(chain.request().newBuilder()).build()
            arrayOf(chain.proceed(request))
        } else {
            //需token的网络请求，发现正在有刷新token的任务，阻塞
            if (refreshIng.get()) {
                awite()
            }
            val request: Request = getRequestHeaderBuilder(chain.request().newBuilder()).build()
            arrayOf(chain.proceed(request))
        }

    }

    /**
     * 判断该请求是否需要token
     */
    private fun canToken() {

    }

    /**
     * 是否有刷新token的任务进行着
     */
    var refreshIng = AtomicBoolean(false);

    /**
     * 拦截返回数据，判断是否需要刷新token等操作
     * @param originalResponse Response
     * @return Response
     */
    private fun responseIntercept(originalResponse: Array<Response>, chain: Interceptor.Chain) {
        try {
            //保存body的相关信息，用于后续构造新的Response
            val responseBody = originalResponse[0].body ?: return
            val responseCode = originalResponse[0].code
            val contentType = responseBody.contentType()
            val responseStr = responseBody.string()
            responseBody.close()
            if (TextUtils.isEmpty(responseStr)) {
                // 没有返回信息，说明没有网络，未请求成功，则原封不动的返回数据
                originalResponse[0] =
                    originalResponse[0].newBuilder().code(originalResponse[0].code)
                        .body(responseStr.toResponseBody(contentType)).build()
                return
            }
            val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)
            if (baseResponse.code == CODE_REFRESH_TOKEN) {
                refreshIng.set(true)
                // 当前token过期，需要刷新token
                DLog.d("======>", "响应时，token已过期，刷新token")
                // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常

                val token: String
                try {
                    token = refreshToken()
                    refreshIng.set(false)
                    DLog.d("======>", "00000，刷新token   ${chain.request().url}")
                    // 将之前拦截的接口重新请求并下发
                    val request: Request.Builder =
                        getRequestHeaderBuilder(chain.request().newBuilder(), token)
                    try {
                        originalResponse[0] = chain.proceed(request.build())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        DLog.d("======>", "Exception1111，刷新token")
                        // 再次请求之前拦截的接口失败，就下发之前请求到的数据到具体位置
                        originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
                            .body(responseStr.toResponseBody(contentType)).build()
                    }
                } catch (e: java.lang.Exception) {
                    DLog.d("======>", "Exception22222，刷新token")
                    // 刷新token失败，强制退出当前登陆
                    GlobalScope.launch(Dispatchers.Main) {
                        loginOut()
                    }
                    // TODO 如果刷新token失败，将之前请求的数据同样下发到具体位置，不过是否需要自己组一个账户已退出的消息数据进行下发？？？
                    originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
                        .body(responseStr.toResponseBody(contentType)).build()
                }


            } else if (baseResponse.code == CODE_LOGIN_OUT || baseResponse.code == CODE_NOT_LOGIN || baseResponse.code == CODE_REFRESH_TOKEN_FAILED) {

                if (baseResponse.code == CODE_NOT_LOGIN && refreshIng.get()) {
                    //阻塞，直到获取到token
                    val token = refreshToken()
                    // 将之前拦截的接口重新请求并下发
                    val request: Request.Builder =
                        getRequestHeaderBuilder(chain.request().newBuilder(), token)
                    try {
                        originalResponse[0] = chain.proceed(request.build())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // 再次请求之前拦截的接口失败，就下发之前请求到的数据到具体位置
                        originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
                            .body(responseStr.toResponseBody(contentType)).build()
                    }

                } else {
                    // 被挤下线，强制退出了  或者 直接是未登陆  或者 刷新token的时候，刷新token失败(每次app启动会有一个刷新token操作，所以也要在这里判断)
                    GlobalScope.launch(Dispatchers.Main) {
                        loginOut()
                    }
                    originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
                        .body(responseStr.toResponseBody(contentType)).build()
                }


            } else {
                // 正常请求,下发数据到具体请求位置
                originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
                    .body(responseStr.toResponseBody(contentType)).build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    var refreshInit = AtomicBoolean(false)

    @Volatile
    var newToken: String? = null


    /**
     * 采用生产与消费模型，一个网络请求负责刷新token,其余网络请求都阻塞等待消费token
     */
    private fun refreshToken(): String {
        if (refreshInit.compareAndSet(false, true)) {
            try {
                newToken = null
                return refreshRealToken();
            } catch (e: java.lang.Exception) {
                throw e
            } finally {
                refreshInit.set(false)
                //结果如何，最终一定要唤醒
                awake()
            }

        } else {
            awite()
//            if (TextUtils.isEmpty(newToken)) {
//                throw  Throwable("token refresh fail")
//            }
            return REFRESH_TOKEN.getStringMMKV("");
        }

    }

    /**
     * 阻塞挂起
     */
    protected fun awite() {
        synchronized(this) {
            this.wait();
        }
    }

    /**
     * 全部唤醒
     */
    private fun awake() {
        synchronized(this) {
            this.notifyAll()
        }
    }

    private fun refreshRealToken(): String {
        DLog.i("========>  refreshRealToken", "${REFRESH_TOKEN.getStringMMKV("")}")
        val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
        if (result?.code == 0) {
            // 刷新token成功，保存最新token
            updateLocalToken(result.data.access, result.data.refresh)
            newToken = result.data.refresh
            return newToken!!
        }
        throw  Throwable("token refresh fail")
    }

    /**
     * 添加公共的 header
     * @param requestBuilder Builder
     * @return Request.Builder
     */
    private fun getRequestHeaderBuilder(
        requestBuilder: Request.Builder,
        token: String? = null
    ): Request.Builder {
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
        if (TextUtils.isEmpty(token)) {
            requestBuilder.addHeader("Authorization", "Bearer ${ACCESS_TOKEN.getStringMMKV()}")
        } else {
            DLog.i("========>  getRequestHeaderBuilder   22222", "$token")

            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
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