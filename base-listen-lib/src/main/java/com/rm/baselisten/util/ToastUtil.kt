package com.rm.baselisten.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.R
import com.rm.baselisten.util.toast.XToast
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenWidth
import java.lang.ref.WeakReference


/**
 * desc   :
 * date   : 2020/08/18
 * version: 1.0
 */
object ToastUtil {
    private var mToast: Toast? = null
    private var xToast: WeakReference<XToast>? = null

    fun show(msg: String?) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                mToast?.cancel()
                mToast = Toast.makeText(BaseApplication.CONTEXT, "", Toast.LENGTH_SHORT)
                mToast?.setText(msg)
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show(msg: String?, isCenter: Boolean) {
        if (isCenter) {
            showCenter(msg)
        } else {
            show(msg)
        }
    }

    @SuppressLint("InflateParams")
    private fun showCenter( msg: String?) {
        try {
            if ( !TextUtils.isEmpty(msg)) {
                val view = LayoutInflater.from(BaseApplication.CONTEXT)
                    .inflate(R.layout.base_layout_toast, null)
                view.findViewById<AppCompatTextView>(R.id.base_toast_msg).text = msg
                mToast?.cancel()
                mToast = Toast.makeText(BaseApplication.CONTEXT, "", Toast.LENGTH_LONG)
                mToast?.view = view
                mToast?.setGravity(Gravity.CENTER, 0, BaseApplication.CONTEXT.dip(100))
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param context ???????????????
     * @param tipText ????????????
     * @param tipColor ????????????
     * @param canAutoCancel ???????????????????????????
     * @param lifecycleOwner ??????????????????????????????????????????????????????????????????
     */
    fun showTopToast(
        context: Context,
        tipText: String = "",
        tipColor: Int = R.color.base_333,
        canAutoCancel: Boolean? = true,
        lifecycleOwner: LifecycleOwner? = null
    ) {
        if (tipText.isNotEmpty()) {
            try {
                if (xToast == null || context != xToast?.get()?.getContext()) {
                    xToast = WeakReference(XToast(context))
                    xToast?.get()?.setView(R.layout.base_toast_view)
                }
                xToast?.get()?.apply {
                    if (canAutoCancel != true) {
                        setCanAutoCancel(false)
                        setVisibility(R.id.baseTipProgress, View.VISIBLE)
                    } else {
                        setCanAutoCancel(true)
                        setVisibility(R.id.baseTipProgress, View.GONE)
                    }
                    setDuration(1500)
                    setAnimStyle(R.style.TopToBottomAnim)
                    setGravity(Gravity.TOP)
                    setWidth(context.screenWidth - context.dip(20))
                    setText(R.id.baseTipText, tipText)
                    setTextColor(R.id.baseTipText, ContextCompat.getColor(context, tipColor))
                    show(lifecycleOwner)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            return
        }
    }

    fun showTopNetErrorToast(
        context: Context,
        lifecycleOwner: LifecycleOwner? = null
    ) {
        try {
            if (xToast == null || context != xToast?.get()?.getContext()) {
                xToast = WeakReference(XToast(context))
                xToast?.get()?.setView(R.layout.base_toast_net_error)
            }
            xToast?.get()?.apply {
                setDuration(1500)
                setAnimStyle(android.R.style.Animation_Toast)
                getView()?.setOnClickListener {
                    context.startActivity(Intent(Settings.ACTION_WIFI_IP_SETTINGS))
                }
                setGravity(Gravity.TOP)
                setWidth(context.screenWidth - context.dip(20))
                show(lifecycleOwner)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun cancelToast() {
        xToast?.get()?.cancel()
    }

}