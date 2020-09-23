package com.rm.module_mine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineSettingActivity
import com.rm.module_mine.databinding.MineFragmentHomeBinding
import com.rm.module_mine.viewmodel.MineHomeViewModel
import kotlinx.android.synthetic.main.mine_fragment_home.*

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineHomeFragment : BaseVMFragment<MineFragmentHomeBinding, MineHomeViewModel>() {
    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = MineHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initLayoutId() = R.layout.mine_fragment_home

    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        addClickListener()

    }

    private fun addClickListener() {
        mine_home_setup.setOnClickListener {
            MineSettingActivity.startActivity(it.context)
            // test 登出
//            loginOut()
        }
        mine_home_notice.setOnClickListener {
            // test 登陆
            RouterHelper.createRouter(LoginService::class.java).quicklyLogin(mViewModel,activity!!)
        }
    }

    override fun initData() {
        mViewModel.getData()
    }


    override fun startObserve() {
    }


}