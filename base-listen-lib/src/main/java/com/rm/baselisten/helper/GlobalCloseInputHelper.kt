package com.rm.baselisten.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
object GlobalCloseInputHelper {
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left
                    + v.getWidth())
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    fun hideSoftInput(token: IBinder?, context: Context) {
        if (token != null) {
            val im =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 监听 Activity的内容布局，全局关闭软键盘
     *
     *
     * 缺点：存在局限性
     *
     * @param activity
     */
    @SuppressLint("ClickableViewAccessibility")
    fun registerInputClose(activity: Activity) {
        val contentLayout = activity.window
            .decorView
            .findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
        contentLayout.getChildAt(0)
            .setOnTouchListener { _: View?, _: MotionEvent? ->
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val v = activity.window.peekDecorView()
                if (null != v) {
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
                false
            }
    }

    class ActivityDispatchTouchEvent {
        /**
         * 用于全局监控 ，点击editText之外关闭软键盘
         *
         * @param ev
         * @param activity
         */
        fun dispatchTouchEventCloseInput(ev: MotionEvent, activity: Activity) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val v = activity.currentFocus
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v!!.windowToken, activity)
                }
            }
        }
    }

    class DialogDispatchTouchEvent {
        /**
         * 用于全局监控 ，点击editText之外关闭软键盘
         *
         * @param ev
         * @param dialog
         */
        fun dispatchTouchEventCloseInput(ev: MotionEvent, dialog: Dialog) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val v = dialog.currentFocus
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v!!.windowToken, dialog.context)
                }
            }
        }
    }
}