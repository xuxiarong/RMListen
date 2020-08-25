package com.rm.baselisten.adapter

import android.content.Context
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
open class BaseBindAdapter<T> constructor(var data: List<T>?, var layoutId: Int, var brId: Int) :
    RecyclerView.Adapter<BaseBindHolder>() {

    protected lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BaseBindHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        if(data == null){
            return
        }
        holder.binding.setVariable(brId, data!![position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (data == null) {
            0
        } else {
            data!!.size
        }
    }

}