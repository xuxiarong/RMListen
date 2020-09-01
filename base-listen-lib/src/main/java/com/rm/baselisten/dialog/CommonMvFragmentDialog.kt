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

    override val initModelBrId = commonViewModelBrId

    override val viewModel = commonViewModel

    override val layoutId = commonLayoutId


    override fun initModelData() {

    }

    override fun startObserve() {

    }

    fun showCommonDialog(activity: FragmentActivity) {
        showDialog(activity)
    }
}