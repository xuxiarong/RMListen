package com.rm.baselisten.binding

import android.os.SystemClock
import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.rm.baselisten.decoration.GridSpaceItemDecoration
import com.rm.baselisten.decoration.LinearItemDecoration
import com.rm.baselisten.utilExt.dip
import kotlin.math.abs


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

@BindingAdapter("bindVerticalLayout")
fun RecyclerView.bindVerticalLayout(adapter: RecyclerView.Adapter<*>?) {
    if (adapter == null) return
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    setLayoutManager(layoutManager)

    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindHorizontalLayout")
fun RecyclerView.bindHorizontalLayout(adapter: RecyclerView.Adapter<*>?) {
    if (adapter == null) return
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindHorizontalLayoutNoScroll")
fun RecyclerView.bindHorizontalLayoutNoScroll(adapter: RecyclerView.Adapter<*>?) {
    if (adapter == null) return
    val layoutManager: LinearLayoutManager = object :
        LinearLayoutManager(context, HORIZONTAL, false) {
        override fun canScrollHorizontally(): Boolean {
            return false
        }
    }
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindVerticallyLayoutNoScroll")
fun RecyclerView.bindVerticallyLayoutNoScroll(adapter: RecyclerView.Adapter<*>?) {
    if (adapter == null) return
    val layoutManager: LinearLayoutManager = object :
        LinearLayoutManager(context, HORIZONTAL, false) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindRecyclerViewLargeCache")
fun RecyclerView.bindRecyclerViewLargeCache(cacheSize: Int) {
    val pool = RecyclerView.RecycledViewPool()
    pool.setMaxRecycledViews(0, cacheSize) //TYPE为0，缓存数量为20
    setRecycledViewPool(pool) //给Recycleview设置缓存池对象
}


@BindingAdapter("bindText")
fun RecyclerView.bindText(visible: Int) {
    visibility = visible
}

@BindingAdapter("bindGridLayout", "bindCount", requireAll = false)
fun RecyclerView.bindGridLayout(adapter: RecyclerView.Adapter<*>?, spanCount: Int) {
    if (adapter == null) return
    val layoutManager = GridLayoutManager(context, spanCount)
    setLayoutManager(layoutManager)
    this.adapter = adapter
//    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("gridItemDecoration")
fun RecyclerView.gridItemDecoration(span: Float) {
    addItemDecoration(
        GridSpaceItemDecoration(
            dip(span),
            0,
            false,
            GridSpaceItemDecoration.GRIDLAYOUT
        )
    )
}

@BindingAdapter("bindFlexboxLayout", requireAll = false)
fun RecyclerView.bindFlexboxLayout(adapter: RecyclerView.Adapter<*>?) {
    if (adapter == null) {
        return
    }
    val manager = FlexboxLayoutManager(this.context)
    manager.flexDirection = FlexDirection.ROW
    manager.flexWrap = FlexWrap.WRAP
    manager.alignItems = AlignItems.STRETCH
    layoutManager = manager
    this.adapter = adapter
}

@BindingAdapter("divLinearItemDecoration", "divHeight", "divColor")
fun RecyclerView.divLinearItemDecoration(span: Int, divHeight: Int, @ColorInt divColor: Int) {
    addItemDecoration(
        LinearItemDecoration()
            .setSpanBottom(span)
            .setDivHeight(divHeight)
            .setDivColor(divColor)
    )
}

@BindingAdapter("bindRecyclerViewScroll")
fun RecyclerView.bindRecyclerViewScroll(action: ((View) -> Unit)?) {
    action?.let {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0 || dy > 0) {
                    action(recyclerView)
                }
            }
        })
    }
}


@BindingAdapter("bindVerticalScroll")
fun RecyclerView.bindVerticalScroll(y: Int? = 0) {
    if (abs(y ?: 0) > 0) {
            scrollBy(0, 1)
    }
}
