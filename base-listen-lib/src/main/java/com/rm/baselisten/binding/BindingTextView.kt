package com.rm.baselisten.binding

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

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
@BindingAdapter("bindingShowText", "bindingDefaultText", "bindingShowStatus",requireAll = false)
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

