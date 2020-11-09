package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.bean.Country
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R

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
                .startVerificationInput(context, it.area_code!!, it.account!!, 1)
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
    fun clickLogoutFun(context: Context) {
        getActivity(context)?.let { activity ->
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.mine_loginout_title)
                contentText = context.String(R.string.mine_loginout_tips)
                leftBtnText = context.String(R.string.mine_loginout_sure)
                rightBtnText = context.String(R.string.mine_loginout_cancel)
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    loginUser.get()?.let {
                        RouterHelper.createRouter(LoginService::class.java)
                            .startVerificationInput(context, it.area_code!!, it.account!!, 2)
                        finish()
                    }
                }
            }.show(activity)
        }
    }
}