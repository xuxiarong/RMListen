package com.rm.baselisten.load.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rm.baselisten.load.callback.Callback
import com.rm.baselisten.load.callback.Callback.OnReloadListener
import com.rm.baselisten.load.callback.SuccessCallback

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class LoadService<T> internal constructor(
    private val convertor: Convertor<T>?,
    targetContext: TargetContext,
    onReloadListener: OnReloadListener?,
    builder: LoadSir.Builder
) {
    private val loadLayout: LoadLayout
    private fun initCallback(builder: LoadSir.Builder) {
        val callbacks =
            builder.callbacks
        val defalutCallback =
            builder.defaultCallback
        if (callbacks.size > 0) {
            for (callback in callbacks) {
                loadLayout.setupCallback(callback)
            }
        }
        if (defalutCallback != null) {
            loadLayout.showCallback(defalutCallback)
        }
    }

    fun showSuccess() {
        loadLayout.showCallback(SuccessCallback::class.java)
    }

    fun showCallback(callback: Class<out Callback?>?) {
        loadLayout.showCallback(callback)
    }

    fun showWithConvertor(t: T) {
        requireNotNull(convertor) { "You haven't set the Convertor." }
        loadLayout.showCallback(convertor.map(t))
    }

    val currentCallback: Class<out Callback?>?
        get() = loadLayout.currentCallback

    /**
     * obtain rootView if you want keep the toolbar in Fragment
     * @since 1.2.2
     */
    @Deprecated("")
    fun getTitleLoadLayout(
        context: Context?,
        rootView: ViewGroup,
        titleView: View?
    ): LinearLayout {
        val newRootView = LinearLayout(context)
        newRootView.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newRootView.layoutParams = layoutParams
        rootView.removeView(titleView)
        newRootView.addView(titleView)
        newRootView.addView(loadLayout, layoutParams)
        return newRootView
    }

    /**
     * modify the callback dynamically
     *
     * @param callback  which callback you want modify(layout, event)
     * @param transport a interface include modify logic
     * @since 1.2.2
     */
    fun setCallBack(
        callback: Class<out Callback?>?,
        transport: Transport?
    ): LoadService<T> {
        loadLayout.setCallBack(callback, transport)
        return this
    }

    init {
        val context = targetContext.context
        val oldContent = targetContext.oldContent
        val oldLayoutParams = oldContent.layoutParams
        loadLayout = LoadLayout(context, onReloadListener)
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent, context,
                onReloadListener
            )
        )
        targetContext.parentView.addView(loadLayout, targetContext.childIndex, oldLayoutParams)
        initCallback(builder)
    }
}