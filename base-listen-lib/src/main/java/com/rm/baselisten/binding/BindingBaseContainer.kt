package com.rm.baselisten.binding

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindChild", requireAll = false)
fun ConstraintLayout.bindChild(@LayoutRes layoutId: Int) : View {

    if (layoutId != 0) {

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        val contentView = View.inflate(context, layoutId, null)
        addView(contentView,layoutParams)

        return contentView
    }

    return View(context)

}