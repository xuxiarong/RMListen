package com.rm.baselisten.dialog

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.fragment.FragmentFactory

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */
open class CommonFragmentDialog  : BaseFragmentDialog() {


    fun showCommonDialog(activity: FragmentActivity,layoutId : Int) {

        val bundle = Bundle()
        bundle.putInt(LAYOUT_ID, layoutId)

        FragmentFactory.create(CommonFragmentDialog::class.java, bundle)
            .getFragment<CommonFragmentDialog>().showDialog<CommonFragmentDialog>(activity)
    }

}