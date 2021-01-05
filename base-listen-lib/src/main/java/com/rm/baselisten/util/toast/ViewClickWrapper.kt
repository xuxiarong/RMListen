package com.rm.baselisten.util.toast

import android.view.View

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
final class ViewClickWrapper(
    private val mToast: XToast,
    view: View,
    private val mListener: OnClickListener<View>
) :
    View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        mListener.onClick(mToast, v)
    }
}