package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineAccountSecuritySettingActivity
import com.rm.module_mine.activity.MineDownloadSettingActivity
import com.rm.module_mine.activity.MinePersonalInfoActivity
import com.rm.module_mine.activity.MinePlaySettingActivity

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineSettingViewModel : BaseVMViewModel() {

    val userInfo = loginUser
    val mIsLogin = isLogin

    /**
     * 登出
     */
    fun loginOutClick(context: Context) {
        TipsFragmentDialog().apply {
            contentText = context.String(R.string.mine_login_out_tips)
            leftBtnText = context.String(R.string.mine_login_out_sure)
            rightBtnText = context.String(R.string.mine_login_out_cancel)
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                loginOut()
                finish()
            }
            rightBtnClick = {
                dismiss()
            }
        }.show(context as FragmentActivity)

    }

    /**
     * 个人资料
     */
    fun clickSettingPersonal(context: Context) {
        if (!isLogin.get()) {
            RouterHelper.createRouter(LoginService::class.java)
                .quicklyLogin(this, context as FragmentActivity)
            return
        }
        startActivity(MinePersonalInfoActivity::class.java)
    }

    /**
     * 播放设置
     */
    fun clickPlaySetting() {
        startActivity(MinePlaySettingActivity::class.java)
    }

    /**
     * 下载设置
     */
    fun clickDownloadSetting() {
        startActivity(MineDownloadSettingActivity::class.java)
    }

    /**
     * 账号安全设置
     */
    fun clickAccountSecurity() {
        startActivity(MineAccountSecuritySettingActivity::class.java)
    }

}