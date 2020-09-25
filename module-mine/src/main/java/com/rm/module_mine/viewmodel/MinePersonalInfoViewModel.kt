package com.rm.module_mine.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.loginUser

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoViewModel : BaseVMViewModel() {
     val userInfo = loginUser
}