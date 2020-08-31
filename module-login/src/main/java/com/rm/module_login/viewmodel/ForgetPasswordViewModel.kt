package com.rm.module_login.viewmodel

import android.app.Activity
import androidx.databinding.ObservableField
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_RESET_PWD
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {

    // 手机输入框的ViewModel
    var phoneInputViewModel = PhoneInputViewModel()


    var context: Activity? = null

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 获取验证码
     */
    fun getCode() {
        // toto 网络通过手机获取验证码
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            ToastUtil.show(
                context,
                context!!.resources.getString(R.string.login_input_right_number_tips)
            )
            return
        }
        VerificationInputActivity.startActivity(
            context!!,
            phoneInputViewModel.phone.get().toString(),
            TYPE_RESET_PWD
        )
    }
}