package com.rm.baselisten.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.rm.baselisten.R
import com.rm.baselisten.binding.bindChild
import com.rm.baselisten.databinding.ActivityBaseNetBinding
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.viewmodel.BaseNetViewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
abstract class BaseNetActivity<T : ViewDataBinding, VM : BaseNetViewModel> : BaseVMActivity() {

    //初始化base的dataBind对象，并且注册lifecycle
    private val baseBinding: ActivityBaseNetBinding by lazy {
        DataBindingUtil.setContentView<ActivityBaseNetBinding>(this, R.layout.activity_base_net)
            .apply {
                lifecycleOwner = this@BaseNetActivity
            }
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initViewModel(): VM
    private lateinit var baseViewModel: VM
    protected lateinit var childView: View
    protected lateinit var dataBind : T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取子类初始化的ViewModel
        baseViewModel = initViewModel()
        baseBinding.viewModel = baseViewModel
        //开启base的liveData的数据变化监听
        startBaseObserve()
        //baseActivity布局添加子类的布局
        childView = baseBinding.clBaseContainer.bindChild(getLayoutId())
        //初始化子类的dataBind
        dataBind = DataBindingUtil.bind(childView)!!
        //开启子类的LiveData数据监听
        startObserve()
        initView()
        initData()
    }

    /**
     * 开始监控baseViewModel的数据变化，包含网络状态，标题栏，以及错误类的布局加载
     */
    private fun startBaseObserve() {

        baseViewModel.baseStatusModel.observe(this, Observer {
            setStatus(it)
        })

        baseViewModel.baseTitleModel.observe(this, Observer {
            setTitle()
        })
    }

    private fun setStatus(statusModel: BaseStatusModel) {
        when(statusModel.netStatus){
            BaseNetStatus.BASE_SHOW_DATA_EMPTY ->{
                if(!baseBinding.baseEmpty.isInflated){
                    baseBinding.baseEmpty.viewStub?.inflate()
                }
                childView.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_NET_ERROR ->{
                if(!baseBinding.baseError.isInflated){
                    baseBinding.baseError.viewStub?.inflate()
                }
                childView.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_LOADING ->{
                if(!baseBinding.baseLoad.isInflated){
                    baseBinding.baseLoad.viewStub?.inflate()
                }
                childView.visibility = View.VISIBLE
            }
            BaseNetStatus.BASE_SHOW_CONTENT ->{
                childView.visibility = View.VISIBLE
            }
        }
    }

    private fun setTitle() {
        if (!baseBinding.baseTitleLayout.isInflated) {
            baseBinding.baseTitleLayout.viewStub?.inflate()
        }
    }

    abstract fun startObserve()

}