package com.rm.module_home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.business_lib.bean.ChapterList
import com.rm.module_home.R

class HomeDetailChapterAdapter : BaseQuickAdapter<ChapterList,BaseViewHolder>(R.layout.home_item_detail_chapter){
    override fun convert(holder: BaseViewHolder, item: ChapterList) {
        holder.setText(R.id.detail_chapter_name_tx , item.chapter_name)
            .setText(R.id.detail_chapter_views_tx,item.play_count)
            ?.setText(R.id.detail_chapter_duration_tx , item.duration)
            ?.setText(R.id.detail_chapter_upload_tx, item.created_at)
            ?.setText(R.id.detail_chapter_number_tx,item.sequence)
    }
}