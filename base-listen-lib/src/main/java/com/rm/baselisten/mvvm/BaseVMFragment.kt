package com.rm.baselisten.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseVMFragment<V:ViewDataBinding>(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    protected lateinit var binding:V

    protected  fun < V : ViewDataBinding> binding(
            inflater: LayoutInflater,
            @LayoutRes layoutId: Int,
            container: ViewGroup?
    ): V =   DataBindingUtil.inflate<V>(inflater,layoutId, container,false).apply {
        lifecycleOwner = this@BaseVMFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = binding(inflater,layoutId,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        startObserve()
        initView()
        initData()
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()
}