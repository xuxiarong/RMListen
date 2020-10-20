package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.Country
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineAccountSecuritySettingViewModel : BaseVMViewModel() {
    /**
     * 修改密码
     */
    fun clickChangePsdFun(context: Context) {
        loginUser.get()?.let {
            RouterHelper.createRouter(LoginService::class.java)
                .startVerificationInput(context, it.area_code, it.account, 1)
        }
    }

    /**
     * 最近登陆设备
     */
    fun clickLoginDeviceFun() {

    }

    /**
     * 注销账号
     */
    fun clickLogoutFun() {}
}