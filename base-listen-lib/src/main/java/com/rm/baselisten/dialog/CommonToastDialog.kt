package com.rm.baselisten.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

/**
 * desc   :
 * date   : 2020/10/12
 * version: 1.0
 */
class CommonToastDialog :DialogFragment(){


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (dialog != null && dialog?.window != null) {
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            super.onActivityCreated(savedInstanceState)
            dialog!!.window!!.setGravity(Gravity.TOP)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setCancelable(false)

            val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
            params.gravity = Gravity.TOP
            //设置Dialog外部透明
            //设置Dialog外部透明
            params.dimAmount = 0f
            dialog!!.window!!.attributes = params
        }
    }
}