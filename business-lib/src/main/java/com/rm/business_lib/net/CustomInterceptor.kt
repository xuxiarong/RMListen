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
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.net.api.BusinessApiService
import com.rm.business_lib.utils.DeviceUtils
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

/**
 * desc   : 业务网络拦截器
 * date   : 2020/09/01
 * version: 1.0
 */
class CustomInterceptor : Interceptor {

    private val TAG = "com.rm.business_lib.net.CustomInterceptor"

    // 服务器固定的 Android端app_key
    private val APP_KEY = "7yeGUJZS7YLXTFSqJTC5uJTvjOd25t0X"

    companion object {
        // 本地时间和服务器不一致，返回的错误码(需要重新获取服务器时间，并计算差值)
        private const val CODE_TIMESTAMP_ERROR = 1014

        // 用户未登陆(请求了需要登陆后才能请求的接口，会返回此状态码，需要退出本地登陆用户信息，不用再请求获取新的游客访问token了)
        private const val CODE_UN_LOGIN = 1013

        // token错误(需要退出token，并重新获取游客token)
        private const val CODE_TOKEN_ERROR = 1003

        // token过期(需要刷新token)
        private const val CODE_TOKEN_EXPIRED = 1004
    }


//    // 刷新token失败
//    private val CODE_REFRESH_TOKEN_FAILED = 401

//    // 用户未登陆
//    private val CODE_NOT_LOGIN = 1013
//
//    // 被强制登出了(被挤下线了)
//    private val CODE_LOGIN_OUT = 1204

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            BusinessApiService::class.java
        )
    }

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        // 请求拦截处理
//        val originalResponse = requestIntercept(chain)
        synchronized(CustomInterceptor::class.java) {
            // 构建请求
            val originalResponse = buildRequestResponse(chain)
            responseIntercept(originalResponse, chain)
            return originalResponse[0]
        }
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
        try {
            val request: Request = getRequestHeaderBuilder(chain.request().newBuilder()).build()
            return arrayOf(chain.proceed(request))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return arrayOf(
                Response.Builder()
                    .request(chain.request())
                    .code(200)
                    .body("ss".toResponseBody())
                    .message("请求出错")
                    .protocol(Protocol.HTTP_2).build()
            )
        }
    }

    /**
     * 拦截返回数据，判断是否需要刷新token等操作
     * @param originalResponse Response
     * @return Response
     */
    private fun responseIntercept(originalResponse: Array<Response>, chain: Interceptor.Chain) {
        //保存body的相关信息，用于后续构造新的Response
        val responseBody = originalResponse[0].body ?: return
        val responseCode = originalResponse[0].code
        val contentType = responseBody.contentType()
        val responseStr = responseBody.string()
        responseBody.close()
        if (responseCode == 200) {
            originalResponse[0] = checkResponseCode(
                originalResponse[0],
                responseCode,
                responseStr,
                chain,
                contentType
            )
        } else {
            // 网络接口请求本身状态是失败的
            // 原封不动的返回响应消息
            originalResponse[0] =
                returnResponse(originalResponse[0], responseCode, responseStr, contentType)
        }

//        val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)
//        if (baseResponse.code == CODE_REFRESH_TOKEN) {
//            // 当前token过期，需要刷新token
//            DLog.d(TAG, "响应时，token已过期，刷新token")
//            // 注意，一定要在当前线程同步请求刷新接口，否则再重新请求之前的接口会抛异常
//            val result =
//                apiService.refreshToken(REFRESH_TOKEN_KEY.getStringMMKV("")).execute().body()
//            if (result?.code == 200) {
//                // 刷新token成功，保存最新token
//                updateLocalToken(result.data.access, result.data.refresh)
//                // 将之前拦截的接口重新请求并下发
//                val request: Request.Builder = getRequestHeaderBuilder(chain.request().newBuilder())
//                try {
//                    originalResponse[0] = chain.proceed(request.build())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    // 再次请求之前拦截的接口失败，就下发之前请求到的数据到具体位置
//                    originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
//                        .body(responseStr.toResponseBody(contentType)).build()
//                }
//            } else {
//                if (result?.code == SIGN_IN_TOKEN_INVALID) {
//                    // 刷新token失败，强制退出当前登陆
//                    GlobalScope.launch(Dispatchers.Main) {
//                        userLoginOut()
//                    }
//                }
//                // TODO 如果刷新token失败，将之前请求的数据同样下发到具体位置，不过是否需要自己组一个账户已退出的消息数据进行下发？？？
//                originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
//                    .body(responseStr.toResponseBody(contentType)).build()
//            }
//        } else if (baseResponse.code == SIGN_IN_TOKEN_INVALID) {
//            // 登陆信息已失效
//            GlobalScope.launch(Dispatchers.Main) {
//                // 清空当前登陆信息
//                userLoginOut()
//            }
//            // TODO 如果刷新token失败，将之前请求的数据同样下发到具体位置，不过是否需要自己组一个账户已退出的消息数据进行下发？？？
//            originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
//                .body(responseStr.toResponseBody(contentType)).build()
//        }
////        else if (baseResponse.code == CODE_LOGIN_OUT || baseResponse.code == CODE_NOT_LOGIN || baseResponse.code == CODE_REFRESH_TOKEN_FAILED) {
////            // 被挤下线，强制退出了  或者 直接是未登陆  或者 刷新token的时候，刷新token失败(每次app启动会有一个刷新token操作，所以也要在这里判断)
////            // TODO
//////            GlobalScope.launch(Dispatchers.Main) {
//////                loginOut()
//////            }
////            originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
////                .body(responseStr.toResponseBody(contentType)).build()
////        }
//        else {
//            // 正常请求,下发数据到具体请求位置
//            originalResponse[0] = originalResponse[0].newBuilder().code(responseCode)
//                .body(responseStr.toResponseBody(contentType)).build()
//        }
    }

    /**
     * 返回Response消息
     */
    private fun returnResponse(
        response: Response,
        responseCode: Int,
        responseStr: String,
        contentType: MediaType?
    ): Response {
        return response.newBuilder().code(responseCode)
            .body(responseStr.toResponseBody(contentType)).build()
    }

    /**
     * 统一返回网络错误消息
     */
    private fun returnErrorResponse(
        response: Response,
        contentType: MediaType?
    ): Response {
        return returnResponse(response, 9009, "Network Error", contentType)
    }

    /**
     * 构建一个新的请求(请求被拦截下来的接口)
     */
    private fun buildNewRequest(chain: Interceptor.Chain): Response? {
        return try {
            val request: Request.Builder = getRequestHeaderBuilder(chain.request().newBuilder())
            chain.proceed(request.build())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 检查服务器返回的业务code，并处理
     */
    private fun checkResponseCode(
        response: Response,
        responseCode: Int,
        responseStr: String,
        chain: Interceptor.Chain,
        contentType: MediaType?
    ): Response {
        val tag = chain.request().tag(String::class.java)
//        if (tag != null && TextUtils.equals(TAG_TXT_URL, tag)) {
//            // 直接是文本数据返回，不是统一数据格式(小说内容url链接的请求，需要自己组合，与统一格式保持一致)
//            val jsonObject = JSONObject()
//            jsonObject.put("code", 0)
//            jsonObject.put("msg", "")
//            jsonObject.put("data", responseStr)
//            return returnResponse(response, responseCode, jsonObject.toString(), contentType)
//        }
        // 解析服务器数据
        val baseResponse = Gson().fromJson(responseStr, BaseResponse::class.java)
            ?: return returnResponse(response, responseCode, responseStr, contentType)// 原封不动的返回响应消息
        return when (baseResponse.code) {
//            CODE_TIMESTAMP_ERROR -> {
//                // 服务器时间和本地时间的差值太大
//                // 去请求服务器数据，并和本地时间再重新计算差值
//                val result = apiService.getServiceTime().execute().body()
//                if (result != null) {
//                    if (result.code == 0) {
//                        // 请求服务器时间成功，计算时间差并保存
//                        KEY_DIFFERENCE_TIME.putMMKV(result.data.time * 1000 - (System.currentTimeMillis()))
//                        // 将之前拦截的请求失败的接口重新请求并下发
//                        // 成功：返回新的响应结果，失败：返回网络错误响应
//                        buildNewRequest(chain) ?: returnErrorResponse(response, contentType)
//                    } else {
//                        // 请求服务器时间失败,返回网络错误消息
//                        returnErrorResponse(response, contentType)
//                    }
//                } else {
//                    // 请求服务器时间失败,返回网络错误消息
//                    returnErrorResponse(response, contentType)
//                }
//            }
            CODE_TOKEN_EXPIRED -> {
                // token过期(需要刷新token)
                val refreshResult =
                    apiService.refreshToken(REFRESH_TOKEN.getStringMMKV("")).execute().body()
                if (refreshResult != null) {
                    // 刷新token成功
                    // 保存新的token
                    updateToken(
                        refreshResult.data.access,
                        refreshResult.data.refresh
                    )
                    // 将之前拦截的请求失败的接口重新请求并下发
                    // 成功：返回新的响应结果，失败：返回网络错误响应
                    buildNewRequest(chain) ?: returnErrorResponse(response, contentType)
                } else {
                    // 请求服务器时间失败,返回网络错误消息
                    returnErrorResponse(response, contentType)
                }
            }
//            CODE_TOKEN_ERROR -> {
//                // token错误(需要退出token，并重新获取游客token)
//                // 退出当前登陆的账户
//                loginOut()
//                // 请求获取新的游客访问token
//                val tokenResult =
//                    apiService.getTouristToken(getRequestTouristTokenParams()).execute().body()
//                if (tokenResult != null) {
//                    // 获取游客token成功
//                    // 保存新的游客token
//                    updateToken(
//                        tokenResult.data.access_token,
//                        tokenResult.data.refresh_token
//                    )
//                    // 将之前拦截的请求失败的接口重新请求并下发
//                    // 成功：返回新的响应结果，失败：返回网络错误响应
//                    buildNewRequest(chain) ?: returnErrorResponse(response, contentType)
//                } else {
//                    // 请求服务器时间失败,返回网络错误消息
//                    returnErrorResponse(response, contentType)
//                }
//
//            }
            CODE_UN_LOGIN -> {
                // 用户未登陆(请求了需要登陆后才能请求的接口，会返回此状态码，需要退出本地登陆用户信息，不用再请求获取新的游客访问token了)
                // 退出当前登陆的账户
                loginOut()
                // 再正常下发数据
                returnResponse(response, responseCode, responseStr, contentType)
            }
            else -> {
                // 其他正常下发(这里面包含真正请求成功的，和其他可能错误的情况)
                returnResponse(response, responseCode, responseStr, contentType)
            }
        }
    }

//    /**
//     * 请求获取游客访问token所需参数
//     */
//    private fun getRequestTouristTokenParams(): Map<String, String> {
//        return HashMap<String, String>().apply {
//            this["version"] = BuildConfig.VERSION_NAME
//            this["platform"] = "4"
//            this["channel"] = "0"
//            this["device_id"] = DeviceUtils.uniqueDeviceId
//            this["device_model"] = Build.MODEL
//            this["cid"] = ""
//        }
//    }


    /**
     * 更新本地token相关信息
     * @param access String
     * @param refresh String
     */
    private fun updateToken(access: String, refresh: String) {
        ACCESS_TOKEN.putMMKV(access)
        REFRESH_TOKEN.putMMKV(refresh)
        // 更新访问token的过期时间 TODO
//        ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(parseToken(access))
    }


    /**
     * 添加公共的 header
     * @param requestBuilder Builder
     * @return Request.Builder
     */
    private fun getRequestHeaderBuilder(
        requestBuilder: Request.Builder
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
        requestBuilder.addHeader("Authorization", "Bearer ${ACCESS_TOKEN.getStringMMKV()}")
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