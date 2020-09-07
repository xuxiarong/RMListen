package com.rm.baselisten.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
/**
 * BindingAdapter注解里面的属性名字，是可以直接用在xml中的，方法定义声明的View.bindClick
 * 代表了是对View的功能扩展，接受的参数是一个代码块，也就是方法的引用
 * @receiver View 扩展对象为View
 * @param action Function0<Unit>? 方法体(或者叫代码块)
 */
@BindingAdapter("bindClick")
fun View.bindClick(action: (() -> Unit)?) {
    if (action != null) {
        setOnClickListener { action() }
    }
}

@BindingAdapter("bindLongClick")
fun View.bindLongClick(action: (() -> Unit)?) {
    if (action != null) {
        setOnLongClickListener {
            action()
            true
        }
    }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("bindBackground")
fun View.bindBackground(drawableId: Int?) {
    if (drawableId != null && drawableId > 0) {
        setBackgroundResource(drawableId)
    }
}



