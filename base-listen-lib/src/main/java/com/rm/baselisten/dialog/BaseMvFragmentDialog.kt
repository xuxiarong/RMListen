package com.rm.baselisten.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseMvFragmentDialog : BaseFragmentDialog(){

    /**
     * 定义子类的dataBing对象
     */
     var mDataBind: ViewDataBinding? = null

    /**
     *  初始化子类viewModel的BrId
     * @return Int
     */
    abstract val initModelBrId : Int

    abstract val  viewModel: BaseViewModel

    /**
     *  初始化子类viewModel的BrId
     * @return Int
     */
    abstract fun initModelData()

    /**
     * 开启子类的LiveData观察者
     */
    abstract fun startObserve()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBind =DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mDataBind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBind?.setVariable(initModelBrId,viewModel)
        initView()
        initModelData()
    }

    fun getBind(){

    }
}