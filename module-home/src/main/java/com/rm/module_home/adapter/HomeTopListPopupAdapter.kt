package com.rm.module_home.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.module_home.R
import com.rm.module_home.bean.HomeRankSegBean
import java.text.FieldPosition

class HomeTopListPopupAdapter : BaseQuickAdapter<HomeRankSegBean, BaseViewHolder>(layoutResId = R.layout.home_item_top_list_popup) {

    private var mPosition = -1
    private var mCurTv: TextView? = null

    override fun convert(holder: BaseViewHolder, item: HomeRankSegBean) {

        val color = if (mPosition == -1 && holder.adapterPosition == 0) {
            mCurTv = holder.getView(R.id.home_item_popup_top_list_tv)
            R.color.business_color_ff5e5e
        } else {
            R.color.business_text_color_666666
        }

        holder.setTextColor(
            R.id.home_item_popup_top_list_tv,
            ContextCompat.getColor(context, color)
        )
        holder.setText(R.id.home_item_popup_top_list_tv, item.name)
    }

    fun setSelectTv(position: Int, textView: TextView?) {
        mPosition = position
        mCurTv?.setTextColor(ContextCompat.getColor(context, R.color.business_text_color_666666))
        textView?.setTextColor(ContextCompat.getColor(context, R.color.business_color_ff5e5e))
        mCurTv = textView
    }
}