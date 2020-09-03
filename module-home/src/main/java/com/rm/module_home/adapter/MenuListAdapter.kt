package com.rm.module_home.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.menu.MenuViewModel
import com.rm.module_home.bean.MenuItemBean
import com.rm.module_home.databinding.HomeAdapterMenuBinding
import com.rm.module_home.databinding.HomeAdapterMenuBookBinding

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class MenuListAdapter(val viewModel: MenuViewModel) :
    BaseQuickAdapter<MenuItemBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_menu) {
    override fun convert(holder: BaseViewHolder, item: MenuItemBean) {
        DataBindingUtil.bind<HomeAdapterMenuBinding>(holder.itemView)
            ?.setVariable(BR.menuItemBean, item)
//        DataBindingUtil.bind<HomeAdapterMenuBinding>(holder.itemView)
//            ?.setVariable(BR.viewModel, viewModel)
        holder.getView<RecyclerView>(R.id.home_menu_adapter_recycler_view).apply {
            bindGridLayout(MenuBookAdapter().apply { setList(item.bookList) }, 3)
            gridItemDecoration(8f)
        }
    }
}

class MenuBookAdapter :
    BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.home_adapter_menu_book) {
    override fun convert(holder: BaseViewHolder, item: BookBean) {
        DataBindingUtil.bind<HomeAdapterMenuBookBinding>(holder.itemView)
            ?.setVariable(BR.bookBean, item)
    }
}