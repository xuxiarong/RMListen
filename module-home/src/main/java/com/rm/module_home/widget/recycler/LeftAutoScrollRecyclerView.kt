package com.rm.module_home.widget.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.R
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel
import me.jessyan.autosize.utils.ScreenUtils
import kotlin.math.abs

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class LeftAutoScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    val defaultWidth = dip(56)

    var downX = 0F
    var downY = 0F

    var controlX = 0F
    /**
     * 第一个的位置
     */
    /**
     * 最后一个可见的item的位置
     */
    var lastVisibleItemPosition = 0

    /**
     * 最后一个的位置
     */

    private var footerView: View? = null


    private val paint: Paint by lazy {
        Paint()
    }

    private val path: Path by lazy {
        Path()
    }

    private val screenWidth by lazy {
        ScreenUtils.getScreenSize(context)[0].toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        footerView?.let {
            if (controlX != 0F) {
                path.reset()
                paint.color = ContextCompat.getColor(context, R.color.business_color_fff7f7f7)
                paint.isAntiAlias = true
                paint.style = Paint.Style.FILL
                path.moveTo(screenWidth, 0F)
                if (controlX < screenWidth - defaultWidth * 2) {
                    controlX = screenWidth - defaultWidth * 2
                }
                path.quadTo(controlX, dip(88).toFloat(), screenWidth, dip(176).toFloat())
                canvas?.save()
                canvas?.drawPath(path, paint)
                canvas?.restore()
            } else {
                path.reset()
                canvas?.drawPath(path, paint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val layoutManager = layoutManager
        if (layoutManager != null) {
            if (layoutManager is GridLayoutManager) {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is LinearLayoutManager) {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            }
        } else {
            throw RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager")
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                return super.onTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                if (downX == -1F && downY == -1F) {
                    downX = ev.x
                    downY = ev.y
                    return super.onTouchEvent(ev)
                }

                val dx: Float = ev.x - downX
                val dy : Float = ev.y -downY

                downX = ev.x
                downY = ev.y
                val totalItemCount = layoutManager.itemCount

                if(lastVisibleItemPosition >= totalItemCount - 1 && abs(dx) > abs(dy)){
                    parent.requestDisallowInterceptTouchEvent(true)
                    if (footerView == null) {
                        footerView = getChildAt(childCount - 1)
                    }
                    footerView?.let {
                        //往左滑动


                        if (it.left <= screenWidth - defaultWidth + 10) {
                            if(dx>0 && controlX ==0F){
                                return super.onTouchEvent(ev)
                            }
                            if (controlX == 0F) {
                                controlX = it.right.toFloat()
                            }
                            controlX += dx
                            invalidate()
//                            DLog.d("suolong_left", "controlX = $controlX    measuredWidth = ${it.measuredWidth}  it.left = $it.left --- it.right = ${it.right}")
                            return true
                        } else {
//                            DLog.d("suolong_left", "it.left <= screenWidth - it.measuredWidth it.left = ${it.left}")
                            super.onTouchEvent(ev)
                        }
                    }
                }else{
//                    DLog.d("suolong_left", "lastVisibleItemPosition < childCount - 1")
                    super.onTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                checkNeedOpen()
                controlX = 0F
                downX = -1F
                downY = -1F
                super.onTouchEvent(ev)
//                DLog.d("suolong_left", "ACTION_UP")
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {

                controlX = 0F
                downX = -1F
                downY = -1F
                super.onTouchEvent(ev)
//                DLog.d("suolong_left", "ACTION_CANCEL")
                invalidate()
            }
        }
        return true
    }

    var blockModel: HomeAudioHorDoubleRvModel? = null

    private fun checkNeedOpen(){
        if(controlX<=screenWidth - defaultWidth * 2 + 20 && controlX >0){
            blockModel?.let{
                HomeTopicListActivity.Companion.startTopicActivity(
                    context,
                    it.block.topic_id,
                    it.block.block_name
                )
            }
        }
    }

}