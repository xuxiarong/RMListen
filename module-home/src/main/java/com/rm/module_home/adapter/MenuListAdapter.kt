package com.rm.module_home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.business_lib.glide.loadCircleImage
import com.rm.module_home.R
import com.rm.module_home.bean.MenuItemBean

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class MenuListAdapter() :
    BaseQuickAdapter<MenuItemBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_menu) {
    override fun convert(holder: BaseViewHolder, item: MenuItemBean) {
        holder.setText(R.id.home_menu_adapter_name, item.name)
            .setText(
                R.id.home_menu_adapter_total_books_number,
                holder.itemView.context.resources.getString(
                    R.string.format_total_books,
                    item.totalNumber
                )
            )
            .setText(R.id.home_menu_adapter_collection_number,item.collectionNumber)
        loadCircleImage(holder.getView(R.id.home_menu_adapter_author_icon),item.authorIcon)
    }
}