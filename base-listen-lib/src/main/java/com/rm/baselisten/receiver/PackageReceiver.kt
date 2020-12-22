package com.rm.baselisten.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.rm.baselisten.util.DLog

/**
 *
 * @author yuanfang
 * @date 12/21/20
 * @description
 *
 */
class PackageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when {
            // 安装
            TextUtils.equals(intent?.action, "android.intent.action.PACKAGE_ADDED") -> {
                DLog.i("========安装>", "")
            }
            // 覆盖安装
            TextUtils.equals(intent?.action, "android.intent.action.PACKAGE_REPLACED") -> {
                DLog.i("========覆盖安装>", "")
            }
            // 移除
            TextUtils.equals(intent?.action, "android.intent.action.PACKAGE_REMOVED") -> {
                DLog.i("========移除>", "")
            }
        }
    }
}