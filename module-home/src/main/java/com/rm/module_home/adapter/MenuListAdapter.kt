package com.rm.module_home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.business_lib.bean.SheetMenuInfoBean
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.viewmodel.HomeMenuViewModel

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class MenuListAdapter(private val mViewModel: HomeMenuViewModel) :
    BaseBindVMAdapter<SheetMenuInfoBean>(
        mViewModel,
        mutableListOf(),
        R.layout.home_adapter_menu,
        BR.itemViewModel,
        BR.menuItemBean
    ) {
    override fun convert(holder: BaseViewHolder, item: SheetMenuInfoBean) {
        super.convert(holder, item)
        holder.getView<RecyclerView>(R.id.home_menu_adapter_recycler_view).apply {
            item.audio_list?.let {
                val list = if (it.size > 3) {
                    it.subList(0, 3)
                } else {
                    it
                }
                bindGridLayout(
                    CommonBindVMAdapter(
                        mViewModel,
                        list,
                        R.layout.home_adapter_menu_book,
                        BR.audioViewModel,
                        BR.audioBean
                    ), 3
                )
                if (itemDecorationCount == 0) {
                    gridItemDecoration(8f)
                }
            }
        }
    }
}
