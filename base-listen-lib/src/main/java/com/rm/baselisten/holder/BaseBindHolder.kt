package com.rm.baselisten.holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
class BaseBindHolder constructor(var binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root)

