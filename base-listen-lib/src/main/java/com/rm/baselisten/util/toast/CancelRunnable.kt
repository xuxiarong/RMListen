package com.rm.baselisten.util.toast

import java.lang.ref.WeakReference

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
class CancelRunnable constructor(toast: XToast) : WeakReference<XToast>(toast), Runnable {

    override fun run() {
        val toast: XToast? = get()
        if (toast != null && toast.isShow()) {
            toast.cancel()
        }
    }
}