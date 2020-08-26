package com.rm.baselisten.mvvm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.rm.baselisten.R
import com.rm.baselisten.databinding.BaseFragmentVmBinding
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.util.DisplayUtils
import com.rm.baselisten.viewmodel.BaseNetViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

/**
 * desc   :
 * date   : 2020/08/26
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVMFragment<V : ViewDataBinding, VM : BaseNetViewModel> : BaseFragment() {


    /**
     * 初始化base的dataBind对象，并且注册lifecycle
     */
    private lateinit var baseBinding: BaseFragmentVmBinding

    /**
     * 定义子类的View，用于跟子类的dataBind进行绑定
     */
    private var childView: View? = null

    /**
     * 定义子类的dataBing对象
     */
    protected lateinit var dataBind: V

    protected val mViewModel by lazy {
        val clazz = this.javaClass.kotlin.supertypes[0].arguments[1].type?.classifier as KClass<VM>
        getViewModel(clazz) //koin 注入
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = DataBindingUtil.inflate<BaseFragmentVmBinding>(inflater, R.layout.base_fragment_vm, container, false).apply {
            lifecycleOwner = this@BaseVMFragment
        }
        return baseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseBinding.viewModel = mViewModel
        startBaseObserve()
        initChild()
        startObserve()
        initView()
        initData()
    }


    /**
     * 初始化子类布局
     */
    private fun initChild() {
        if (!baseBinding.baseChildView.isInflated) {
            baseBinding.baseChildView.viewStub?.layoutResource = initLayoutId()
            childView = baseBinding.baseChildView.viewStub?.inflate()
            if (childView != null) {
                dataBind = DataBindingUtil.bind(childView!!)!!
            }
        }
    }

    /**
     * 开始监控baseViewModel的数据变化，包含网络状态，标题栏，以及错误类的布局加载
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun startBaseObserve() {

        mViewModel.baseStatusModel.observe(this@BaseVMFragment, Observer {
            setStatus(it)
        })

        mViewModel.baseTitleModel.observe(this@BaseVMFragment, Observer {
            setTitle()
        })
    }

    /**
     * 根据网络状态，数据状态设置显示的View
     * @param statusModel BaseStatusModel 当前的界面状态
     */
    private fun setStatus(statusModel: BaseStatusModel) {
        when (statusModel.netStatus) {
            BaseNetStatus.BASE_SHOW_DATA_EMPTY -> {
                if (!baseBinding.baseEmpty.isInflated) {
                    baseBinding.baseEmpty.viewStub?.layoutResource = initEmptyLayout()
                    baseBinding.baseEmpty.viewStub?.inflate()
                }
                childView?.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_NET_ERROR -> {
                if (!baseBinding.baseError.isInflated) {
                    baseBinding.baseError.viewStub?.layoutResource = initErrorLayout()
                    baseBinding.baseError.viewStub?.inflate()
                }
                childView?.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_LOADING -> {
                if (!baseBinding.baseLoad.isInflated) {
                    baseBinding.baseLoad.viewStub?.layoutResource = initLoadLayout()
                    baseBinding.baseLoad.viewStub?.inflate()
                }
                childView?.visibility = View.VISIBLE
            }
            BaseNetStatus.BASE_SHOW_CONTENT -> {
                childView?.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 设置标题栏
     */
    private fun setTitle() {
        if (!baseBinding.baseTitleLayout.isInflated) {
            baseBinding.baseTitleLayout.viewStub?.inflate()
            if (childView != null && context!=null) {
                val layoutParams = childView?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = DisplayUtils.dip2px(this.context!!, 48.0f)
                childView?.layoutParams = layoutParams
            }
        }
    }

    /**
     * 开启子类的LiveData观察者
     */
    abstract fun startObserve()


    /**
     * 初始化网络错误的View
     * @return Int 化网络错误View的layoutId
     */
    protected open fun initErrorLayout(): Int {
        return R.layout.base_layout_error
    }

    /**
     * 初始化加载中的View
     * @return Int 加载中View的layoutId
     */
    protected open fun initLoadLayout(): Int {
        return R.layout.base_layout_loading
    }

    /**
     * 初始化空数据的View
     * @return Int 空数据View的layoutId
     */
    protected open fun initEmptyLayout(): Int {
        return R.layout.base_layout_empty
    }


}