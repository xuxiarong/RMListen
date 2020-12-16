package com.rm.business_lib.net

import android.os.Build
import android.text.TextUtils
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
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
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

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val originRequest = chain.request()
            val request = getRequestHeaderBuilder(originRequest.newBuilder()).build()
            //获取请求结果
            val originResponse = chain.proceed(request)
            val body = originResponse.body
            // 原请求地址
            val originReqUrl = originRequest.url.toString()
            //没有返回信息，说明没有网络，未请求成功，则原封不动的返回数据
            DLog.i("=====>RefreshTokenInterceptor", "=====>>>>$originReqUrl")

            if (TextUtils.isEmpty(getResponseString(originResponse))) {
                DLog.i("=====>RefreshTokenInterceptor", "=====000000000   ${request.url}")
                return originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            }
            val code = getResponseCode(originResponse)

            return if (code == CODE_REFRESH_TOKEN) {

                val token = request.headers["TOKEN"] ?: REFRESH_TOKEN.getStringMMKV("")
                val newToken = getToken(token)
                //token刷新成功
                if (!TextUtils.isEmpty(newToken)) {
                    val headers = request.headers.newBuilder()
                    headers["Authorization"] = "Bearer $newToken"
                    val newRequest = request.newBuilder().headers(headers.build()).build()
                    DLog.i("=====>RefreshTokenInterceptor", "=====111111  ${request.url}")
                    chain.proceed(newRequest)
                } else {
                    //token 刷新失败
                    loginOut()
                    val headers = request.headers.newBuilder()
                    headers["Authorization"] = "Bearer "
                    val newRequest = request.newBuilder().headers(headers.build()).build()
                    DLog.i("=====>RefreshTokenInterceptor", "=====2222222  ${request.url}")
                    chain.proceed(newRequest)
                }
            } else if (code == CODE_LOGIN_OUT || code == CODE_NOT_LOGIN /*|| code == CODE_REFRESH_TOKEN_FAILED*/) {
                //被挤下线了/用户未登陆/
                loginOut()
//            val headers = request.headers.newBuilder()
//            headers["Authorization"] = "Bearer "
//            val newRequest = request.newBuilder().headers(headers.build()).build()
//            DLog.i("=====>RefreshTokenInterceptor", "=====333333 $code")
//            chain.proceed(newRequest)
                originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            } else {
                DLog.i("=====>RefreshTokenInterceptor", "=====444444 ${request.url}")
                originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            }
        } catch (e: Exception) {
            val str="{\n" +
                    "    \"code\":1009,\n" +
                    "    \"msg\":\"请求报错\",\n" +
                    "    \"data\":{}}"
            return Response.Builder()
                .request(chain.request())
                .code(200)
                .body(str.toResponseBody())
                .message("请求出错")
                .protocol(Protocol.HTTP_2).build()

        }
    }

    @Throws(IOException::class)
    fun getToken(token: String): String {
        val refreshToken = REFRESH_TOKEN.getStringMMKV("")
        DLog.i("=====>RefreshTokenInterceptor", "=====getToken $refreshToken")

        val result = apiService.refreshToken(token).execute().body()
        return if (result?.code == 0) {
            updateLocalToken(result.data.access, result.data.refresh)
            result.data.access
        } else {
            ""
        }
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

    private fun getResponseCode(response: Response): Int {
        val readString = getResponseString(response)
        if (readString.isNullOrEmpty()) {
            return -1
        }
        return try {
            val json = JSONObject(readString)
            json.getInt("code")
        } catch (e: Exception) {
            -1
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
        requestBuilder.addHeader("TOKEN", REFRESH_TOKEN.getStringMMKV())
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

