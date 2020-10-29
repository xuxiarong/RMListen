package com.rm.business_lib.base.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

/**
 * @des:DialogFragment 底部通用
 * @data: 8/26/20 5:44 PM
 * @Version: 1.0.0
 */
open class BottomDialogFragment : RMSupportDialogFragment() {

    /*
     * 设置布局在底部
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val params = attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.dimAmount = getBackgroundAlpha()
            attributes = params
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    open fun getBackgroundAlpha(): Float {
        return 0.6f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideToUp(view)
    }


    /**
     * 从底部拉起
     *
     * @param view
     */
    private fun slideToUp(view: View) {
        val slide = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f).apply {
            duration = 120
            fillAfter = true
            isFillEnabled = true
        }
        view.startAnimation(slide)
    }

}
