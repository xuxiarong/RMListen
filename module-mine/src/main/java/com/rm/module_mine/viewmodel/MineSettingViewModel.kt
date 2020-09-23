package com.rm.module_mine.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.helpter.loginOut
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser

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
     fun loginOutClick(){
          loginOut()
     }

}