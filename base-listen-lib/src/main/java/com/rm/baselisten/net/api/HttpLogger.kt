package com.rm.baselisten.net.api

import com.rm.baselisten.util.DLog
import okhttp3.logging.HttpLoggingInterceptor

/**
 * desc   : 网络日志类
 * date   : 2020/09/01
 * version: 1.0
 */
class HttpLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        DLog.d("HttpLog", message)
    }
}