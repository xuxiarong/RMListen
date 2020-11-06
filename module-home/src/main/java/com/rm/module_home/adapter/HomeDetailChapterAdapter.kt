package com.rm.module_home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.business_lib.bean.ChapterList
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.viewmodel.HomeDetailViewModel

/**
 *
 * @author yuanfang
 * @date 9/17/20
 * @description
 *
 */
class HomeDetailChapterAdapter(mViewModel: HomeDetailViewModel) : BaseBindVMAdapter<ChapterList>(
    mViewModel,
    mutableListOf(),
    R.layout.home_item_detail_chapter,
    BR.chapterclick,
    BR.DetailChapterViewModel
) {
    override fun convert(holder: BaseViewHolder, item: ChapterList) {
        super.convert(holder, item)
        holder.setText(R.id.detail_chapter_number_tx, "${holder.adapterPosition + 1}")
    }
}