package com.rm.baselisten.mvvm

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.rm.baselisten.R
import com.rm.baselisten.databinding.ActivityBaseVmBinding
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.util.dip
import com.rm.baselisten.viewmodel.BaseVMViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

/**
 * desc   :包含网络状态，数据状态的基类Activity T:对应界面布局的dataBind对象 VM:对应界面的ViewModel对象
 * date   : 2020/08/04
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVMActivity<V : ViewDataBinding, VM : BaseVMViewModel> : BaseActivity() {

    /**
     * 初始化base的dataBind对象，并且注册lifecycle
     */
    private val mBaseBinding: ActivityBaseVmBinding by lazy {
        DataBindingUtil.setContentView<ActivityBaseVmBinding>(this, R.layout.activity_base_vm)
            .apply {
                lifecycleOwner = this@BaseVMActivity
            }
    }
    protected val mViewModel by lazy {
        val clazz = this.javaClass.kotlin.supertypes[0].arguments[1].type?.classifier as KClass<VM>
        getViewModel(clazz) //koin 注入
    }

    /**
     * 定义子类的View，用于跟子类的dataBind进行绑定
     */
    private var mChildView: View? = null

    /**
     * 定义子类的dataBing对象
     */
    protected lateinit var mDataBind: V

    /**
     * 设置dataBind为true，让父类不要setContentView
     * @return Boolean
     */
    override fun isDataBind() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取子类初始化的ViewModel
//        createViewModel()
        mBaseBinding.viewModel = mViewModel
        //开启base的liveData的数据变化监听
        startBaseObserve()
        //初始化子类的布局
        initChild()
        //开启子类的LiveData监听
        startObserve()
        //初始化子类的View
        initView()
        //初始化子类的数据
        initData()
    }

    /**
     * 初始化子类布局
     */
    private fun initChild() {
        if (!mBaseBinding.baseChildView.isInflated) {
            mBaseBinding.baseChildView.viewStub?.layoutResource = getLayoutId()
            mChildView = mBaseBinding.baseChildView.viewStub?.inflate()
            if (mChildView != null) {
                mDataBind = DataBindingUtil.bind(mChildView!!)!!
                mDataBind.setVariable(initModelBrId(),mViewModel)
            }
        }
    }

    /**
     * 开始监控baseViewModel的数据变化，包含网络状态，标题栏，以及错误类的布局加载
     */
    private fun startBaseObserve() {

        mViewModel.baseStatusModel.observe(this, Observer {
            setStatus(it)
        })

        mViewModel.baseTitleModel.observe(this, Observer {
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
                if (!mBaseBinding.baseEmpty.isInflated) {
                    mBaseBinding.baseEmpty.viewStub?.layoutResource = initEmptyLayout()
                    mBaseBinding.baseEmpty.viewStub?.inflate()
                }
                mChildView?.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_NET_ERROR -> {
                if (!mBaseBinding.baseError.isInflated) {
                    mBaseBinding.baseError.viewStub?.layoutResource = initErrorLayout()
                    mBaseBinding.baseError.viewStub?.inflate()
                }
                mChildView?.visibility = View.GONE
            }
            BaseNetStatus.BASE_SHOW_LOADING -> {
                if (!mBaseBinding.baseLoad.isInflated) {
                    mBaseBinding.baseLoad.viewStub?.layoutResource = initLoadLayout()
                    mBaseBinding.baseLoad.viewStub?.inflate()
                }
                mChildView?.visibility = View.VISIBLE
            }
            BaseNetStatus.BASE_SHOW_CONTENT -> {
                mChildView?.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 设置标题栏
     */
    private fun setTitle() {
        if (!mBaseBinding.baseTitleLayout.isInflated) {
            mBaseBinding.baseTitleLayout.viewStub?.inflate()
            if (mChildView != null) {
                val layoutParams = mChildView?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dip(48.0f)
                mChildView?.layoutParams = layoutParams
            }
        }
    }

    /**
     *  初始化子类viewModel的BrId
     * @return Int
     */
    abstract fun initModelBrId() : Int

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


//    /**
//     * 创建 ViewModel
//     */
//    @Suppress("UNCHECKED_CAST")
//    private fun createViewModel() {
//        val type = javaClass.genericSuperclass
//        if (type is ParameterizedType) {
//            val tp = type.actualTypeArguments[1]
//            val tClass = tp as? Class<VM> ?: BaseNetViewModel::class.java
//            mViewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
//        }
//
//    }
//    class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return modelClass.newInstance()
//        }
//    }
}