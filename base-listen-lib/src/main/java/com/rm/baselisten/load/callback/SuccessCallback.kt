package com.rm.baselisten.load.callback

import android.content.Context
import android.view.View

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
class SuccessCallback(
    view: View?,
    context: Context?,
    onReloadListener: OnReloadListener?
) : Callback(view, context, onReloadListener) {
    override fun onCreateView(): Int {
        return 0
    }

    @Deprecated("Use {@link #showWithCallback(boolean successVisible)} instead.")
    fun hide() {
        obtainRootView()!!.visibility = View.INVISIBLE
    }

    fun show() {
        obtainRootView()!!.visibility = View.VISIBLE
    }

    fun showWithCallback(successVisible: Boolean) {
        obtainRootView()!!.visibility = if (successVisible) View.VISIBLE else View.INVISIBLE
    }
}