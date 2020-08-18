package com.rm.baselisten.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

/**
 * desc   :
 * date   : 2020/08/18
 * version: 1.0
 */
object ToastUtil {
    private var mToast: Toast? = null
    fun show(context: Context?, msg: String?) {
        try {
            if (null != context && !TextUtils.isEmpty(msg)) {
                if (null != mToast) {
                    mToast!!.cancel()
                }
                mToast = Toast.makeText(context, "", Toast.LENGTH_LONG)
                mToast?.setText(msg)
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}