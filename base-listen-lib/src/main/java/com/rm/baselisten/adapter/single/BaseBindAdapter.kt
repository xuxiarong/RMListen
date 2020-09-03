package com.rm.baselisten.adapter.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.holder.BaseBindHolder


/**
 * desc   : dataBinding模式抽象的单Item基类BaseAdapter
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseBindAdapter<T> constructor(data : MutableList<T>,var layoutId: Int, private var brId: Int) :
    BaseQuickAdapter<T, BaseViewHolder>(layoutId,data) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW, LOAD_MORE_VIEW -> {
                super.onCreateViewHolder(parent, viewType)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
                return BaseBindHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        when(getItemViewType(position)){
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW,LOAD_MORE_VIEW->{
                super.onBindViewHolder(holder, position)
            }
            else ->{
//                DLog.d("suolong","type = ${getItemViewType(position)}")
                DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(brId, data[position - headerLayoutCount ])
                convert(holder,data[position - headerLayoutCount])
                DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.executePendingBindings()
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: T) {

    }

}