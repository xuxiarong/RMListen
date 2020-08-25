package com.rm.business_lib.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * @function 屏幕工具
 */
object ScreenUtil {
    /**
     * 获取屏幕内容高度
     * @param activity
     * @return
     */
    @JvmStatic
    fun getScreenHeight(activity: Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        var result = 0
        val resourceId = activity.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return dm.heightPixels - result
    }

    /**
     * dp转px
     * @param context
     * @param dipValue
     * @return
     */
    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}