package com.rm.module_search.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.bean.SearchHotDetailBean

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchRecommendAdapter(mViewModel: BaseVMViewModel) :
    BaseBindVMAdapter<SearchHotDetailBean>(
        mViewModel,
        mutableListOf(),
        R.layout.search_adapter_content,
        BR.viewModel,
        BR.item
    ) {
    override fun convert(holder: BaseViewHolder, item: SearchHotDetailBean) {
        super.convert(holder, item)
        val position = holder.adapterPosition
        val view = holder.getView<AppCompatTextView>(R.id.search_adapter_content_num)
        view.text = "${(position + 1)}"
        val color = when (position) {
            0 -> {
                R.color.business_color_ff5e5e
            }
            1 -> {
                R.color.business_color_ffba56
            }
            2 -> {
                R.color.business_color_ffdf85
            }
            else -> {
                R.color.business_color_b1b1b1
            }
        }
        view.setTextColor(view.Color(color))

        val icon = when (item.level_up) {
            "1" -> {
                R.drawable.search_icon_rank_ea
            }
            "2" -> {
                R.drawable.search_icon_rank_eh
            }
            else -> {
                R.drawable.search_icon_rank_eb
            }
        }

        holder.getView<AppCompatImageView>(R.id.search_adapter_content_icon).setImageResource(icon)
    }
}