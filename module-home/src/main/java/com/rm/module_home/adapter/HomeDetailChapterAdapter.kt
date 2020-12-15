package com.rm.module_home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.util.DLog
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.db.download.DownloadChapter
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
class HomeDetailChapterAdapter(mViewModel: HomeDetailViewModel) :
    BaseBindVMAdapter<DownloadChapter>(
        mViewModel,
        mutableListOf(),
        R.layout.home_item_detail_chapter,
        BR.chapterclick,
        BR.DetailChapterViewModel
    ) {
    private var mSort = AudioSortType.SORT_ASC

    override fun convert(holder: BaseViewHolder, item: DownloadChapter) {
        super.convert(holder, item)
        if (mSort == AudioSortType.SORT_ASC) {
            holder.setText(R.id.detail_chapter_number_tx, "${holder.adapterPosition + 1}")
        } else {
            holder.setText(
                R.id.detail_chapter_number_tx,
                "${itemCount - holder.adapterPosition - footerLayoutCount}"
            )
        }
    }
}