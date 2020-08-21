package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

@BindingAdapter("bindAdapter")
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<*>) {
    setAdapter(adapter)
}

@BindingAdapter("layoutVertical")
fun RecyclerView.layoutVertical(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
}

@BindingAdapter("layoutHorizontal")
fun RecyclerView.layoutHorizontal(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    setLayoutManager(layoutManager)
    itemAnimator = DefaultItemAnimator()
}