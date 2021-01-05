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
interface OnTouchListener<V : View> {
    /**
     * 触摸回调
     */
    fun onTouch(toast: XToast?, view: V?, event: MotionEvent?): Boolean
}