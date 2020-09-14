package com.rm.business_lib.net

import android.os.Build
import com.google.gson.Gson
import com.rm.baselisten.BuildConfig
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.encodeMD5
import com.rm.baselisten.util.getStringMMKV
import com.rm.business_lib.ACCESS_TOKEN
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.business_lib.bean.RefreshTokenBean
import com.rm.business_lib.utils.DeviceUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * desc   : 业务网络拦截器
 * date   : 2020/09/01
 * version: 1.0
 */
class CustomInterceptor : Interceptor {

    // 服务器固定的 Android端app_key
    private val APP_KEY = "5d25eb5bf85ef867e78172d0d0df5ef0"

    companion object {
        var flag = 0
    }

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            RefreshApiService::class.java
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        DLog.i("llj", "当前线程----->>>${Thread.currentThread().name}")
        val original = chain.request()
        val request: Request = getRequestHeaderBuilder(original.newBuilder()).build()
        val originalResponse = arrayOf(chain.proceed(request))
        return originalResponse[0]
//        // 返回参数拦截处理
//        responseIntercept(originalResponse, chain)
//        return originalResponse[0]
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


    /**
     * 拦截返回数据，判断是否需要刷新token
     * @param response Response
     * @return Response
     */
    private fun responseIntercept(originalResponse: Array<Response>, chain: Interceptor.Chain) {

        //保存body的相关信息，用于后续构造新的Response
        val responseBody = originalResponse[0].body
        val contentType = responseBody?.contentType()
        val responseStr = responseBody?.string()
        responseBody?.close()
        val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)

//        if (baseResponse.code == 0) {
        if (flag == 0) {
            flag++
            // token 过期，需要刷新token
            GlobalScope.launch {
                val refreshResponse = GlobalScope.async {
                    apiService.refreshToken(REFRESH_TOKEN.getStringMMKV(""))
                }
                if (refreshResponse.await().code != 0) {
                    // 刷新成功,再次去请求之前的接口
                    DLog.i("llj", "刷新成功,再次去请求之前的接口")
                    val request: Request.Builder =
                        getRequestHeaderBuilder(chain.request().newBuilder())
                    try {
                        originalResponse[0] = chain.proceed(request.build())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    // 刷新失败，退出登陆
                    DLog.e("llj", "刷新token失败！！")
                    originalResponse[0] = originalResponse[0].newBuilder().code(200).body(
                        responseStr?.let {
                            ResponseBody.create(
                                contentType,
                                it
                            )
                        }).build()
                }
            }

        } else {
            DLog.i("llj", "返回码------>>>${baseResponse.code}")
            originalResponse[0] = originalResponse[0].newBuilder().code(200).body(responseStr?.let {
                ResponseBody.create(
                    contentType,
                    it
                )
            }).build()
        }
    }
}

interface RefreshApiService {
    /**
     * 刷新token(令牌)
     * @param refreshToken String
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): BaseResponse<RefreshTokenBean>
}