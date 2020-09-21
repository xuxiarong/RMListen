package com.rm.module_mine

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.module_mine.bean.MineHomeBean
import com.rm.module_mine.bean.MineHomeDetailBean
import com.rm.module_mine.viewmodel.MineHomeViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineHomeAdapter(viewModel: MineHomeViewModel) : BaseBindVMAdapter<MineHomeBean>(
    viewModel,
    mutableListOf(),
    R.layout.mine_adapter_home,
    BR.itemViewModel,
    BR.item
) {
    override fun convert(holder: BaseViewHolder, item: MineHomeBean) {
        super.convert(holder, item)

        holder.getView<RecyclerView>(R.id.mine_adapter_home_recycler_view).apply {
            bindGridLayout(
                CommonBindVMAdapter(
                    viewModel,
                    item.data,
                    R.layout.mine_adapter_home_detail,
                    BR.itemDetailViewModel,
                    BR.detailItem
                )
            ,4)
        }
    }

}