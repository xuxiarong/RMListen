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

    companion object{
        const val VIEW_MODEL_BR_ID = "viewModelBrId"
        const val VIEW_MODEL = "viewModel"
    }

    /**
     * 定义子类的dataBing对象
     */
     var mDataBind: ViewDataBinding? = null

    var initDialog : (() -> Unit) = {}

    val clickMap : HashMap<Int,()->Unit> = HashMap()

    val disMissIdMap : HashMap<Int,()->Unit> = HashMap()
    /**
     * 开启子类的LiveData观察者
     */
    abstract fun startObserve()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = arguments?.getInt(LAYOUT_ID, 0)?:0
        mDataBind =DataBindingUtil.inflate(inflater, layoutId, container, false)
        val viewModelBrId = arguments?.getInt(VIEW_MODEL_BR_ID, 0)?:0
        val viewModel :  BaseViewModel? = arguments?.getParcelable(VIEW_MODEL)
        if(viewModelBrId>0 && viewModel!=null){
            mDataBind?.setVariable(viewModelBrId,viewModel)
        }
        disMissIdMap.forEach { entry ->
            mDataBind?.root?.findViewById<View>(entry.key)?.setOnClickListener {
                dismiss()
                entry.value()
            }
        }
        initDialog()
        return mDataBind?.root
    }

    fun setClicks(viewId : Int,viewClickAction : ()->Unit){
        clickMap[viewId] = viewClickAction
    }

    fun setDismissIdAndAction(id : Int, action : ()->Unit){
        disMissIdMap[id] = action
    }

}