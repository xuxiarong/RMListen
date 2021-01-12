package com.rm.baselisten.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.rm.baselisten.R
import com.rm.baselisten.databinding.ActivityBaseVmBinding
import com.rm.baselisten.ktx.putAnyExtras
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.utilExt.dip
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

    /**
     * 延迟加载ViewModel
     */
    protected val mViewModel by lazy {
        val clazz = this.javaClass.kotlin.supertypes[0].arguments[1].type?.classifier as KClass<VM>
        getViewModel(clazz) //koin 注入
    }

    /**
     * 定义子类的View，用于跟子类的dataBind进行绑定
     */
    private var mChildView: View? = null

    /**
     * 定义子类空视图对象
     */
    var mDataShowView: View? = null

    /**
     * 定义子类的dataBing对象
     */
    protected var mDataBind: V? = null

    /**
     * 设置dataBind为true，让父类不要setContentView
     * @return Boolean
     */
    override fun isDataBind() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取子类初始化的ViewModel
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
        //添加通用的提示框
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBind?.unbind()
        mDataBind = null
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param view
     */
    fun hideKeyboard(view: View?) {
        if (view != null) {
            val im: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 初始化子类布局
     */
    private fun initChild() {
        if (!mBaseBinding.baseChildView.isInflated) {
            mBaseBinding.baseChildView.viewStub?.layoutResource = getLayoutId()
            mChildView = mBaseBinding.baseChildView.viewStub?.inflate()
            mChildView?.let {
                mDataBind = DataBindingUtil.bind(it)
                mDataBind?.setVariable(initModelBrId(), mViewModel)
            }
        }
    }

    fun getBaseContainer(): ConstraintLayout = mBaseBinding.clBaseContainer

    /**
     * 开始监控baseViewModel的数据变化，包含网络状态，标题栏，以及错误类的布局加载
     */
    private fun startBaseObserve() {

        mViewModel.baseStatusModel.observe(this, Observer {
            setStatus(it)
        })

        mViewModel.baseTitleModel.observe(this, Observer {
            setTitle(it)
        })

        mViewModel.baseToastModel.observe(this, Observer {
            if (it.isNetError) {
                ToastUtil.showTopNetErrorToast(this@BaseVMActivity)
                return@Observer
            }
            if (it.contentId > 0) {
                ToastUtil.showTopToast(
                    this@BaseVMActivity,
                    getString(it.contentId),
                    it.colorId,
                    it.canAutoCancel,
                    this@BaseVMActivity
                )
            } else {
                if (it.content != null) {
                    ToastUtil.showTopToast(
                        this@BaseVMActivity,
                        it.content,
                        it.colorId,
                        it.canAutoCancel,
                        this@BaseVMActivity
                    )
                } else {
                    Toast.makeText(this@BaseVMActivity, it.content, Toast.LENGTH_SHORT).show()
                }
            }
        })
        mViewModel.baseCancelToastModel.observe(this, Observer { isCancel ->
            if (!isFinishing) {
                if (isCancel) {
                    ToastUtil.cancelToast()
                }
            }
        })

        mViewModel.baseIntentModel.observe(this, Observer {
            val startIntent = Intent(this@BaseVMActivity, it.clazz)
            if (it.dataMap != null && it.dataMap.size > 0) {
                it.dataMap.forEach { (key, value) ->
                    startIntent.putAnyExtras(key, value)
                }
            }
            startActivityForResult(startIntent, it.requestCode)
        })

        mViewModel.baseFinishModel.observe(this, Observer {
            if (it.finish) {
                if (it.dataMap != null && it.dataMap.size > 0) {
                    val finishIntent = Intent()
                    it.dataMap.forEach { (key, value) ->
                        finishIntent.putAnyExtras(key, value)
                    }
                    setResult(it.resultCode, finishIntent)
                } else {
                    setResult(it.resultCode)
                }
                finish()
            }
        })

        mViewModel.baseResultModel.observe(this, Observer {
            if (it.dataMap != null && it.dataMap.size > 0) {
                val finishIntent = Intent()
                it.dataMap.forEach { (key, value) ->
                    finishIntent.putAnyExtras(key, value)
                }
                setResult(it.resultCode, finishIntent)
            } else {
                setResult(it.resultCode)
            }
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
                    mBaseBinding.baseEmpty.viewStub?.inflate()?.apply {
                        val tv = findViewById<TextView>(R.id.tv_empty)
                        statusModel.msgTips?.let { tv.text = it }
                    }
                }
                if (mDataShowView != null) {
                    mDataShowView!!.visibility = View.GONE
                } else {
                    mChildView?.visibility = View.GONE
                }
            }
            BaseNetStatus.BASE_SHOW_SEARCH_DATA_EMPTY -> {
                if (!mBaseBinding.baseSearchEmpty.isInflated) {
                    mBaseBinding.baseSearchEmpty.viewStub?.layoutResource = initSearchEmptyLayout()
                    mBaseBinding.baseSearchEmpty.viewStub?.inflate()
                }
                if (mDataShowView != null) {
                    mDataShowView!!.visibility = View.GONE
                } else {
                    mChildView?.visibility = View.GONE
                }
            }
            BaseNetStatus.BASE_SHOW_SERVICE_ERROR -> {
                setServiceError()
            }
            BaseNetStatus.BASE_SHOW_LOADING -> {
                if (!mBaseBinding.baseLoad.isInflated) {
                    mBaseBinding.baseLoad.viewStub?.layoutResource = initLoadLayout()
                    mBaseBinding.baseLoad.viewStub?.inflate()
                }
            }
            BaseNetStatus.BASE_SHOW_CONTENT -> {
                if (mDataShowView != null) {
                    mDataShowView!!.visibility = View.VISIBLE
                } else {
                    mChildView?.visibility = View.VISIBLE
                }
            }
            BaseNetStatus.BASE_SHOW_NET_ERROR -> {
                setServiceError()
                ToastUtil.showTopNetErrorToast(this@BaseVMActivity)
            }
        }
    }

    private fun setServiceError() {
        if (!mBaseBinding.baseError.isInflated) {
            mBaseBinding.baseError.viewStub?.layoutResource = initErrorLayout()
            mBaseBinding.baseError.viewStub?.inflate()
        }
        if (mDataShowView != null) {
            mDataShowView!!.visibility = View.GONE
        } else {
            mChildView?.visibility = View.GONE
        }
    }

    /**
     * 设置标题栏
     */
    private fun setTitle(titleModel: BaseTitleModel) {
        if (!mBaseBinding.baseTitleLayout.isInflated) {
            mBaseBinding.baseTitleLayout.viewStub?.inflate()
            if (mChildView != null) {
                val layoutParams = mChildView?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dip(48.0f)
                mChildView?.layoutParams = layoutParams
            }
        }
        if (titleModel.noTitle) {
            if (mChildView != null) {
                val layoutParams = mChildView?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dip(0)
                mChildView?.layoutParams = layoutParams
            }
        }
    }

    /**
     *  初始化子类viewModel的BrId
     * @return Int
     */
    abstract fun initModelBrId(): Int

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

    /**
     * 初始化搜索空数据的View
     * @return Int 空数据View的layoutId
     */
    protected open fun initSearchEmptyLayout(): Int {
        return R.layout.base_layout_search_empty
    }
}