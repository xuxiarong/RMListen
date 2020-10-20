package com.rm.baselisten.binding

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.ConvertUtils

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindText")
fun TextView.bindText(content: Any?) {

    visibility = if (TextUtils.isEmpty(content.toString())) {
        View.GONE
    } else {
        text = content.toString()
        View.VISIBLE
    }
}

/**
 * 绑定显示默认文本
 * @receiver TextView
 * @param content String?
 * @param defaultContent String?
 */
@BindingAdapter("bindingShowText", "bindingDefaultText", "bindingShowStatus", requireAll = false)
fun TextView.bindShowText(content: String?, defaultContent: String?, showStatus: Boolean?) {
    if (content == null && defaultContent == null) return
    if (showStatus == null) {
        text = if (TextUtils.isEmpty(content.toString()) || TextUtils.equals(
                "null",
                content.toString()
            )
        ) {
            defaultContent.toString()
        } else {
            content.toString()
        }
    } else {
        text = if (showStatus) {
            if (content == null || TextUtils.equals(null, content.toString())) return
            content.toString()
        } else {
            if (defaultContent == null || TextUtils.equals(
                    "null",
                    defaultContent.toString()
                )
            ) return
            defaultContent.toString()
        }
    }
}

/**
 * 富文本
 * @param content 默认的文本
 * @param keyword 关键字
 * @param spanColor 富文本字体颜色
 */
@BindingAdapter("bindSpanContent", "bindSpanKeyword", "bindSpanColor", requireAll = false)
fun TextView.bindSpanText(content: String?, keyword: String?, @ColorInt spanColor: Int) {
    if (content.isNullOrEmpty()) {
        return
    }
    if (!keyword.isNullOrEmpty()) {
        var a = content.indexOf(keyword)
        val keyList = mutableListOf<IntArray>()

        while (a != -1) {
            keyList.add(intArrayOf(a, a + keyword.length))
            a = content.indexOf(keyword, a + keyword.length)
        }
        val span = SpannableString(content)
        for (ints in keyList) {
            span.setSpan(
                ForegroundColorSpan(spanColor),
                ints[0],
                ints[1],
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
        text = span
    } else {
        text = content
    }
}

@BindingAdapter("bindMemoryText")
fun TextView.bindMemoryText(size : Long){
    text = ConvertUtils.byte2FitMemorySize(size,1)
}

