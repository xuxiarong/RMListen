package com.rm.business_lib.net

import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BuildConfig
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.net.util.GsonUtils
import com.rm.baselisten.util.*
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
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.GzipSource
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch

/**
 * desc   : 业务网络拦截器
 * date   : 2020/09/01
 * version: 1.0
 */
class RefreshTokenInterceptor1 : Interceptor {
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

        private val mRefreshListenerList = mutableListOf<OnRefreshListener>()

        @Volatile
        private var isRefreshing = false

    }

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            BusinessApiService::class.java
        )
    }


    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val originRequest = chain.request()
            val request = getRequestHeaderBuilder(originRequest.newBuilder()).build()
            //获取请求结果
            val originResponse = chain.proceed(request)

            originResponse.code


            val body = originResponse.body
            //没有返回信息，说明没有网络，未请求成功，则原封不动的返回数据
            val responseString = getResponseString(originResponse)
            if (TextUtils.isEmpty(responseString)) {
                return originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            }
            val code = getResponseCode(responseString)
            return if (code == CODE_REFRESH_TOKEN) {
                refreshToken(chain, request)
            } else if (code == CODE_LOGIN_OUT || code == CODE_NOT_LOGIN || code == CODE_REFRESH_TOKEN_FAILED) {
                //被挤下线了/用户未登陆  需要放在主线程去更新，不然会出现异常
                GlobalScope.launch(Dispatchers.Main) {
                    loginOut()
                }
                originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            } else {
                originResponse.newBuilder().code(originResponse.code)
                    .body(body).build()
            }
        } catch (e: Exception) {
            val json = GsonUtils.toJson(BaseResponse(10086, "服务器连接异常", Any()))
            return Response.Builder()
                .request(chain.request())
                .code(200)
                .body(json.toResponseBody())
                .message("请求出错")
                .protocol(Protocol.HTTP_2).build()
        }
    }

    private fun refreshToken(chain: Interceptor.Chain, request: Request): Response {
        if (!isRefreshing) {
            isRefreshing = true
            val token = REFRESH_TOKEN.getStringMMKV("")
            val newToken = getToken(token)
            //token刷新成功
            if (!TextUtils.isEmpty(newToken)) {
                val headers = request.headers.newBuilder()
                headers["Authorization"] = "Bearer $newToken"
                val newRequest = request.newBuilder().headers(headers.build()).build()
                isRefreshing = false
                triggerRefreshListener(true, newToken)
                return chain.proceed(newRequest)
            } else {
                //token 刷新失败
                GlobalScope.launch(Dispatchers.Main) {
                    val activity = BaseApplication.baseApplication.getTopTaskActivity()
                    activity?.let { context ->
                        ToastUtil.showTopToast(
                            context,
                            "登录凭证已过期，请重新登陆"
                        )
                    }
                    loginOut()
                }
                val headers = request.headers.newBuilder()
                headers["Authorization"] = "Bearer "
                val newRequest = request.newBuilder().headers(headers.build()).build()
                triggerRefreshListener(false, "")
                return chain.proceed(newRequest)
            }
        } else {
            val countDownLatch = CountDownLatch(1)
            mRefreshListenerList.add(object : OnRefreshListener {
                override fun onRefresh(isSuccess: Boolean, accessToken: String?) {
                    mRefreshListenerList.remove(this)
                    if (isSuccess) {
                        countDownLatch.countDown()
                    } else {

                    }
                }
            })
            try {
                countDownLatch.await()
            } catch (e: InterruptedException) {
                DLog.i("countDownLatch InterruptedException:", e.toString());
            }
        }
        return chain.proceed(request)
    }

    @Throws(IOException::class)
    fun getToken(token: String): String {
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
        if (body != null) {
            val contentLength = body.contentLength()
            val source = body.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer
            val headers = response.headers
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }
            val contentType = body.contentType()
            val charset: Charset =
                contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
            return if (contentLength != 0L) {
                buffer.clone().readString(charset)
            } else {
                null
            }
        } else {
            return null
        }
    }

    private fun getResponseCode(readString: String?): Int {
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

    /**
     * 将RefreshToken结果通知到所有队列中等待的线程
     *
     */
    private fun triggerRefreshListener(isSuccess: Boolean, accessToken: String) {
        for (onRefreshListener in mRefreshListenerList) {
            onRefreshListener.onRefresh(isSuccess, accessToken)
        }
    }


    interface OnRefreshListener {
        fun onRefresh(isSuccess: Boolean, accessToken: String?)
    }
}

