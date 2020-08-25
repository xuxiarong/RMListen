package com.rm.module_home.adapter

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.binding.bindUrl
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.R

/**
 * desc   : 书籍列表Adapter
 * date   : 2020/08/25
 * version: 1.0
 */
class BookAdapter : BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.home_adapter_book) {
    override fun convert(holder: BaseViewHolder, item: BookBean) {
        holder.setText(R.id.home_book_adapter_name, item.name)
            .setText(R.id.home_book_adapter_describe, item.describe)
            .setText(R.id.home_book_adapter_tips, item.tips)
            .setText(R.id.home_book_adapter_author, item.author)
        holder.getView<AppCompatImageView>(R.id.home_book_adapter_icon)
            .bindUrl(8f, item.icon)
    }
}