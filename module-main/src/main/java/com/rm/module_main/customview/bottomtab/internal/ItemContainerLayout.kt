package com.rm.module_main.customview.bottomtab.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.rm.module_main.customview.bottomtab.ItemController
import com.rm.module_main.customview.bottomtab.item.BaseTabItem
import com.rm.module_main.customview.bottomtab.item.NormalItemView
import com.rm.module_main.customview.bottomtab.listener.OnTabItemSelectedListener
import com.rm.module_main.customview.bottomtab.listener.SimpleTabItemSelectedListener

/**
 * 存放自定义项的布局
 */
class ItemContainerLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ItemController {
    private val mItems: ArrayList<BaseTabItem> = arrayListOf()
    private val mListeners: ArrayList<OnTabItemSelectedListener> =
        arrayListOf()
    private val mSimpleListeners: MutableList<SimpleTabItemSelectedListener> =
        arrayListOf()
    override var selected = -1

    fun initialize(items: List<BaseTabItem>) {
        mItems.clear()
        mItems.addAll(items)

        //添加按钮到布局，并注册点击事件
        val n = mItems.size
        for (i in 0 until n) {
            val tabItem = mItems[i]
            tabItem.setChecked(false)
            this.addView(tabItem)
            tabItem.setOnClickListener {
                val index = mItems.indexOf(tabItem)
                if (index >= 0) {
                    setSelect(index)
                }
            }
        }

        //默认选中第一项
        selected = 0
        mItems[0].setChecked(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val n = childCount
        var visableChildCount = 0
        for (i in 0 until n) {
            if (getChildAt(i).visibility != View.GONE) {
                visableChildCount++
            }
        }
        if (visableChildCount == 0) {
            return
        }
        val childWidthMeasureSpec =
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) / visableChildCount, MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            Math.max(
                0,
                MeasureSpec.getSize(heightMeasureSpec) - paddingBottom - paddingTop
            ), MeasureSpec.EXACTLY
        )
        for (i in 0 until n) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        val count = childCount
        val width = right - left
        val height = bottom - top
        //只支持top、bottom的padding
        val padding_top = paddingTop
        val padding_bottom = paddingBottom
        var used = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                child.layout(
                    width - used - child.measuredWidth,
                    padding_top,
                    width - used,
                    height - padding_bottom
                )
            } else {
                child.layout(used, padding_top, child.measuredWidth + used, height - padding_bottom)
            }
            used += child.measuredWidth
        }
    }

    override fun getAccessibilityClassName(): CharSequence {
        return ItemContainerLayout::class.java.name
    }

    override fun setSelect(index: Int) {
        setSelect(index, true)
    }

    override fun setSelect(index: Int, needListener: Boolean) {

        //重复选择
        if (index == selected) {
            if (needListener) {
                for (listener in mListeners) {
                    mItems[selected].onRepeat()
                    listener.onRepeat(selected)
                }
            }
            //首页双次点击
            if(index == 0){
               var homeItem =  mItems[0] as NormalItemView
                homeItem.setDoubleClickListener()
            }
            return
        }

        //记录前一个选中项和当前选中项
        val oldSelected = selected
        selected = index

        //前一个选中项必须不小于0才有效
        if (oldSelected >= 0) {
            mItems[oldSelected].setChecked(false)
        }
        mItems[selected].setChecked(true)
        if (needListener) {
            //事件回调
            for (listener in mListeners) {
                listener.onSelected(selected, oldSelected)
            }
            for (listener in mSimpleListeners) {
                listener.onSelected(selected, oldSelected)
            }
        }
    }

    override fun setMessageNumber(index: Int, number: Int) {
        mItems[index].setMessageNumber(number)
    }

    override fun setHasMessage(index: Int, hasMessage: Boolean) {
        mItems[index].setHasMessage(hasMessage)
    }

    override fun addTabItemSelectedListener(listener: OnTabItemSelectedListener) {
        mListeners.add(listener)
    }

    override fun addSimpleTabItemSelectedListener(listener: SimpleTabItemSelectedListener) {
        mSimpleListeners.add(listener)
    }

    override fun setTitle(index: Int, title: String) {
        mItems[index].title = title
    }

    override fun setDefaultDrawable(index: Int, drawable: Drawable) {
        mItems[index].setDefaultDrawable(drawable)
    }

    override fun setSelectedDrawable(index: Int, drawable: Drawable) {
        mItems[index].setSelectedDrawable(drawable)
    }

    override val itemCount: Int
        get() = mItems.size

    override fun getItemTitle(index: Int): String? {
        return mItems[index].title
    }

    override fun removeItem(index: Int): Boolean {
        if (index == selected || index >= mItems.size || index < 0) {
            return false
        }
        if (selected > index) {
            selected--
        }
        removeViewAt(index)
        mItems.removeAt(index)
        return true
    }


    override fun addCustomItem(index: Int, item: BaseTabItem) {
        item.setChecked(false)
        item.setOnClickListener {
            val index = mItems.indexOf(item)
            if (index >= 0) {
                setSelect(index)
            }
        }
        if (index >= mItems.size) {
            mItems.add(index, item)
            this.addView(item)
        } else {
            mItems.add(index, item)
            this.addView(item, index)
        }
    }


    override fun addPlaceholder(index: Int) {
        context?.run {
            var placeholder = View(context).apply {
                layoutParams?.height = LayoutParams.MATCH_PARENT
                layoutParams?.width = LayoutParams.MATCH_PARENT
                isEnabled = false
            }
            if (index >= mItems.size) {
                this@ItemContainerLayout.addView(placeholder)
            } else {
                this@ItemContainerLayout.addView(placeholder, index)
            }
        }
    }

}