package com.rm.baselisten.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.R
import com.rm.baselisten.databinding.ActivityBaseNetBinding
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.viewmodel.BaseNetViewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
abstract class BaseNetActivity< T : ViewDataBinding, VM : BaseNetViewModel> : BaseVMActivity() {

    abstract fun getViewModel() : VM

    private lateinit var baseBinding : ActivityBaseNetBinding
    private lateinit var baseViewModel : VM
    protected lateinit var databind : T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView<ActivityBaseNetBinding>(this,R.layout.activity_base_net).apply{
            lifecycleOwner = this@BaseNetActivity
        }
        baseViewModel = getViewModel()
        baseBinding.flBaseLayout.setContentLayout(baseViewModel.layoutId)
        baseBinding.viewModel = baseViewModel
        databind = DataBindingUtil.bind(baseBinding.flBaseLayout.getContentView())!!
        startObserve()
        initView()
        initData()
    }


    protected fun showContent(){
        baseViewModel.showContent()
    }
    protected fun showEmpty(){
        baseViewModel.showEmpty()
    }
    protected fun showError(){
        baseViewModel.showError()
    }
    protected fun showLoad(){
        baseViewModel.showLoad()
    }

}