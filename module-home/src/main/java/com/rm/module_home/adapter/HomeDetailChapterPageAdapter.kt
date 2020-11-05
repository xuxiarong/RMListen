package com.rm.module_home.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.Drawable
import com.rm.business_lib.bean.DataStr
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
class HomeDetailChapterPageAdapter(
    private val mViewModel: HomeDetailViewModel
) : BaseBindVMAdapter<DataStr>(
    mViewModel,
    mutableListOf(),
    R.layout.home_chapter_item_anthology,
    BR.click,
    BR.item
) {
    private var oldTextView: AppCompatTextView? = null
    private var position = 0

    override fun convert(holder: BaseViewHolder, item: DataStr) {
        super.convert(holder, item)
        val textView = holder.itemView as AppCompatTextView
        if (oldTextView == textView) {
            setSelect(true, oldTextView)
        } else if (oldTextView == null && holder.adapterPosition == 0) {
            setSelect(true, textView)
        } else {
            setSelect(false, textView)
        }
        textView.setOnClickListener { v ->
            position = holder.adapterPosition
            setSelect(false, oldTextView)
            setSelect(true, v as AppCompatTextView)
            mViewModel.itemClickSelectChapter(item.position)
        }
    }

    private fun setSelect(select: Boolean, textView: AppCompatTextView?) {
        if (select) {
            textView?.apply {
                setTextColor(Color(R.color.business_text_color_ffffff))
                background = Drawable(R.drawable.home_chapter_page_select)
                oldTextView = this
            }
        } else {
            textView?.apply {
                setTextColor(Color(R.color.business_text_color_666666))
                background = Drawable(R.drawable.home_chapter_page_un_select)
            }
        }
    }
}