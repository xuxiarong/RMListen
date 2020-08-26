package com.rm.baselisten.util

import android.annotation.SuppressLint
import android.util.Log
import com.rm.baselisten.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

/**
 * desc   :
 * date   : 2020/08/26
 * version: 1.0
 */
object DLog {
    private val DEBUG = BuildConfig.DEBUG
    private const val APP_PREFIX = ""
    fun d(TAG: String, method: String, msg: String) {
        if (DEBUG) {
            Log.d(APP_PREFIX + TAG, "[$method]$msg")
        }
    }

    fun d(TAG: String, msg: String) {
        if (DEBUG) {
            Log.d(APP_PREFIX + TAG, "[$fileLineMethod]$msg")
        }
    }

    fun e(TAG: String, method: String, msg: String) {
        if (DEBUG) {
            Log.e(APP_PREFIX + TAG, "[$method]$msg")
        }
    }

    fun e(TAG: String, msg: String) {
        if (DEBUG) {
            Log.e(APP_PREFIX + TAG, "[$fileLineMethod]$msg")
        }
    }

    fun i(TAG: String, method: String, msg: String) {
        if (DEBUG) {
            Log.i(APP_PREFIX + TAG, "[$method]$msg")
        }
    }

    fun i(TAG: String, msg: String) {
        if (DEBUG) {
            Log.i(APP_PREFIX + TAG, "[$fileLineMethod]$msg")
        }
    }

    fun w(TAG: String, method: String, msg: String) {
        if (DEBUG) {
            Log.w(APP_PREFIX + TAG, "[$method]$msg")
        }
    }

    fun w(TAG: String, msg: String) {
        if (DEBUG) {
            Log.w(APP_PREFIX + TAG, "[$fileLineMethod]$msg")
        }
    }

    // 获取文件、行数
    private val fileLineMethod: String
        private get() {
            val traceElement =
                Exception().stackTrace[2]
            val toStringBuffer = StringBuffer("[")
                .append(traceElement.fileName).append(" | ")
                .append(traceElement.lineNumber).append(" | ")
                .append(traceElement.methodName).append("]")
            return toStringBuffer.toString()
        }

    // 获取行数
    val lineMethod: String
        get() {
            val traceElement =
                Exception().stackTrace[2]
            val toStringBuffer = StringBuffer("[")
                .append(traceElement.lineNumber).append(" | ")
                .append(traceElement.methodName).append("]")
            return toStringBuffer.toString()
        }

    // 获取文件名
    fun _FILE_(): String {
        val traceElement =
            Exception().stackTrace[2]
        return traceElement.fileName
    }

    // 获取方法名
    fun _FUNC_(): String {
        val traceElement =
            Exception().stackTrace[1]
        return traceElement.methodName
    }

    // 获取行数
    fun _LINE_(): Int {
        val traceElement =
            Exception().stackTrace[1]
        return traceElement.lineNumber
    }

    // 获取时间
    @SuppressLint("SimpleDateFormat")
    fun _TIME_(): String {
        val now = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return sdf.format(now)
    }
}