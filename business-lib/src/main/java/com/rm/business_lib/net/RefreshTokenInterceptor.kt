package com.rm.business_lib.net

import android.os.Build
import com.rm.baselisten.BuildConfig
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
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * desc   : 业务网络拦截器
 * date   : 2020/09/01
 * version: 1.0
 */
class RefreshTokenInterceptor : Interceptor {
    companion object {
        const val TAG = "RefreshTokenInterceptor"

        // 服务器固定的 Android端app_key
        const val APP_KEY = "5d25eb5bf85ef867e78172d0d0df5ef0"

        // 需要刷新token服务器返回的code
        const val CODE_REFRESH_TOKEN = 1004

        // 用户未登陆
        const val CODE_NOT_LOGIN = 1013

        // 被强制登出了(被挤下线了)
        const val CODE_LOGIN_OUT = 1204

        // 刷新token失败
        const val CODE_REFRESH_TOKEN_FAILED = 1014
    }

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            BusinessApiService::class.java
        )
    }

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request: Request = getRequestHeaderBuilder(chain.request().newBuilder()).build()
            return responseIntercept(chain.proceed(request), chain)
        } catch (e: Throwable) {
            if (e is IOException) {
                throw e
            } else {
                throw IOException(e)
            }
        }
    }

    /**
     * 拦截返回数据，判断是否需要刷新token等操作
     * @param response Response
     * @return Response
     */
    private fun responseIntercept(response: Response, chain: Interceptor.Chain): Response {
        val readString = getResponseString(response)
        if (readString.isNullOrEmpty()) {
            return response
        }
        val json = JSONObject(readString)
        val code = json.get("code")
        DLog.i(TAG, "code:$code")
        return if (code == CODE_REFRESH_TOKEN) {
            refreshToken(response, chain)
        } else if (code == CODE_LOGIN_OUT || code == CODE_NOT_LOGIN) {
            loginOut()
            chain.proceed(retryLoad(response))
        } else {
            response
        }
    }

    @Synchronized
    private fun refreshToken(response: Response, chain: Interceptor.Chain): Response {
        val result = apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
        //如果刷新成功
        if (result?.code == 0) {
            // 刷新token成功，保存最新token
            DLog.i(TAG, "refreshToken:" + result.data.access)
            updateLocalToken(result.data.access, result.data.refresh)
            return chain.proceed(retryLoad(response))
        } else {
            if (result?.code == CODE_REFRESH_TOKEN_FAILED) {
                loginOut()
                return chain.proceed(retryLoad(response))
            }
        }
        return response
    }

    /**
     * 重试
     */
    private fun retryLoad(response: Response): Request {
        DLog.i(TAG, "retryLoad:")
        val timestamp = System.currentTimeMillis() / 1000
        // 随机生成的16位数字符
        val nonce = generateNonce()
        val signSource =
            StringBuilder().append(timestamp).append(nonce).append(APP_KEY).toString()
        return response.request.newBuilder()
            .header(
                "Authorization",
                "Bearer ${ACCESS_TOKEN.getStringMMKV()}"
            )
            .header("APP-SIGN", encodeMD5(signSource))
            .header("APP-NONCE", nonce)
            .header("APP-TIMESTAMP", "$timestamp")
            .method(response.request.method, response.request.body)
            .build()
    }

    private fun getResponseString(response: Response): String? {
        val body = response.body
        val source = body?.source()
        val buffer = source?.buffer
        val contentType = body?.contentType()
        val charset: Charset =
            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        return buffer?.clone()?.readString(charset)

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
        val signSource =
            StringBuilder().append(timestamp).append(nonce).append(APP_KEY).toString()
        requestBuilder.addHeader("APP-DEVICE", DeviceUtils.uniqueDeviceId)
        requestBuilder.addHeader("APP-DEVICE-MODEL", Build.MODEL)
        requestBuilder.addHeader("APP-PLATFORM", "1")
        requestBuilder.addHeader("APP-TIMESTAMP", "$timestamp")
        requestBuilder.addHeader("APP-NONCE", nonce)
        requestBuilder.addHeader("APP-SIGN", encodeMD5(signSource))
        requestBuilder.addHeader("APP-VERSION", BuildConfig.VERSION_NAME)
        requestBuilder.addHeader("APP-CHANNEL", "0")
        requestBuilder.addHeader("Authorization", "Bearer ${ACCESS_TOKEN.getStringMMKV()}")
        return requestBuilder
    }

    /**
     * 随机生成16位字符的字符串
     */
    private fun generateNonce(): String {
        val nonceScope = "1234567890abcdefghijklmnopqrstuvwxyz"
        val nonceItem: (Int) -> Char =
            { nonceScope[(nonceScope.length * Math.random()).toInt()] }
        return Array(16, nonceItem).joinToString("")
    }
}

