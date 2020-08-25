package com.rm.module_home.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.R
import com.rm.module_home.bean.MenuItemBean

/**
 * desc   : 书单列表Adapter
 * date   : 2020/08/21
 * version: 1.0
 */
class MenuListAdapter :
    BaseQuickAdapter<MenuItemBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_menu) {

    override fun convert(holder: BaseViewHolder, item: MenuItemBean) {
        holder.setText(R.id.home_menu_adapter_name, item.name)
            .setText(
                R.id.home_menu_adapter_total_books_number,
                holder.itemView.context.resources.getString(
                    R.string.format_total_books,
                    item.totalNumber
                )
            )
            .setText(R.id.home_menu_adapter_collection_number, item.collectionNumber)
            .setText(R.id.home_menu_adapter_author_name, item.authorName)

        holder.getView<RecyclerView>(R.id.home_menu_adapter_recycler_view).apply {
            bindGridLayout(MenuBookAdapter().apply { setNewInstance(item.bookList) }, 3)
            gridItemDecoration(8f)
        }

        holder.getView<ImageView>(R.id.home_menu_adapter_author_icon).bindUrl(bindUrl = item.authorIcon,isCircle = true)
    }
}

class MenuBookAdapter :
    BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.home_adapter_menu_book) {
    override fun convert(holder: BaseViewHolder, item: BookBean) {
        holder.setText(R.id.home_menu_book_adapter_tips, item.tips)
            .setText(R.id.home_menu_book_adapter_name, item.name)

        holder.getView<ImageView>(R.id.home_menu_book_adapter_icon).bindUrl(4f,item.icon)
    }


}

//fun getCannotScrollGridLayoutManager(context: Context, orientation: Int): LinearLayoutManager {
//    return object : GridLayoutManager(context, 3) {
//        override fun canScrollHorizontally(): Boolean {
//            return false
//        }
//
//        override fun canScrollVertically(): Boolean {
//            return false
//        }
//    }
//}