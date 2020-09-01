package com.rm.baselisten.adapter.multi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.holder.BaseBindHolder


/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseMultiAdapter<T : MultiItemEntity > constructor(private var brId: Int) :
    BaseMultiItemQuickAdapter<T,BaseBindHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return BaseBindHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(brId, data[position])
        convert(holder,data[position])
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.executePendingBindings()
    }

}