package com.rm.module_home.adapter

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.decoration.GridSpaceItemDecoration
import com.rm.baselisten.thridlib.glide.loadCircleImage
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.baselisten.util.DisplayUtils
import com.rm.module_home.R
import com.rm.module_home.bean.BookBean
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
        val bookAdapter = MenuBookAdapter()
        bookAdapter.setNewInstance(item.bookList)
        holder.getView<RecyclerView>(R.id.home_menu_adapter_recycler_view).apply {
            layoutManager = getCannotScrollGridLayoutManager(
                holder.itemView.context,
                LinearLayoutManager.HORIZONTAL
            )
            adapter = bookAdapter
            addItemDecoration(GridSpaceItemDecoration(DisplayUtils.dip2px(holder.itemView.context,8f),0,false,GridSpaceItemDecoration.GRIDLAYOUT))
//            addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(holder.itemView.context, 16f)))
        }
        loadCircleImage(holder.getView(R.id.home_menu_adapter_author_icon), item.authorIcon)
    }
}

class MenuBookAdapter() :
    BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.home_adapter_menu_book) {
    override fun convert(holder: BaseViewHolder, item: BookBean) {
        holder.setText(R.id.home_menu_book_adapter_tips, item.tips)
            .setText(R.id.home_menu_book_adapter_name, item.name)
        loadRoundCornersImage(
            4f,
            holder.getView(R.id.home_menu_book_adapter_icon) as ImageView,
            item.icon
        )
    }


}

fun getCannotScrollGridLayoutManager(context: Context, orientation: Int): LinearLayoutManager {
    return object : GridLayoutManager(context, 3) {
        override fun canScrollHorizontally(): Boolean {
            return false
        }

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}