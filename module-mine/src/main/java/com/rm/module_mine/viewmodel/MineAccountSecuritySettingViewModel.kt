package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineAccountSecuritySettingViewModel(private val repository: MineRepository) :
    BaseVMViewModel() {
    /**
     * 修改密码
     */
    fun clickChangePsdFun(context: Context) {
        loginUser.get()?.let {
            sendMessage(context, "change_pwd", it.area_code!!, it.account!!, 1)
        }
    }

    private fun sendMessage(
        context: Context,
        codeType: String,
        countryCode: String,
        phone: String,
        type: Int
    ) {
        showLoading()
        // 获取短信验证码
        launchOnIO {
            repository.sendMessage(
                codeType,
                countryCode,
                phone
            ).checkResult(
                onSuccess = {
                    showContentView()
                    getActivity(context)?.let {
                        RouterHelper.createRouter(LoginService::class.java)
                            .startVerificationInput(it, countryCode, phone, type)
                    }
                },
                onError = { it, _ ->
                    showContentView()
                    it?.let { showTip(it, R.color.business_color_ff5e5e) }
                }
            )
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
            if (loginUser.get()?.member_type == 2) {
                TipsFragmentDialog().apply {
                    titleText = context.String(R.string.business_tips)
                    contentText = context.getString(R.string.mine_member_not_logout)
                    leftBtnText = context.String(R.string.business_sure)
                    leftBtnClick = {
                        dismiss()
                    }
                }.show(activity)
            } else {
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
                            sendMessage(context, "close_account", it.area_code!!, it.account!!, 2)
                            dismiss()
                        }
                    }
                }.show(activity)
            }
        }
    }
}