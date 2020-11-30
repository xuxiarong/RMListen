package com.rm.business_lib.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.TimeUtils

/**
 * desc   :
 * date   : 2020/10/21
 * version: 1.0
 */

@BindingAdapter("bindDate")
fun TextView.bindDate(time: Long?) {
    if (time != null) {
        bindDate(time)
    }
}

@BindingAdapter("bindDate")
fun TextView.bindDate(time: Long) {
    try {
        text = TimeUtils.getListenYearTime(time)
    } catch (e: Exception) {
        text = ""
        e.printStackTrace()
    }
}

@BindingAdapter("bindDateString")
fun TextView.bindDateString(time: String?) {
    if (time == null) {
        return
    }
    try {
        bindDate(time.toLong())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("bindDuration")
fun TextView.bindDuration(duration: Long) {
    try {
        text = TimeUtils.getPlayDuration(duration)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("bindDuration")
fun TextView.bindDuration(duration: Int?) {
    duration?.let { bindDuration(it.toLong()) }
}

@BindingAdapter("bindTimeDateLong")
fun TextView.bindTimeDateLong(timeMillis: Long? = 0L) {
    if (timeMillis == null) {
        return
    }
    text = TimeUtils.getFriendlyTimeSpanByNow(TimeUtils.millis2Date(timeMillis * 1000L))
}

@BindingAdapter("bindTimeDateString")
fun TextView.bindTimeDateString(timeMillis: String? = null) {
    if (timeMillis == null) {
        return
    }
    try {
        bindTimeDateLong(timeMillis.toLong())
    } catch (e: Exception) {
        bindTimeDateLong(0L)
        e.printStackTrace()
    }
}

//@BindingAdapter("bindDateStr")
//fun TextView.bindDateStr(time: String?) {

//}
//
//@BindingAdapter("bindDateStr")
//fun TextView.bindDateStr(time: String) {
//    try {
//        bindDate(time.toLong())
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}