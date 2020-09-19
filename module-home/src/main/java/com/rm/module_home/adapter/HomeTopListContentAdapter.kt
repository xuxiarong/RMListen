package com.rm.module_home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.flyco.roundview.RoundTextView
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.R
import com.rm.module_home.bean.HomeTopListDataBean

/**
 *
 * @author yuanfang
 * @date 9/17/20
 * @description
 *
 */
class HomeTopListContentAdapter(
    mViewModel: BaseVMViewModel,
    viewModelBrId: Int,
    vmDataBrId: Int
) : BaseBindVMAdapter<HomeTopListDataBean>(
    mViewModel,
    mutableListOf(),
    R.layout.home_adapter_top_list_content,
    viewModelBrId,
    vmDataBrId
) {
    override fun convert(holder: BaseViewHolder, item: HomeTopListDataBean) {
        super.convert(holder, item)
        val view = holder.getView<RoundTextView>(R.id.home_top_list_book_adapter_label)
        view.text = (holder.adapterPosition + 1).toString()
        val color = when (holder.adapterPosition) {
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
                R.color.business_color_999999
            }
        }
        view.delegate.backgroundColor = view.Color(color)
    }
}