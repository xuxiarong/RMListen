package com.rm.module_home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog

/**
 *
 * @author yuanfang
 * @date 10/14/20
 * @description
 *
 */
class HomeDetailRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    override fun onTouchEvent(e: MotionEvent?): Boolean {

        return super.onTouchEvent(e)
    }



}