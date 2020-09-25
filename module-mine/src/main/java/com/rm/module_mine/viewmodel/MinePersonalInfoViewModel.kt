package com.rm.module_mine.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.loginUser
import com.rm.module_mine.activity.MineNicknameSettingActivity
import com.rm.module_mine.activity.MinePersonalSignatureSettingActivity

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoViewModel : BaseVMViewModel() {
    val userInfo = loginUser

    /**
     * 昵称点击事件
     */
    fun clickNickname() {
        startActivity(MineNicknameSettingActivity::class.java)
    }

    /**
     * 性别点击事件
     */
    fun clickSex() {}

    /**
     * 地址点击事件
     */
    fun clickAddress() {}

    /**
     * 生日点击事件
     */
    fun clickBirthday() {}

    /**
     * 个性签名点击事件
     */
    fun clickPersonalSignature() {
        startActivity(MinePersonalSignatureSettingActivity::class.java)
    }
}