package com.rm.baselisten.util

import android.view.View

/**
 *
 * @des:防止双击
 * @data: 8/17/20 6:43 PM
 * @Version: 1.0.0
 */

/** 记录最后一次触发的时间 **/
private val lastTimeMap: LinkedHashMap<String, Long> = linkedMapOf()

/**
 * 判断是否连续双击
 */
fun isOnDoubleClick(tag: String = "default"): Boolean {
    return isOnDoubleClick(1500, tag)
}

/**
 * 防止双击
 */
fun isNotOnDoubleClick(duration: Long = 1500, tag: String = "default", isNotDoubleClick: () -> Unit = {}) {
    if (!isOnDoubleClick(duration, tag)) isNotDoubleClick()
}

/**
 * View扩展方法防止双击
 */
fun View.setOnClickNotDoubleListener(duration: Long = 1500, tag: String = "default", isNotDoubleClick: (View) -> Unit = {}) {
    setOnClickListener { if (!isOnDoubleClick(duration, tag)) isNotDoubleClick(it) }
}

/**
 * View扩展方法防止双击
 */
fun View.setPlayOnClickNotDoubleListener(duration: Long = 500, tag: String = "default", isNotDoubleClick: (View) -> Unit = {}) {
    setOnClickListener { if (!isOnDoubleClick(duration, tag)) isNotDoubleClick(it) }
}

/**
 * 移除双击事件
 */
fun clearDoubleCheck(tag: String = "default") = lastTimeMap.remove(tag)

/**
 * 判断是否是连续事件
 * @param duration 一定时间内
 * @param tag 分组，当前需要哪一组的记录
 */
fun isOnDoubleClick(duration: Long, tag: String = "default"): Boolean {
    val sysTime = System.currentTimeMillis()
    val lastTime = lastTimeMap.getOrElse(tag) { 0 }
    return if (sysTime >= lastTime + duration) {
        /** 不是连续双击，这时最后时间切换成记录当前时间 **/
        lastTimeMap[tag] = sysTime
        false
    } else {
        /** 是连续双击的话，一直过滤这段时间内的所有事件，直到超过这段时间才重新开始 **/
        true
    }
}
