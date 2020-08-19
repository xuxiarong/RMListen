package com.rm.module_main.customview.bottomtab.internal

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * A simple ViewGroup that aligns all the views inside on a baseline. Note: bottom padding for this
 * view will be measured starting from the baseline.
 */
class BaselineLayout : ViewGroup {
    private var mBaseline = -1

    constructor(context: Context?) : super(context, null, 0) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0) {}
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount
        var maxWidth = 0
        var maxHeight = 0
        var maxChildBaseline = -1
        var maxChildDescent = -1
        var childState = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val baseline = child.baseline
            if (baseline != -1) {
                maxChildBaseline = Math.max(maxChildBaseline, baseline)
                maxChildDescent = Math.max(maxChildDescent, child.measuredHeight - baseline)
            }
            maxWidth = Math.max(maxWidth, child.measuredWidth)
            maxHeight = Math.max(maxHeight, child.measuredHeight)
            childState = childState or child.measuredState
        }
        if (maxChildBaseline != -1) {
            maxChildDescent = Math.max(maxChildDescent, paddingBottom)
            maxHeight = Math.max(maxHeight, maxChildBaseline + maxChildDescent)
            mBaseline = maxChildBaseline
        }
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)
        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val count = childCount
        val parentLeft = paddingLeft
        val parentRight = right - left - paddingRight
        val parentContentWidth = parentRight - parentLeft
        val parentTop = paddingTop
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            val width = child.measuredWidth
            val height = child.measuredHeight
            val childLeft = parentLeft + (parentContentWidth - width) / 2
            val childTop: Int
            childTop = if (mBaseline != -1 && child.baseline != -1) {
                parentTop + mBaseline - child.baseline
            } else {
                parentTop
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height)
        }
    }

    override fun getBaseline(): Int {
        return mBaseline
    }
}