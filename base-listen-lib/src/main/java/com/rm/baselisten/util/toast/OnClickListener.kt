package com.rm.baselisten.util.toast

import android.view.View

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
interface OnClickListener<V : View> {
    /**
     * 点击回调
     */
    fun onClick(toast: XToast?, view: V?)
}