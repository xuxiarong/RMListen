package com.rm.module_login.viewmodel

import android.app.Activity
import androidx.databinding.ObservableField
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.LoginByPasswordActivity
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var phoneInputViewModel = PhoneInputViewModel()
    var context: Activity? = null

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 获取验证码
     */
    fun getCode() {
        // toto 网络通过手机获取验证码
        DLog.i("llj", "获取验证码！！！isCheck---->>${isCheck.get()}")
        if (!isCheck.get()!!) {
            // 未选中check box
            ToastUtil.show(context, context!!.resources.getString(R.string.login_agree_deal_tips))
            return
        }
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
            TYPE_LOGIN
        )
    }

    /**
     * 密码登陆入口
     */
    fun loginByPassword() {
        LoginByPasswordActivity.startActivity(context!!)
        context!!.finish()
    }

}