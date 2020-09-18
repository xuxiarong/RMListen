package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.helpter.loginIn
import com.rm.module_login.R
import com.rm.module_login.activity.ResetPasswordActivity
import com.rm.module_login.repository.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

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
    var countDownTime = ObservableField(-1)

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
                        loginIn(it.access,it.refresh,it.member)

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
                        // 开始倒计时
                        startCountDown()
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
                        // 开始倒计时
                        startCountDown()
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


    /**
     * 开始倒计时
     */
    fun startCountDown() {
        countDownTime.set(60)
        launchOnUI {
            countDown().collect {
                if (it >= 0) {
                    countDownTime.set(it - 1)
                }
            }
        }
    }
    private fun countDown(): Flow<Int> = flow {
        while (countDownTime.get()!! > 0) {
            delay(1000)
            emit(countDownTime.get()!!)
        }
    }
}