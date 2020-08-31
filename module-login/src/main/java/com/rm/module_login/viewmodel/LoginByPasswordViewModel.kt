package com.rm.module_login.viewmodel

import android.app.Activity
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.ForgetPasswordActivity
import com.rm.module_login.activity.LoginByVerifyCodeActivity
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {

    // 手机输入框的ViewModel
    var phoneInputViewModel = PhoneInputViewModel()

    var context: Activity? = null

    //  输入的密码值
    var password = ObservableField<String>()

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    // 密码输入是否ok
    var isPasswordInputOk = ObservableField<Boolean>(false)

    // 密码是否显示按钮的资源id
    var eyesResId = ObservableField<Int>(R.drawable.login_ic_close_eyes)

    // 是否明文显示密码文本
    var isShowPasswordText = ObservableField<Boolean>(false)

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { inputChange() }

    private fun inputChange() {
        // 设置是否输入了文本
//        isPhoneInputOk.set(!TextUtils.isEmpty(phoneInputViewModel.phone.get()))
        isPasswordInputOk.set(!TextUtils.isEmpty(password.get()))
//        isInputText.set(isPhoneInputOk.get()!! && isPasswordInputOk.get()!!)
    }

    /**
     * 登陆
     */
    fun login() {
        // todo 登陆
        if (!isCheck.get()!!) {
            // 未选中check box
            ToastUtil.show(context, context!!.resources.getString(R.string.login_agree_deal_tips))
            return
        }

    }

    /**
     * 密码显示按钮点击事件
     */
    fun eyesClick() {
        if (eyesResId.get() == R.drawable.login_ic_close_eyes) {
            // 显示密码
            eyesResId.set(R.drawable.login_ic_open_eyes)
            isShowPasswordText.set(true)
        } else {
            // 隐藏密码
            eyesResId.set(R.drawable.login_ic_close_eyes)
            isShowPasswordText.set(false)
        }
    }

    /**
     * 忘记密码
     */
    fun forgetPassword() {
        ForgetPasswordActivity.startActivity(context!!)
    }

    /**
     * 验证码登陆
     */
    fun loginByVerifyCode() {
        LoginByVerifyCodeActivity.startActivity(context!!)
        context!!.finish()
    }
}