package com.rm.module_search.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.module_search.R

/**
 *
 * @author yuanfang
 * @date 10/19/20
 * @description
 *
 */
class TagLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttr) {
    private var lineHeights = mutableListOf<Int>()
    private var views = mutableListOf<MutableList<View>>()
    private var maxLine = 2
    private var itemClick: (View, String) -> Unit = { _, _ -> }
    private var maxHeight = 0

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        views.clear()
        lineHeights.clear()
        //1.计算
        //该行有多少列数据
        var lineViews = mutableListOf<View>()
        val width = measuredWidth //容器自己的宽度
        var lineWidth = 0
        var lineHeight = 0 //这一行的最大高度
        val childCount = childCount
        for (j in 0 until childCount) {
            val child = getChildAt(j)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                //超出,换行
                lineHeights.add(lineHeight)
                views.add(lineViews)
                lineWidth = 0
                lineHeight = 0
                lineViews = ArrayList()
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = lineHeight.coerceAtLeast(childHeight + lp.topMargin + lp.bottomMargin)
            lineViews.add(child)
        }
        lineHeights.add(lineHeight)
        views.add(lineViews)
        var left = 0
        var top = 0
        //2.摆放
        val size = views.size
        for (i in 0 until size) {

            lineViews = views[i]
            lineHeight = lineHeights[i]
            for (j in lineViews.indices) {
                if (i < maxLine) {
                    //遍历这一行的所有child
                    val child: View = lineViews[j]
                    val lp = child.layoutParams as MarginLayoutParams
                    val lc = left + lp.leftMargin
                    val tc = top + lp.topMargin
                    val rc = lc + child.measuredWidth
                    val bc = tc + child.measuredHeight
                    child.layout(lc, tc, rc, bc)
                    left += child.measuredWidth + lp.leftMargin + lp.rightMargin
                } else {
                    removeView(lineViews[j])
                }
            }
            left = 0
            top += lineHeight
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        var width = 0 //width=所有行里面最宽的一行
        var height = 0 //height=所有行的高度相加
        //一行的宽度=一行当中的所有view的宽度的和
        var lineWidth = 0
        var lineHeight = 0

        //1.测量所有子控件的大小
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val lp = child.layoutParams as MarginLayoutParams
            //子控件真实占用的宽和高度
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (maxHeight <= 0) {
                maxHeight = childHeight * maxLine
            }
            //当一行放不下的时候需要换行
            if (lineWidth + childWidth > sizeWidth) {
                //换行
                width = lineWidth.coerceAtLeast(width)
                lineWidth = childWidth
                height += lineHeight
                lineHeight = childHeight
            } else { //累加
                lineWidth += childWidth
                lineHeight = lineHeight.coerceAtLeast(childHeight)
            }
            //最后一步
            if (i == childCount - 1) {
                width = width.coerceAtLeast(lineWidth)
                height += lineHeight
            }
        }
        //2.测量并定义自身的大小
        val measuredWidth = if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width
        var measuredHeight = if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height
        if (measuredHeight > maxHeight) {
            measuredHeight = maxHeight
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    fun setData(list: MutableList<String>) {
        removeAllViews()
        list.forEach {
            val childView = createView(it)
            addView(childView)
        }
    }

    fun setMaxLine(maxLine: Int) {
        this.maxLine = maxLine
    }

    fun setItemClick(clickBlock: (View, String) -> Unit) {
        itemClick = clickBlock
    }

    private fun createView(content: String): View {
        val view = LayoutInflater.from(context).inflate(
            R.layout.search_adapter_history,
            this,
            false
        ) as TextView
        if (content.length > 10) {
            view.text = "${content.substring(0, 9)}..."
        } else {
            view.text = content
        }
        view.setOnClickListener {
            itemClick(it, content)
        }
        return view
    }
}

@BindingAdapter("bindTagMaxLine", "bindTagData", "bindTagClick", requireAll = false)
fun TagLayout.bindData(
    bindTagMaxLine: Int = 2,
    bindTagData: MutableList<String>?,
    blockClick: ((View, String) -> Unit)?
) {
    if (bindTagData == null) {
        visibility = View.GONE
        return
    }

    if (bindTagMaxLine >= 0) {
        setMaxLine(bindTagMaxLine)
    }

    setData(bindTagData)

    if (blockClick != null) {
        setItemClick { view, content -> blockClick(view, content) }
    }
}