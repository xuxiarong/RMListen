package com.rm.baselisten.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.rm.baselisten.R
import com.rm.baselisten.binding.bindChild
import com.rm.baselisten.databinding.ActivityBaseNetBinding
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.viewmodel.BaseNetViewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
abstract class BaseNetActivity< T : ViewDataBinding, VM : BaseNetViewModel> : BaseVMActivity() {

    private val baseBinding : ActivityBaseNetBinding by lazy {
        DataBindingUtil.setContentView<ActivityBaseNetBinding>(this, R.layout.activity_base_net).apply {
            lifecycleOwner = this@BaseNetActivity
        }
    }

    abstract fun getViewModel() : VM
    private lateinit var baseViewModel : VM
    protected lateinit var databind : T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = getViewModel()
        baseBinding.viewModel = baseViewModel
        startBaseObserve()
        val contentView = baseBinding.clBaseContainer.bindChild(baseViewModel.layoutId)
        databind = DataBindingUtil.bind(contentView)!!
        startObserve()
        initView()
        initData()
    }


    private fun startBaseObserve(){

        baseViewModel.baseStatusModel.observe(this, Observer {
            setStatus()
        })

        baseViewModel.baseTitleModel.observe(this, Observer {
            setTitle()
        })

        baseViewModel.baseLayoutModel.observe(this, Observer {
            setLayout()
        })


    }

    private fun setLayout() {

    }

    private fun setStatus() {

    }

    private fun setTitle() {
        if(!baseBinding.baseTitleLayout.isInflated){
            baseBinding.baseTitleLayout.viewStub?.inflate()
        }
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