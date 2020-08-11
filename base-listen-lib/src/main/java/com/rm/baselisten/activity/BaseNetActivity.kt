package com.rm.baselisten.activity

import android.os.Bundle
import android.view.View
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

    protected lateinit var baseBinding : ActivityBaseNetBinding
    protected lateinit var baseViewModel : VM
    protected lateinit var databind : T

    override fun onCreate(savedInstanceState: Bundle?) {
        baseViewModel = getViewModel()
        baseViewModel.setContentView(baseViewModel.layoutId)

        baseBinding = DataBindingUtil.setContentView<ActivityBaseNetBinding>(this,R.layout.activity_base_net).apply{
            lifecycleOwner = this@BaseNetActivity
        }
        baseBinding.viewModel = baseViewModel
        databind = DataBindingUtil.bind(View.inflate(this,baseViewModel.layoutId,null))!!
        super.onCreate(savedInstanceState)
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