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
    private var mDownY = 0
    private var interceptLayout: HomeDetailInterceptLayout? = null
    private var params: ConstraintLayout.LayoutParams? = null


    /*override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (interceptLayout == null) {
            interceptLayout = parent.parent as HomeDetailInterceptLayout
            params = interceptLayout!!.layoutParams as ConstraintLayout.LayoutParams
        }
        //recyclerView是否滑动到顶部
        val isScrollTop = !canScrollVertically(-1)
        var intercept = true
        when (ev.action) {

            MotionEvent.ACTION_MOVE -> {
                //判断滑动的方向是否是往上滑动
                val offsetY = ev.y - mDownY
                DLog.i("-------->","${ev.y}    $mDownY   $intercept")
                mDownY = ev.y.toInt()


                if (offsetY > 0 && isScrollTop) {
//                    params!!.height = params!!.height - offsetY.toInt()
                    params!!.topMargin = params!!.topMargin + offsetY.toInt()
                    params!!.bottomMargin = params!!.bottomMargin - offsetY.toInt()
                    interceptLayout!!.layoutParams = params
                    intercept = false
                }
            }
            MotionEvent.ACTION_UP->{
                intercept=true
            }
        }

        mDownY = ev.y.toInt()
        return intercept
//        return super.onTouchEvent(ev)
    }*/
}