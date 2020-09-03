package com.rm.baselisten.adapter.single

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rm.baselisten.holder.BaseBindHolder


/**
 * desc   : dataBinding模式抽象的单Item基类BaseAdapter
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseBindAdapter<T> constructor(data : MutableList<T>,var layoutId: Int, private var brId: Int) :
    BaseQuickAdapter<T, BaseBindHolder>(layoutId,data) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BaseBindHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(brId, data[position])
        convert(holder,data[position])
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.executePendingBindings()
    }

    override fun convert(holder: BaseBindHolder, item: T) {

    }

}