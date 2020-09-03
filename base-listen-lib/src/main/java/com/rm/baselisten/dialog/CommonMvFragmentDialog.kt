package com.rm.baselisten.dialog

import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonMvFragmentDialog constructor(
    commonViewModel: BaseViewModel,
    commonLayoutId: Int,
    commonViewModelBrId: Int
) : BaseMvFragmentDialog() {

    var initDialogViewRef : () -> Unit = {}

    var initDialogDataRef : () -> Unit = {}

    override val initModelBrId = commonViewModelBrId

    override val viewModel = commonViewModel

    override val layoutId = commonLayoutId

    override fun initView() {
        initDialogViewRef()
    }

    override fun initModelData() {
        initDialogDataRef()
    }

    override fun startObserve() {

    }

    fun showCommonDialog(activity: FragmentActivity) : CommonMvFragmentDialog{
        showDialog(activity)
        return this
    }
}