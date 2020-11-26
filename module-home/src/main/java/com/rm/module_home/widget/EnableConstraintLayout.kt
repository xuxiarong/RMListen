package com.rm.module_home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

/**
 *
 * @author yuanfang
 * @date 11/26/20
 * @description
 *
 */
class EnableConstraintLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var isNoClick = false
    fun setIsNoClick(isNoClick: Boolean) {
        this.isNoClick = isNoClick
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isNoClick) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}

@BindingAdapter("bindIsNoClick")
fun EnableConstraintLayout.bindIsNoClick(isNoClick: Boolean?) {
    setIsNoClick(isNoClick == true)
}