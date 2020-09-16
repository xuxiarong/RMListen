package com.rm.module_play.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by shenyu_zxjCode on 2017/9/25 0025.
 */
/**
 * 默认分割线：高度为2px，颜色为灰色
 *
 * @param context
 * @param orientation 列表方向
 */
class RecycleViewDivider @JvmOverloads constructor(
    val context: Context,
    private val mOrientation: Int
) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint? = null
    private var mDivider: Drawable? = null
    private var mDividerHeight = 2//分割线高度，默认为1px

    init {
        if (mOrientation != LinearLayoutManager.VERTICAL && mOrientation != LinearLayoutManager.HORIZONTAL) {
            throw IllegalArgumentException("请输入正确的参数！")
        }

        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 自定义分割线
     */
    fun setDrawableId(drawableId: Int) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider?.intrinsicHeight ?: 0
    }

    /**
     * 自定义分割线高度
     */
    fun setDividerHeight(dividerHeight: Int) {
        mDividerHeight = dividerHeight
    }

    /**
     * 自定义分割线颜色
     */
    fun setDividerColor(dividerColor: Int) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = dividerColor
        mPaint?.style = Paint.Style.FILL
    }

    var positionDivider = -1

    /**
     * 指定绘制分割线
     */
    fun setPosDraw(position: Int) {
        positionDivider = position
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (positionDivider > -1) {
                drawVerticalPosition(c, parent, positionDivider)
            } else {
                drawVertical(c, parent)
            }
        } else {
            if (positionDivider > -1) {
                drawHorizontalPositon(c, parent, positionDivider)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, mDividerHeight)
    }


    //绘制横向 item 分割线
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    it
                )
            }
        }
    }

    //绘制纵向 item 分割线
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    it
                )
            }
        }
    }

    private fun drawVerticalPosition(canvas: Canvas, parent: RecyclerView, positon: Int) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        if (childSize == positon) {
            val child = parent.getChildAt(childSize)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    it
                )
            }
        }
    }

    //绘制横向 item 分割线
    private fun drawHorizontalPositon(canvas: Canvas, parent: RecyclerView, positon: Int) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        if (childSize == positon) {
            val child = parent.getChildAt(childSize)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
            mPaint?.let {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    it
                )
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}