package com.rm.baselisten.adapter.single

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
abstract class BaseBindAdapter constructor(var data: List<*>?, var layoutId: Int, private var brId: Int) :
    RecyclerView.Adapter<BaseBindHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BaseBindHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        if(data == null){
            return
        }
        holder.binding.setVariable(brId, data!![position])
        convert(holder,position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (data == null) {
            0
        } else {
            data!!.size
        }
    }

    protected open fun convert(holder: BaseBindHolder, position: Int){}

}