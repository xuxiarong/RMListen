package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.LoginUserBean

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoViewModel : BaseVMViewModel() {

     val userInfo = ObservableField<LoginUserBean>()
     val mIsLogin = ObservableField<Boolean>()

}