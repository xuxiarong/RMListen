package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePlaySettingViewModel : BaseVMViewModel() {

    val userInfo = loginUser
    val mIsLogin = isLogin

}