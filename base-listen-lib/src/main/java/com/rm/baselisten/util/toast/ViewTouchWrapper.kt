package com.rm.baselisten.util.toast

import android.view.MotionEvent
import android.view.View

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
class ViewTouchWrapper(
    private val mToast: XToast,
    view: View,
    val mListener: OnTouchListener<View>
) : View.OnTouchListener {

    init {
        view.isFocusable = true
        view.isEnabled = true
        view.isClickable = true
        view.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mListener.onTouch(mToast, v, event)
    }
}