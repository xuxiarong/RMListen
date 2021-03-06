package com.rm.baselisten.view

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.rm.baselisten.R
import com.rm.baselisten.utilExt.dip
import kotlinx.android.synthetic.main.base_tip_view.view.*

/**
 * desc   :
 * date   : 2020/10/13
 * version: 1.0
 */
class BaseTipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.base_tip_view, this)
    }

    var isAdd = false
    var isShowing = false
    private val showAnim by lazy {
        ObjectAnimator.ofFloat(this, "translationY", 0f, dip(96).toFloat()).apply {
            duration = 200
        }
    }

    private val hideAnim by lazy {
        ObjectAnimator.ofFloat(this, "translationY", dip(96).toFloat(), -20f).apply {
            duration = 200
        }
    }

    fun showNetError(activity: Activity) {
        showTipView(
            activity = activity,
            tipText = activity.getString(R.string.net_error),
            tipColor = R.color.base_ff5e5e,
            tipProgress = false,
            netError = true
        )
    }

    fun showTipView(
        activity: Activity,
        tipText: String = "",
        tipColor: Int = R.color.base_333,
        tipProgress: Boolean = false,
        netError: Boolean = false
    ) {
        if (TextUtils.isEmpty(tipText) || isShowing) {
            return
        }
        try {
            rootViewAddView(activity)
            baseTipText.text = tipText
            baseTipText.setTextColor(ContextCompat.getColor(activity, tipColor))

            if (netError) {
                setOnClickListener { activity.startActivity(Intent(Settings.ACTION_WIFI_IP_SETTINGS)) }
                baseNetErrorProgress.visibility = View.VISIBLE
            } else {
                baseNetErrorProgress.visibility = View.GONE
                setOnClickListener {}
            }

            if (tipProgress) {
                baseTipProgress.visibility = View.VISIBLE
            } else {
                baseTipProgress.visibility = View.GONE
            }
            clearAnimation()
            showAnim.start()
            isShowing = true
            handler?.removeCallbacksAndMessages(null)
            handler?.postDelayed({
                hideTipView(activity)
            }, 3000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler?.removeCallbacksAndMessages(null)
    }

    fun hideTipView(activity: Activity) {
        rootViewAddView(activity)
        clearAnimation()
        hideAnim.start()
        isShowing = false
    }


    /**
     * ???????????????
     */
    fun rootViewAddView(activity: Activity) {
        if (isAdd) {
            return
        }
        val rootView: FrameLayout = activity.findViewById(android.R.id.content)
        rootView.addView(this, tipViewLayoutParams)
        isAdd = true
    }


    protected open val tipViewLayoutParams by lazy {
        LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.TOP
            setMargins(dip(8), dip(-64), dip(8), 0)
        }
    }

}