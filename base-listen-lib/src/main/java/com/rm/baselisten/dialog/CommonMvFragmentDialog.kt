package com.rm.baselisten.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonMvFragmentDialog : BaseMvFragmentDialog() {


    fun showCommonDialog(activity: FragmentActivity,layoutId : Int,viewModel : BaseVMViewModel,viewModelBrId : Int) : CommonMvFragmentDialog{
        val bundle  = Bundle()
        bundle.putInt("layoutId",layoutId)
        bundle.putParcelable("viewModel",viewModel)
        bundle.putInt("viewModelBrId",viewModelBrId)
        arguments = bundle
        showDialog<CommonMvFragmentDialog>(activity)
        return this
    }
}