package com.rm.baselisten.load.core

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.rm.baselisten.load.LoadSirUtil.isMainThread
import com.rm.baselisten.load.callback.Callback
import com.rm.baselisten.load.callback.Callback.OnReloadListener
import com.rm.baselisten.load.callback.SuccessCallback
import java.util.*

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class LoadLayout(context: Context) : FrameLayout(context) {
    private val callbacks: MutableMap<Class<out Callback>, Callback?> = HashMap()
    private var onReloadListener: OnReloadListener? = null
    private var preCallback: Class<out Callback?>? = null
    var currentCallback: Class<out Callback?>? = null
        private set

    constructor(context: Context, onReloadListener: OnReloadListener?) : this(
        context
    ) {
        this.onReloadListener = onReloadListener
    }

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.rootView
        successView?.visibility = View.GONE
        addView(successView)
        currentCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback?) {
        val cloneCallback = callback!!.copy()
        cloneCallback?.setCallback(null, context, onReloadListener)
        cloneCallback?.let { addCallback(it) }
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.containsKey(callback.javaClass)) {
            callbacks[callback.javaClass] = callback
        }
    }

    fun showCallback(callback: Class<out Callback?>?) {
        checkCallbackExist(callback)
        if (isMainThread) {
            showCallbackView(callback)
        } else {
            postToMainThread(callback)
        }
    }

    private fun postToMainThread(status: Class<out Callback?>?) {
        post { showCallbackView(status) }
    }

    private fun showCallbackView(status: Class<out Callback?>?) {
        if (preCallback != null) {
            if (preCallback == status) {
                return
            }
            callbacks[preCallback!!]!!.onDetach()
        }
        if (childCount > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX)
        }
        for (key in callbacks.keys) {
            if (key == status) {
                val successCallback =
                    callbacks[SuccessCallback::class.java] as SuccessCallback?
                if (key == SuccessCallback::class.java) {
                    successCallback!!.show()
                } else {
                    successCallback!!.showWithCallback(callbacks[key]!!.successVisible)
                    val rootView = callbacks[key]!!.rootView
                    addView(rootView)
                    callbacks[key]!!.onAttach(context, rootView)
                }
                preCallback = status
            }
        }
        currentCallback = status
    }

    fun setCallBack(
        callback: Class<out Callback?>?,
        transport: Transport?
    ) {
        if (transport == null) {
            return
        }
        checkCallbackExist(callback)
        transport.order(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: Class<out Callback?>?) {
        require(callbacks.containsKey(callback)) {
            String.format(
                "The Callback (%s) is nonexistent.", callback
                    ?.simpleName
            )
        }
    }

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }
}