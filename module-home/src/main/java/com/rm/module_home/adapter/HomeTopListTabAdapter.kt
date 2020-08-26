package com.rm.module_home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.module_home.R
import com.rm.module_home.bean.CategoryTabBean

class HomeTopListTabAdapter :
    BaseQuickAdapter<CategoryTabBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_top_list_tab) {

    override fun convert(holder: BaseViewHolder, item: CategoryTabBean) {
        holder.setText(R.id.home_top_list_tab_adapter_tv, item.name)
        val position = holder.adapterPosition
        val tv = holder.getView<TextView>(R.id.home_top_list_tab_adapter_tv)
        if (position == 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.business_shape_circle_ff5e5e, 0, 0, 0)
            tv.setBackgroundResource(R.drawable.home_top_list_tab_bg)
            tv.elevation=context.resources.getDimension(R.dimen.dp_2)
        }else{
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.business_shape_circle_transparent, 0, 0, 0)
            tv.elevation=0f
        }
    }
}