package com.rm.baselisten.util;

import android.util.Log;

import com.rm.baselisten.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author :
 * date : 2019-09-24 19:05
 * description :
 */
public class DLog {
  private static final boolean DEBUG = BuildConfig.DEBUG;
  private static final String APP_PREFIX = "";

  public static void d(String TAG, String method, String msg) {
    if (DEBUG) {
      Log.d(APP_PREFIX + TAG, "[" + method + "]" + msg);
    }
  }

  public static void d(String TAG, String msg) {
    if (DEBUG) {
      Log.d(APP_PREFIX + TAG, "[" + getFileLineMethod() + "]" + msg);
    }
  }

  public static void e(String TAG, String method, String msg) {
    if (DEBUG) {
      Log.e(APP_PREFIX + TAG, "[" + method + "]" + msg);
    }
  }

  public static void e(String TAG, String msg) {
    if (DEBUG) {
      Log.e(APP_PREFIX + TAG, "[" + getFileLineMethod() + "]" + msg);
    }
  }

  public static void i(String TAG, String method, String msg) {
    if (DEBUG) {
      Log.i(APP_PREFIX + TAG, "[" + method + "]" + msg);
    }
  }

  public static void i(String TAG, String msg) {
    if (DEBUG) {
      Log.i(APP_PREFIX + TAG, "[" + getFileLineMethod() + "]" + msg);
    }
  }

  public static void w(String TAG, String method, String msg) {
    if (DEBUG) {
      Log.w(APP_PREFIX + TAG, "[" + method + "]" + msg);
    }
  }

  public static void w(String TAG, String msg) {
    if (DEBUG) {
      Log.w(APP_PREFIX + TAG, "[" + getFileLineMethod() + "]" + msg);
    }
  }

  // 获取文件、行数
  private static String getFileLineMethod() {
    StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
    StringBuffer toStringBuffer = new StringBuffer("[")
        .append(traceElement.getFileName()).append(" | ")
        .append(traceElement.getLineNumber()).append(" | ")
        .append(traceElement.getMethodName()).append("]");
    return toStringBuffer.toString();
  }

  // 获取行数
  public static String getLineMethod() {
    StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
    StringBuffer toStringBuffer = new StringBuffer("[")
        .append(traceElement.getLineNumber()).append(" | ")
        .append(traceElement.getMethodName()).append("]");
    return toStringBuffer.toString();
  }

  // 获取文件名
  public static String _FILE_() {
    StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
    return traceElement.getFileName();
  }

  // 获取方法名
  public static String _FUNC_() {
    StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
    return traceElement.getMethodName();
  }

  // 获取行数
  public static int _LINE_() {
    StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
    return traceElement.getLineNumber();
  }

  // 获取时间
  public static String _TIME_() {
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return sdf.format(now);
  }
}
