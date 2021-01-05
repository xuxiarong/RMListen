package com.rm.baselisten.util.toast

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Build
import android.os.Bundle

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
class ToastLifecycle constructor(toast: XToast?, activity: Activity?) :
    ActivityLifecycleCallbacks {

    private var mActivity: Activity? = null
    private var mToast: XToast? = null

    init {
        mActivity = activity
        mToast = toast
    }

    /**
     * 注册监听
     */
    fun register() {
        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mActivity!!.registerActivityLifecycleCallbacks(this)
            } else {
                mActivity!!.application.registerActivityLifecycleCallbacks(this)
            }
        }
    }

    /**
     * 取消监听
     */
    fun unregister() {
        if (mActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mActivity!!.unregisterActivityLifecycleCallbacks(this)
            } else {
                mActivity!!.application.unregisterActivityLifecycleCallbacks(this)
            }
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityPaused(activity: Activity) {
        // 一定要在 onPaused 方法中销毁掉，如果放在 onDestroyed 方法中还是有一定几率会导致内存泄露
        if (mActivity != null && mToast != null && mActivity === activity && mToast!!.isShow() && mActivity!!.isFinishing) {
            mToast!!.cancel()
        }
    }

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (mActivity === activity) {
            // 释放 Activity 的引用
            mActivity = null
            if (mToast != null) {
                if (mToast!!.isShow()) {
                    mToast!!.cancel()
                }
                mToast!!.recycle()
                mToast = null
            }
        }
    }
}