package com.rm.baselisten.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.decoration.GridSpaceItemDecoration
import com.rm.baselisten.util.DisplayUtils

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

//@BindingAdapter(
//    "itemTopPadding",
//    "itemLeftPadding",
//    "itemBottomPadding",
//    "itemRightPadding",
//    requireAll = false
//)
//fun RecyclerView.addItemPadding(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0) {
//    addItemDecoration(DefaultItemAnimator(top, left, bottom, right))
//}

@BindingAdapter("bindVerticalLayout")
fun RecyclerView.bindVerticalLayout(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    setLayoutManager(layoutManager)

    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindHorizontalLayout")
fun RecyclerView.bindHorizontalLayout(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("bindText")
fun RecyclerView.bindText(visible: Int) {
    visibility = visible
}

@BindingAdapter("bindGridLayout", "bindCount", requireAll = false)
fun RecyclerView.bindGridLayout(adapter: RecyclerView.Adapter<*>, spanCount: Int) {
    val layoutManager = GridLayoutManager(context, spanCount)
    setLayoutManager(layoutManager)
    this.adapter = adapter
//    overScrollMode = View.OVER_SCROLL_NEVER
}

@BindingAdapter("gridItemDecoration")
fun RecyclerView.gridItemDecoration(span: Float) {
    addItemDecoration(
        GridSpaceItemDecoration(
            DisplayUtils.dip2px(context, span),
            0,
            false,
            GridSpaceItemDecoration.GRIDLAYOUT
        )
    )
}