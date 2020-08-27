package com.rm.baselisten.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.holder.BaseBindHolder


/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseMultiAdapter<T : BaseMultiAdapter.IBindItemType> constructor(var data: List<T>, private var brId: Int) :
    RecyclerView.Adapter<BaseBindHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return BaseBindHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        holder.binding.setVariable(brId, data[position])
        convert(holder,position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface IBindItemType{
        fun bindType() : Int
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].bindType()
    }

    abstract fun convert(holder: BaseBindHolder, position: Int)
}