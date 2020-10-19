package com.rm.module_home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.viewmodel.HomeMenuViewModel

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class MenuListAdapter(private val mViewModel: HomeMenuViewModel) :
    BaseBindVMAdapter<SheetInfoBean>(
        mViewModel,
        mutableListOf(),
        R.layout.home_adapter_menu,
        BR.itemViewModel,
        BR.menuItemBean
    ) {
    override fun convert(holder: BaseViewHolder, item: SheetInfoBean) {
        super.convert(holder, item)
        holder.getView<RecyclerView>(R.id.home_menu_adapter_recycler_view).apply {
            if (tag != true) {
                tag = true
                item.audio_list?.let {
                    bindGridLayout(
                        CommonBindVMAdapter<AudioBean>(
                            mViewModel,
                            it,
                            R.layout.home_adapter_menu_book,
                            BR.audioViewModel,
                            BR.audioBean
                        ), 3
                    )
                    gridItemDecoration(8f)
                }
            }
        }
    }
}
