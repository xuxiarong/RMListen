package com.rm.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.flyco.roundview.RoundTextView
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.module_home.R
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.bean.HomeTopListDataBean

class HomeTopListContentAdapter(private val brId: Int) :
    BaseQuickAdapter<HomeTopListDataBean, BaseViewHolder>(R.layout.home_adapter_top_list_content) {
    private var mDataBinding: ViewDataBinding? = null
    override fun convert(holder: BaseViewHolder, item: HomeTopListDataBean) {
        holder.setText(R.id.home_top_list_book_adapter_label, "${holder.adapterPosition + 1}")
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
        val roundTextView = holder.getView<RoundTextView>(R.id.home_top_list_book_adapter_label)
        roundTextView.delegate.backgroundColor = ContextCompat.getColor(context, color)
        mDataBinding?.setVariable(brId,item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.home_adapter_top_list_content,
            parent,
            false
        )
        return BaseViewHolder(mDataBinding!!.root)
    }

}