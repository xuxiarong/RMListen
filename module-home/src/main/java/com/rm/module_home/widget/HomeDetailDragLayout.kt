package com.rm.module_home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import com.rm.module_home.R

/**
 *
 * @author yuanfang
 * @date 10/13/20
 * @description
 *
 */
class HomeDetailDragLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var dragCl: HomeDetailInterceptLayout


    override fun onFinishInflate() {
        super.onFinishInflate()
        dragCl = findViewById(R.id.home_detail_draggable_cl)

    }



}