package com.rm.baselisten.helper

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry

/**
 *
 * @author yuanfang
 * @date 10/12/20
 * @description 监听键盘的显示/隐藏
 *
 */
class KeyboardStatusDetector {
    //纪录根视图的显示高度
    private var rootViewVisibleHeight = 0

    //接口回调
    private var mVisibilityListener: KeyboardVisibilityListener? = null

    private var mView: View? = null

    fun registerVisibilityListener(activity: AppCompatActivity): KeyboardStatusDetector {
        val view = activity.window.decorView.findViewById<View>(android.R.id.content)
        view?.let {
            mView = it
            it.viewTreeObserver.addOnGlobalLayoutListener(windowListener)
            val lifecycleRegistry = LifecycleRegistry(activity)
            lifecycleRegistry.addObserver(KeyboardLifecycle(this))
        }
        return this
    }

    fun registerVisibilityListener(fragment: Fragment): KeyboardStatusDetector {
        val view = fragment.view
        view?.let {
            mView = it
            it.viewTreeObserver.addOnGlobalLayoutListener(windowListener)
            val lifecycleRegistry = LifecycleRegistry(fragment)
            lifecycleRegistry.addObserver(KeyboardLifecycle(this))
        }
        return this
    }


    fun registerVisibilityListener(view: View?): KeyboardStatusDetector {
        view?.let {
            mView = it
            it.viewTreeObserver.addOnGlobalLayoutListener(windowListener)
        }
        return this
    }

    /**
     * 动态获取输入法的高度，并且给联想的RV设置bottomMargin，修复联想被挡住的bug
     */
    private val windowListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        mView!!.getWindowVisibleDisplayFrame(rect)
        val visibleHeight = rect.height()
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }

        //软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return@OnGlobalLayoutListener
        }

        if (rootViewVisibleHeight - visibleHeight > 200) {
            mVisibilityListener?.onVisibilityChanged(
                true,
                rootViewVisibleHeight - visibleHeight
            )
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }

        if (visibleHeight - rootViewVisibleHeight > 200) {
            mVisibilityListener?.onVisibilityChanged(false, 0)
            rootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
    }

    fun setVisibilityListener(listener: KeyboardVisibilityListener): KeyboardStatusDetector {
        this.mVisibilityListener = listener
        return this
    }

    fun removeListener() {
        mVisibilityListener = null
        mView?.viewTreeObserver?.removeOnGlobalLayoutListener(windowListener)
    }

    interface KeyboardVisibilityListener {
        fun onVisibilityChanged(keyboardVisible: Boolean, keyboardHeight: Int)
    }

}