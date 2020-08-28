package com.rm.module_login.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.repository.LoginRepository

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    //  输入的密码值
    var password = ObservableField<String>()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    // 密码输入框内容是否为空
    var isInputTextEmpty = ObservableField<Boolean>(true)

    // 密码输入是否ok
    var isPasswordInputOk = ObservableField<Boolean>(false)

    // 密码是否显示按钮的资源id
    var eyesResId = ObservableField<Int>(R.drawable.login_ic_close_eyes)

    // 是否明文显示密码文本
    var isShowPasswordText = ObservableField<Boolean>(false)

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { inputChange() }

    // 密码的正则表达式
    private val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}\$")

    private fun inputChange() {
        isInputTextEmpty.set(TextUtils.isEmpty(password.get()!!))
        isPasswordInputOk.set(passwordRegex.matches(password.get()!!))
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
     * 修改密码
     */
    fun modify() {
        // todo 网络请求，修改密码
    }
}