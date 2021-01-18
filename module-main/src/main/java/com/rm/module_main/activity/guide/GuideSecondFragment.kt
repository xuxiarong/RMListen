package com.rm.module_main.activity.guide

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseFragment
import com.rm.module_main.R

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class GuideSecondFragment : BaseFragment() {

    override fun initLayoutId() = R.layout.fragment_main_guide_second

    override fun initData() {
    }

    companion object {
        fun newInstance(): GuideSecondFragment {
            return GuideSecondFragment()
        }
    }
}