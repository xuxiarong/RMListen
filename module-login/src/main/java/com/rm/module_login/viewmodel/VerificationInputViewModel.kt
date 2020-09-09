package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.ResetPasswordActivity
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.utils.loginIn

/**
 * desc   : 验证码输入ViewModel
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    // 获取验证码的类型,0 = 登陆类型，1=重置密码类型
    var getCodeType = 0

    // 发送验证码的手机号码
    var phone = ""
    var phoneStr = ObservableField<String>()

    // 国家区号代码
    var countryCode = "+86"

    // 倒计时时间
    var countDownTime: Int = 60
    var countDownTimeStr = ObservableField<String>("")

    // 重新获取
    var reGetCodeStr = ObservableField<String>("")

    // 监听绑定输入框内容变化
    var completeInput: (String) -> Unit = {
        completeInput(it)
    }

    private fun completeInput(content: String) {
        // 验证码输入完成
        showLoading()
        if (getCodeType == 0) {
            // 登录类型,验证码登陆
            launchOnIO {
                repository.loginByVerifyCode(countryCode, phone, content).checkResult(
                    onSuccess = {
                        // 设置登陆成功数据
                        loginIn(it)

                        // 登陆成功
                        showToast(R.string.login_success)
                        showContentView()
                        finish()

                    },
                    onError = {
                        showContentView()
                        it?.let { showToast(it) }
                    })
            }


        } else if (getCodeType == 1) {
            // 重置密码类型，校验验证码是否正确
            launchOnIO {
                repository.validateForgetPasswordVerifyCode(countryCode, phone, content)
                    .checkResult(
                        onSuccess = {
                            showContentView()
                            if (it.result) {
                                // 验证码校验正确
                                startActivity(
                                    ResetPasswordActivity::class.java,
                                    ResetPasswordActivity.getIntent(content, countryCode, phone)
                                )
                                finish()
                            } else {
                                // 验证码错误
                                showToast(R.string.login_verify_wrong)
                            }
                        },
                        onError = {
                            showContentView()
                            it?.let { showToast(it) }
                        }
                    )
            }
        }

    }

    /**
     * 重新获取验证码
     */
    fun reGetVerifyCode() {
        showLoading()
        if (getCodeType == 0) {
            // 重新获取登陆验证码
            launchOnIO {
                repository.sendLoginVerifyCode(countryCode, phone).checkResult(
                    onSuccess = {
                        showToast(R.string.login_send_success)
                        countDownTime = 60
                        reGetCodeStr.set("")
                        showContentView()
                    },
                    onError = {
                        showToast(R.string.login_send_failed)
                        showContentView()
                    }
                )
            }
        } else if (getCodeType == 1) {
            // 重新获取重置重设密码的验证码
            launchOnIO {
                repository.sendForgetPasswordVerifyCode(countryCode, phone).checkResult(
                    onSuccess = {
                        showToast(R.string.login_send_success)
                        countDownTime = 60
                        reGetCodeStr.set("")
                        showContentView()
                    },
                    onError = {
                        showToast(R.string.login_send_failed)
                        showContentView()
                    }
                )
            }
        }
    }
}