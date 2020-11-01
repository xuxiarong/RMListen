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
fun TextView.bindDateString(time: String) {
    try{
        bindDate(time.toLong())
    }catch (e : Exception){
        e.printStackTrace()
    }
}

@BindingAdapter("bindDuration")
fun TextView.bindDuration(duration: Long) {
    try{
        text = TimeUtils.getListenDuration(duration)
    }catch (e : Exception){
        e.printStackTrace()
    }
}
@BindingAdapter("bindDuration")
fun TextView.bindDuration(duration: Int?) {
    duration?.let { bindDuration(it.toLong()) }
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