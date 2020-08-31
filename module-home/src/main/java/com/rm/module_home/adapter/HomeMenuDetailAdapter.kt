package com.rm.module_home.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.R
import com.rm.module_home.bean.MenuItemBean

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class HomeMenuDetailAdapter :
    BaseQuickAdapter<BookBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_menu_detail) {

    override fun convert(holder: BaseViewHolder, item: BookBean) {
        holder.setText(R.id.home_menu_detail_book_adapter_title,item.describe)
        holder.setText(R.id.home_menu_detail_book_adapter_content,item.tips)
        holder.setText(R.id.home_menu_detail_book_adapter_author,item.author)
        loadRoundCornersImage(4f,holder.getView(R.id.home_menu_detail_book_adapter_icon),item.icon)
    }
}

