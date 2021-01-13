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

    fun show(context: Context?, msg: String?) {
        try {
            if (null != context && !TextUtils.isEmpty(msg)) {
                mToast?.cancel()
                mToast = Toast.makeText(context.applicationContext, "", Toast.LENGTH_SHORT)
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
                val view = LayoutInflater.from(context.applicationContext)
                    .inflate(R.layout.base_layout_toast, null)
                view.findViewById<AppCompatTextView>(R.id.base_toast_msg).text = msg
                mToast?.cancel()
                mToast = Toast.makeText(context.applicationContext, "", Toast.LENGTH_LONG)
                mToast?.view = view
                mToast?.setGravity(Gravity.CENTER, 0, context.dip(100))
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param context 上下文对象
     * @param tipText 提示信息
     * @param tipColor 文本颜色
     * @param canAutoCancel 是否能自动消失文本
     * @param lifecycleOwner 当前的生命周期，最好加上不然会造成内存泄露。
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
                    setAnimStyle(android.R.style.Animation_Toast)
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
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    fun cancelToast() {
        xToast?.get()?.cancel()
    }

}