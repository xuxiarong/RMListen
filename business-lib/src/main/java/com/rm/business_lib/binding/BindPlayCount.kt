package com.rm.business_lib.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/10/14
 * version: 1.0
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("bindPlayCount")
fun TextView.bindPlayCount(count: Int? = 0) {
    text = getPlayCount(count)
}

@BindingAdapter("bindPlayCount", "bindPlayCountUnit", requireAll = false)
fun TextView.bindPlayCount(count: Int, unit: String? = null) {
    val bf = StringBuffer(getPlayCount(count))
    if (unit != null) {
        bf.append(unit)
    }
    text = bf
}

/**
 * 可设置前后缀
 */
@BindingAdapter("bindPlayCount", "bindPlayCountPrefix", "bindPlayCountSuffix", requireAll = false)
fun TextView.bindPlayCount(
    count: Int,
    bindPlayCountPrefix: String? = null,
    bindPlayCountSuffix: String? = null
) {
    val prefix = bindPlayCountPrefix ?: ""
    val suffix = bindPlayCountSuffix ?: ""
    val bf = StringBuffer(prefix)
    val countStr = getPlayCount(count)
    bf.append(countStr)
    bf.append(suffix)
    text = bf
}

@BindingAdapter("bindPlayCountString", "bindPlayCountUnit", requireAll = false)
fun TextView.bindPlayCountString(count: String?, unit: String? = null) {
    try {
        if (count != null) {
            bindPlayCount(count.toInt(), unit)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun getPlayCount(count: Int?): String {
    val i = count ?: 0
    return when {
        i < 10000 -> {
            i.toString()
        }
        i > 99999999 -> {
            "9999w+"
        }
        else -> {
            val wan = i / 10000
            val qian = i % 10000 / 1000
            val bai = i % 1000 / 100
            "$wan" + "." + "$qian" + "w"
        }
    }

}
