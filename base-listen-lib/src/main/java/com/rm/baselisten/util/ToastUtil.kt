package com.rm.baselisten.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

/**
 * 应用模块: utils
 *
 *
 * 类描述: toast显示工具类
 *
 *
 *
 * @author darryrzhoong
 * @since 2020-01-27
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