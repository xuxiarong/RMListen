package com.rm.module_login.viewmodel.view

import androidx.databinding.ObservableField
import com.rm.module_login.R

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
class PasswordInputViewModel {
    //  输入的密码值
    var password = ObservableField<String>("")

    // 密码输入是否ok
    var isPasswordInputOk = ObservableField<Boolean>(false)

    // 密码是否显示按钮的资源id
    var eyesResId = ObservableField<Int>(R.drawable.login_ic_close_eyes)

    // 是否明文显示密码文本
    var isShowPasswordText = ObservableField<Boolean>(false)

    // 密码的正则表达式
//    private val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}\$")
    // 密码的正则表达式(至少包含字母/数字/特殊字符 其中两种类型组合)
    private val passwordRegex = Regex("(?!^\\d+\$)(?!^[A-Za-z]+\$)(?!^[^A-Za-z0-9]+\$)(?!^.*[\\u4E00-\\u9FA5].*\$)^\\S{8,16}\$")

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { inputChange() }

    private fun inputChange() {
        // 输入密码是否ok
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
}