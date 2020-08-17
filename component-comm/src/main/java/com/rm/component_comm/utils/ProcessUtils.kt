package com.rm.component_comm.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
/**
 * 是否为主进程
 * @param context
 * @return
 */
fun isMainProcess(context: Context): Boolean {
    return context.packageName == getCurrentProcessName(context)
}

/**
 * 获取当前进程名
 */
fun getCurrentProcessName(context: Context): String {
    val pid = Process.myPid()
    var processName = ""
    val manager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in manager.runningAppProcesses) {
        if (process.pid == pid) {
            processName = process.processName
        }
    }
    return processName
}