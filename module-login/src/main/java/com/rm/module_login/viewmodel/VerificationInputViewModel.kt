package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.bean.LoginInfo
import com.rm.module_login.repository.LoginRepository

/**
 * desc   : 验证码输入ViewModel
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    // 获取验证码的类型
    var getCodeType = 0

    // 发送验证码的手机号码
    var phone = ""
    var phoneStr = ObservableField<String>()

    // 倒计时时间
    var countDownTime: Int = 60
    var countDownTimeStr = ObservableField<String>()

    // 重新获取
    var reGetCodeStr = ObservableField<String>()

    // 监听绑定输入框内容变化
    var completeInput: (String) -> Unit = {
        completeInput(it)
    }

    var loginResultBean = ObservableField<LoginInfo>()

    private fun completeInput(content: String) {
        // 验证码输入完成
        // todo 输入验证码完成，网络验证输入验证码是否正确
        DLog.i("llj", "完成-----inputVerifyCode---->>>${content}")
        if (getCodeType == 0) {
            // 登录类型
            launchOnIO {
                repository.loginByVerifyCode(phone, content).checkResult(
                    onSuccess = {
                        loginResultBean.set(it)
                        DLog.i("llj", "登陆成功！！access---->>>${it}")
                    },
                    onError = {
                        DLog.e("llj", "登陆失败！！--->>>$it")
                    })
            }


        } else if (getCodeType == 1) {
            // 重置密码类型
        }

    }

    /**
     * 重新获取验证码
     */
    fun reGetVerifyCode() {
        // todo
        DLog.i("llj", "重新获取验证码")
    }
}