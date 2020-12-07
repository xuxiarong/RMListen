package com.rm.baselisten.helper

import android.app.Activity
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment

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


    fun registerVisibilityListener(activity: Activity): KeyboardStatusDetector {
        return registerVisibilityListener(activity.window.decorView.findViewById<View>(android.R.id.content))
    }

    fun registerVisibilityListener(fragment: Fragment): KeyboardStatusDetector {
        return registerVisibilityListener(fragment.view)
    }


    fun registerVisibilityListener(view: View?): KeyboardStatusDetector {
        view?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                it.getWindowVisibleDisplayFrame(rect)
                val visibleHeight = rect.height()
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight
                    return@addOnGlobalLayoutListener
                }

                //软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return@addOnGlobalLayoutListener
                }

                if (rootViewVisibleHeight - visibleHeight > 200) {
                    mVisibilityListener?.onVisibilityChanged(
                        true,
                        rootViewVisibleHeight - visibleHeight
                    )
                    rootViewVisibleHeight = visibleHeight
                    return@addOnGlobalLayoutListener
                }

                if (visibleHeight - rootViewVisibleHeight > 200) {
                    mVisibilityListener?.onVisibilityChanged(false, 0)
                    rootViewVisibleHeight = visibleHeight
                    return@addOnGlobalLayoutListener
                }
            }
        }
        return this
    }

    fun setVisibilityListener(listener: KeyboardVisibilityListener): KeyboardStatusDetector {
        this.mVisibilityListener = listener
        return this
    }

    interface KeyboardVisibilityListener {
        fun onVisibilityChanged(keyboardVisible: Boolean, keyboardHeight: Int)
    }

}