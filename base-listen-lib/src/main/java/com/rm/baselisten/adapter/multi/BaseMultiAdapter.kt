package com.rm.baselisten.adapter.multi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.holder.BaseBindHolder


/**
 * desc   : dataBinding模式抽象的多Item基类BaseAdapter
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseMultiAdapter<T : MultiItemEntity > constructor(private var brId: Int) :
    BaseMultiItemQuickAdapter<T,BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when (viewType) {
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW, LOAD_MORE_VIEW -> {
                super.onCreateViewHolder(parent, viewType)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
                val baseBindHolder = BaseBindHolder(binding)
                bindViewClickListener(baseBindHolder,viewType)
                return baseBindHolder
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(getItemViewType(position)) {
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW, LOAD_MORE_VIEW -> {
                super.onBindViewHolder(holder, position)
            }
            else -> {
                DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
                    ?.setVariable(brId, data[position - headerLayoutCount])
                convert(holder, data[position - headerLayoutCount])
                DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
                    ?.executePendingBindings()
            }
        }
    }
}