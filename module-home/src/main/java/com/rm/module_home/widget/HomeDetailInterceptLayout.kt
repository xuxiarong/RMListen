package com.rm.module_home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.util.DLog

/**
 *
 * @author yuanfang
 * @date 10/14/20
 * @description
 *
 */
class HomeDetailInterceptLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var startY = 0
    private lateinit var params: LayoutParams

    override fun onFinishInflate() {
        super.onFinishInflate()
//        val location = Array<Int>(2)
//        getLocationInWindow(location)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.rawY.toInt()
                params = this.layoutParams as LayoutParams
                intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetY = event.rawY - startY

                DLog.i("------>", "offsetY:$offsetY    height:$height")

                params.topMargin = params.topMargin + offsetY.toInt()
                params.bottomMargin = params.bottomMargin - offsetY.toInt()
                this.layoutParams = params
                startY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
            }
        }
        return intercept
    }


}