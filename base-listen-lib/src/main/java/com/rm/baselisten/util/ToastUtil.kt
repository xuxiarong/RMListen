package com.rm.baselisten.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.rm.baselisten.R
import com.rm.baselisten.util.toast.OnClickListener
import com.rm.baselisten.util.toast.XToast
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenWidth
import com.tencent.bugly.proguard.v


/**
 * desc   :
 * date   : 2020/08/18
 * version: 1.0
 */
object ToastUtil {
    private var mToast: Toast? = null
    private var xToast: XToast? = null

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

    /* fun showCustomToast(
         context: Context?,
         tipText: String = "",
         tipColor: Int = R.color.base_333,
         tipProgress: Boolean = false,
         netError: Boolean = false
     ) {
         if (context != null && !TextUtils.isEmpty(tipText)) {
             val view = LayoutInflater.from(context).inflate(R.layout.base_tip_view, null)
             view.findViewById<ProgressBar>(R.id.baseTipProgress).isVisible = tipProgress
             view.findViewById<ProgressBar>(R.id.baseNetErrorProgress).isVisible = netError
             view.findViewById<TextView>(R.id.baseTipText).text = tipText
             view.findViewById<TextView>(R.id.baseTipText)
                 .setTextColor(ContextCompat.getColor(context, tipColor))
             mToast?.cancel()
             mToast = Toast.makeText(context, "", Toast.LENGTH_LONG)
             mToast?.view = view
             mToast?.setGravity(Gravity.TOP *//*or Gravity.FILL_HORIZONTAL*//*, 0, 0)
            mToast?.show()
        }
    }*/


    fun showTopToast(
        context: Context,
        tipText: String = "",
        tipColor: Int = R.color.base_333,
        canAutoCancel: Boolean? = true
    ) {
        if (xToast == null || context != xToast?.getContext()) {
            xToast = XToast(context)
            xToast?.setView(R.layout.base_toast_view)
        }
        xToast?.apply {
            if (canAutoCancel != true) {
                setCanAutoCancel(false)
                setVisibility(R.id.baseTipProgress, View.VISIBLE)
            } else {
                setCanAutoCancel(true)
                setVisibility(R.id.baseTipProgress, View.GONE)
            }
            setDuration(1500)
            setAnimStyle(android.R.style.Animation_Toast)
            setGravity(Gravity.TOP)
            setWidth(context.screenWidth - context.dip(20))
            setText(R.id.baseTipText, tipText)
            setTextColor(R.id.baseTipText, ContextCompat.getColor(context, tipColor))
            show()
        }
    }

    fun showTopNetErrorToast(
        context: Context
    ) {
        if (xToast == null || context != xToast?.getContext()) {
            xToast = XToast(context)
            xToast?.setView(R.layout.base_toast_net_error)
        }
        xToast?.apply {
            setDuration(1500)
            setAnimStyle(android.R.style.Animation_Toast)
            getView()?.setOnClickListener {
                context.startActivity(Intent(Settings.ACTION_WIFI_IP_SETTINGS))
            }
            setGravity(Gravity.TOP)
            setWidth(context.screenWidth - context.dip(20))
            show()
        }
    }


    fun cancelToast() {
        xToast?.cancel()
    }

}