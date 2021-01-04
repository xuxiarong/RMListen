package com.rm.baselisten.util.toast

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
interface OnToastListener {
    /**
     * 显示回调
     */
    fun onShow(toast: XToast?)

    /**
     * 消失回调
     */
    fun onDismiss(toast: XToast?)
}