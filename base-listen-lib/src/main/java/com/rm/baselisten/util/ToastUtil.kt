package com.rm.baselisten.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.rm.baselisten.R
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.baselisten.utilExt.dip

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
                mToast?.cancel()
                mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                mToast?.setText(msg)
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show(context: Context?, msg: String?, isCenter: Boolean) {
        if (isCenter) {
            showCenter(context, msg)
        } else {
            show(context, msg)
        }
    }

    @SuppressLint("InflateParams")
    private fun showCenter(context: Context?, msg: String?) {
        try {
            if (null != context && !TextUtils.isEmpty(msg)) {
                val view = LayoutInflater.from(context).inflate(R.layout.base_layout_toast, null)
                view.findViewById<AppCompatTextView>(R.id.base_toast_msg).text = msg
                mToast?.cancel()
                mToast = Toast.makeText(context, "", Toast.LENGTH_LONG)
                mToast?.view = view
                mToast?.setGravity(Gravity.CENTER, 0, context.dip(100))
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCustomToast(context: Context?,
        tipText: String = "",
        tipColor: Int = R.color.base_333,
        tipProgress: Boolean = false,
        netError: Boolean = false
    ) {
        if(context!=null && !TextUtils.isEmpty(tipText)){
            val view = LayoutInflater.from(context).inflate(R.layout.base_tip_view, null)
            view.findViewById<ProgressBar>(R.id.baseTipProgress).isVisible = tipProgress
            view.findViewById<ProgressBar>(R.id.baseNetErrorProgress).isVisible = netError
            view.findViewById<TextView>(R.id.baseTipText).text = tipText
            view.findViewById<TextView>(R.id.baseTipText).setTextColor(ContextCompat.getColor(context, tipColor))
            mToast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            mToast?.view = view
            mToast?.setGravity(Gravity.TOP, 0, context.dip(20))
            mToast?.show()
        }
    }

}