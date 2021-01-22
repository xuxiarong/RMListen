package com.rm.module_main.customview.bottomtab

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2
import com.rm.module_main.R
import com.rm.module_main.customview.bottomtab.internal.*
import com.rm.module_main.customview.bottomtab.item.BaseTabItem
import com.rm.module_main.customview.bottomtab.listener.OnTabItemSelectedListener

/**
 * 导航视图
 */
class BottomTabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var mTabPaddingTop = 0
    private var mTabPaddingBottom = 0
    private var mNavigationController: NavigationController? = null
    private var mPageChangeListener: ViewPager2.OnPageChangeCallback? = null
    private var mViewPager: ViewPager2? = null

    init {
        setPadding(0, 0, 0, 0)
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.BottomTabView
        )
        if (a.hasValue(R.styleable.BottomTabView_PaddingTop)) {
            mTabPaddingTop = a.getDimensionPixelSize(R.styleable.BottomTabView_PaddingTop, 0)
        }
        if (a.hasValue(R.styleable.BottomTabView_PaddingBottom)) {
            mTabPaddingBottom = a.getDimensionPixelSize(R.styleable.BottomTabView_PaddingBottom, 0)
        }
        a.recycle()
    }

    private val mTabItemListener: OnTabItemSelectedListener = object : OnTabItemSelectedListener {
        override fun onSelected(index: Int, old: Int) {
            if (mViewPager != null) {
                mViewPager!!.setCurrentItem(index, false)
            }
        }

        override fun onRepeat(index: Int) {}
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount
        var maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        var maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            maxWidth = maxWidth.coerceAtLeast(child.measuredWidth)
            maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
        }
        setMeasuredDimension(maxWidth, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val width = r - l
        val height = b - t
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            child.layout(0, 0, width, height)
        }
    }

    override fun getAccessibilityClassName(): CharSequence {
        return BottomTabView::class.java.name
    }


    /**
     * 构建自定义导航栏
     */
    fun custom(): CustomBuilder {
        return CustomBuilder()
    }

    /**
     * 构建 自定义 的导航栏
     */
    inner class CustomBuilder internal constructor() {
        private val items: ArrayList<BaseTabItem> = arrayListOf()

        /**
         * 完成构建
         *
         * @return [NavigationController],通过它进行后续操作
         * @throws RuntimeException 没有添加导航项时会抛出
         */
        fun build(): NavigationController {

            //未添加任何按钮
            if (items.size == 0) {
                throw RuntimeException("must add a navigation item")
            }
            val itemController: ItemController
            val customItemLayout = ItemContainerLayout(context)
            customItemLayout.initialize(items)
            customItemLayout.setPadding(0, mTabPaddingTop, 0, mTabPaddingBottom)
            removeAllViews()
            this@BottomTabView.addView(customItemLayout)
            itemController = customItemLayout
            mNavigationController = NavigationController(Controller(), itemController)
            mNavigationController!!.addTabItemSelectedListener(mTabItemListener)
            return mNavigationController!!
        }

        /**
         * 添加一个导航按钮
         *
         * @param baseTabItem [BaseTabItem],所有自定义Item都必须继承它
         * @return [CustomBuilder]
         */
        fun addItem(baseTabItem: BaseTabItem): CustomBuilder {
            items.add(baseTabItem)
            return this@CustomBuilder
        }

    }


    /**
     * 实现控制接口
     */
    private inner class Controller : BottomLayoutController {
        private var animator: ObjectAnimator? = null
        private var hide = false
        override fun setupWithViewPager(viewPager: ViewPager2) {
            mViewPager = viewPager
            if (mPageChangeListener != null) {
                mViewPager!!.unregisterOnPageChangeCallback(mPageChangeListener!!)
            } else {
                mPageChangeListener = ViewPagerPageChangeListener()
            }
            if (mNavigationController != null) {
                val n = mViewPager!!.currentItem
                if (mNavigationController!!.selected != n) {
                    mNavigationController!!.setSelect(n)
                }
                mViewPager!!.registerOnPageChangeCallback(mPageChangeListener!!)
            }
        }

        override fun hideBottomLayout() {
            if (!hide) {
                hide = true
                getAnimator()!!.start()
            }
        }

        override fun showBottomLayout() {
            if (hide) {
                hide = false
                getAnimator()!!.reverse()
            }
        }

        private fun getAnimator(): ObjectAnimator? {
            if (animator == null) {
                ObjectAnimator.ofFloat(
                    this@BottomTabView, "translationY", 0f, this@BottomTabView.height.toFloat()
                )
                animator?.duration = 300
                animator?.interpolator = AccelerateDecelerateInterpolator()
            }
            return animator
        }
    }

    private inner class ViewPagerPageChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (mNavigationController != null && mNavigationController?.selected != position) {
                mNavigationController!!.setSelect(position)
            }
        }
    }

    private val STATUS_SELECTED = "STATUS_SELECTED"
    override fun onSaveInstanceState(): Parcelable? {
        if (mNavigationController == null) {
            return super.onSaveInstanceState()
        }
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState())
        bundle.putInt(STATUS_SELECTED, mNavigationController!!.selected)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val selected = state.getInt(STATUS_SELECTED, 0)
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATUS))
            if (selected != 0 && mNavigationController != null) {
                mNavigationController!!.setSelect(selected)
            }
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val INSTANCE_STATUS = "INSTANCE_STATUS"
    }
}