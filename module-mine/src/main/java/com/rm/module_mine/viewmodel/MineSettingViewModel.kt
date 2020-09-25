package com.rm.module_mine.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
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
    fun loginOutClick() {
        loginOut()
    }

    /**
     * 个人资料
     */
    fun clickSettingPersonal() {
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