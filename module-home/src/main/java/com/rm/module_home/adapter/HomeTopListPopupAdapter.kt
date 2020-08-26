package com.rm.module_home.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.module_home.R

class HomeTopListPopupAdapter(data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(
    layoutResId = R.layout.home_item_top_list_popup,
    data = data
) {
    override fun convert(holder: BaseViewHolder, item: String) {
        val color = if (holder.adapterPosition == 0) {
            R.color.business_color_ff5e5e
        } else {
            R.color.business_text_color_666666
        }
        holder.setTextColor(
            R.id.home_item_popup_top_list_tv,
            ContextCompat.getColor(context, color)
        )
        holder.setText(R.id.home_item_popup_top_list_tv, item)
    }
}