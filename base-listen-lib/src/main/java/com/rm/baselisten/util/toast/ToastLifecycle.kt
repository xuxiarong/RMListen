package com.rm.baselisten.util.toast

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.OnLifecycleEvent
import com.rm.baselisten.util.DLog

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
class ToastLifecycle(toast: XToast?, private val lifecycleRegistry: LifecycleRegistry) :
    LifecycleObserver {

    private var mToast: XToast? = null

    init {
        mToast = toast
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        DLog.i("xToast onCreate: ", "==")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        // 一定要在 onPaused 方法中销毁掉，如果放在 onDestroyed 方法中还是有一定几率会导致内存泄露
        if (mToast != null && mToast!!.isShow()) {
            mToast!!.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        DLog.i("xToast onActivityDestroyed: ", "==")
        mToast?.apply {
            if (isShow()) {
                cancel()
            }
            recycle()
            mToast = null
        }
        lifecycleRegistry.removeObserver(this)
    }
}