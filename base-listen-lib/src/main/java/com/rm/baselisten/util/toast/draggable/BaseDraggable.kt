package com.rm.baselisten.util.toast.draggable

import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.rm.baselisten.util.toast.XToast

/**
 *
 * @author yuanfang
 * @date 12/31/20
 * @description
 *
 */
abstract class BaseDraggable : View.OnTouchListener {
    private var mToast: XToast? = null
    private var mRootView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mWindowParams: WindowManager.LayoutParams? = null

    /**
     * Toast 显示后回调这个类
     */
    fun start(toast: XToast) {
        mToast = toast
        mRootView = toast.getView()
        mWindowManager = toast.getWindowManager()
        mWindowParams = toast.getWindowParams()
        mRootView!!.setOnTouchListener(this)
    }

    protected fun getXToast(): XToast? {
        return mToast
    }

    protected fun getWindowManager(): WindowManager? {
        return mWindowManager
    }

    protected fun getWindowParams(): WindowManager.LayoutParams? {
        return mWindowParams
    }

    protected fun getRootView(): View {
        return mRootView!!
    }

    /**
     * 获取状态栏的高度
     */
    protected fun getStatusBarHeight(): Int {
        val frame = Rect()
        getRootView().getWindowVisibleDisplayFrame(frame)
        return frame.top
    }

    /**
     * 更新悬浮窗的位置
     *
     * @param x             x 坐标
     * @param y             y 坐标
     */
    protected fun updateLocation(x: Float, y: Float) {
        updateLocation(x.toInt(), y.toInt())
    }

    /**
     * 更新 WindowManager 所在的位置
     */
    protected fun updateLocation(x: Int, y: Int) {
        if (mWindowParams!!.x != x || mWindowParams!!.y != y) {
            mWindowParams!!.x = x
            mWindowParams!!.y = y
            // 一定要先设置重心位置为左上角
            mWindowParams!!.gravity = Gravity.TOP or Gravity.START
            try {
                mWindowManager!!.updateViewLayout(mRootView, mWindowParams)
            } catch (ignored: IllegalArgumentException) {
                // 当 WindowManager 已经消失时调用会发生崩溃
                // IllegalArgumentException: View not attached to window manager
            }
        }
    }

    /**
     * 判断用户是否移动了，判断标准以下：
     * 根据手指按下和抬起时的坐标进行判断，不能根据有没有 move 事件来判断
     * 因为在有些机型上面，就算用户没有手指没有移动也会产生 move 事件
     *
     * @param downX         手指按下时的 x 坐标
     * @param upX           手指抬起时的 x 坐标
     * @param downY         手指按下时的 y 坐标
     * @param upY           手指抬起时的 y 坐标
     */
    protected fun isTouchMove(downX: Float, upX: Float, downY: Float, upY: Float): Boolean {
        return downX.toInt() != upX.toInt() || downY.toInt() != upY.toInt()
    }
}