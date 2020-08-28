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

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var context: Activity? = null

    // 标识是否输入了文本
    var isInputText = ObservableField<Boolean>()

    //  输入的电话号码值
    var phone = ObservableField<String>()

    //  输入的密码值
    var password = ObservableField<String>()

    // 国家代码具体值
    var countryCode = ObservableField<String>()

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    // 手机号码输入是否ok
    var isPhoneInputOk = ObservableField<Boolean>(false)

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
        isPhoneInputOk.set(!TextUtils.isEmpty(phone.get()))
        isPasswordInputOk.set(!TextUtils.isEmpty(password.get()))
        isInputText.set(isPhoneInputOk.get()!! && isPasswordInputOk.get()!!)
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

    // 置空输入框的值
    fun clearInput() {
        phone.set("")
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

    /**
     * 显示国家列表选择框
     */
    fun showCountryList() {
        // todo
    }
}