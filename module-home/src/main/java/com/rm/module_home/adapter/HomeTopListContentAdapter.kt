package com.rm.module_home.adapter

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.flyco.roundview.RoundTextView
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.module_home.R
import com.rm.module_home.bean.TopListBean

class HomeTopListContentAdapter :
    BaseQuickAdapter<TopListBean, BaseViewHolder>(layoutResId = R.layout.home_adapter_top_list_content) {
    override fun convert(holder: BaseViewHolder, item: TopListBean) {
        holder.setText(R.id.home_top_list_book_adapter_author, item.name)
        holder.setText(R.id.home_top_list_book_adapter_label, "${holder.adapterPosition+1}")
        holder.setText(R.id.home_top_list_book_adapter_content, item.content)

        val imageView = holder.getView<ImageView>(R.id.home_top_list_book_adapter_icon)
        loadRoundCornersImage(4f,imageView,item.icon)
      val color=  when(holder.adapterPosition){
            0->{R.color.business_color_ff5e5e}
            1->{R.color.business_color_ffba56}
            2->{R.color.business_color_ffdf85}
            else->{R.color.business_color_999999}
        }
        val roundTextView = holder.getView<RoundTextView>(R.id.home_top_list_book_adapter_label)
        roundTextView.delegate.setBackgroundColor(ContextCompat.getColor(context,color))
    }
}