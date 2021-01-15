package com.rm.module_main.activity.guide

import com.rm.baselisten.mvvm.BaseFragment
import com.rm.module_main.R

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class GuideThreeFragment : BaseFragment() {

    override fun initLayoutId() = R.layout.fragment_main_three

    override fun initData() {
    }

    companion object {
        fun newInstance(): GuideThreeFragment {
            return GuideThreeFragment()
        }
    }
}