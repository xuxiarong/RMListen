package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity
import com.rm.module_mine.activity.MinePersonalInfoActivity
import com.rm.module_mine.activity.MineSettingActivity
import com.rm.module_mine.adapter.MineHomeAdapter
import com.rm.module_mine.bean.MineHomeBean
import com.rm.module_mine.bean.MineHomeDetailBean

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineAboutViewModel : BaseVMViewModel() {


    private fun quicklyLogin(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(this, it)
        }
    }
}