package com.rm.module_search.widget

import android.content.Context
import android.os.IBinder
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author yuanfang
 * @date 11/17/20
 * @description
 *
 */
class KeyboardRecyclerView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attr, defStyleAttr) {

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                hideKeyboard(applicationWindowToken)
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}