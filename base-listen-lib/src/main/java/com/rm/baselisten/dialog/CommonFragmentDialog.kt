package com.rm.baselisten.dialog

import androidx.fragment.app.FragmentActivity

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonFragmentDialog constructor(commonLayoutId: Int) : BaseFragmentDialog() {

    override val layoutId = commonLayoutId

    fun showCommonDialog(activity: FragmentActivity) {
        showDialog(activity)
    }

}