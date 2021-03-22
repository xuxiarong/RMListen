package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.*
import com.rm.business_lib.helpter.loginIn
import com.rm.module_login.R
import com.rm.module_login.activity.ResetPasswordActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_FORGET_PWD
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGOUT
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_RESET_PWD
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_CHANGE_PWD
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_CLOSE_ACCOUNT
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_FORGET_PWD
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_LOGIN
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
    var codeType = ObservableInt(0)

    // 发送验证码的手机号码
    var phone = ""
    var phoneStr = ObservableField<String>()

    // 国家区号代码
    var countryCode = "+86"

    // 倒计时时间
    var countDownTime = ObservableField(-1)

    //是否清空输入
    var inputClear = ObservableField(false)

    val errorTips = ObservableField<String>("")

    // 监听绑定输入框内容变化
    var completeInput: (String) -> Unit = {
        inputClear.set(false)
        completeInput(it)
    }

    /**
     * 输入完成
     */
    private fun completeInput(content: String) {
        // 验证码输入完成
        showLoading()
        when (codeType.get()) {
            TYPE_LOGIN -> {
                // 登录类型,验证码登陆
                loginByVerifyCode(content)
            }
            TYPE_RESET_PWD -> {
                // 重置密码类型，校验验证码是否正确
                verificationCode(CODE_TYPE_CHANGE_PWD, content)
            }
            TYPE_FORGET_PWD -> {
                verificationCode(CODE_TYPE_FORGET_PWD, content)
            }
            TYPE_LOGOUT -> {
                logout(content)
            }
        }

    }

    /**
     * 验证码登陆
     */
    private fun loginByVerifyCode(content: String) {
        launchOnIO {
            repository.loginByVerifyCode(countryCode, phone, content).checkResult(
                onSuccess = {
                    // 设置登陆成功数据
                    loginIn(it.access, it.refresh, it.member)
                    // 登陆成功
                    showTip(CONTEXT.getString(R.string.login_success))
                    showContentView()
                    finish()

                },
                onError = { it, _ ->
                    inputClear.set(true)
                    showContentView()
                    errorTips.set(it)
                })
        }
    }

    /**
     * 校验验证码是否正确
     */
    private fun verificationCode(type: String, content: String) {
        launchOnIO {
            repository.validateForgetPasswordVerifyCode(type, countryCode, phone, content)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        // 验证码校验正确
                        startActivity(
                            ResetPasswordActivity::class.java,
                            ResetPasswordActivity.getIntent(content, countryCode, phone, type)
                        )
                        finish()
                    },
                    onError = { it, _ ->
                        inputClear.set(true)
                        showContentView()
                        errorTips.set(it)
                    }
                )
        }
    }

    /**
     * 注销用户
     */
    private fun logout(code: String) {
        launchOnIO {
            repository.logout(code).checkResult(onSuccess = {
                showContentView()
                loginOut()
            }, onError = { it, _ ->
                inputClear.set(true)
                showContentView()
                errorTips.set(it)
            })
        }
    }

    /**
     * 登出
     */
    private fun loginOut() {
        // 保存登陆信息到本地
        ACCESS_TOKEN.putMMKV("")
        REFRESH_TOKEN.putMMKV("")
        LOGIN_USER_INFO.putMMKV("")
        ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(0)

        // 改变当前是否用户登陆状态 和 登陆的用户信息
        isLogin.set(false)
        loginUser.set(null)
        finish()
    }

    /**
     * 重新获取验证码
     */
    fun reGetVerifyCode() {
        showLoading()
        when (codeType.get()) {
            TYPE_LOGIN -> {
                sendCodeMessage(CODE_TYPE_LOGIN)
            }
            TYPE_RESET_PWD -> {
                // 重新获取重置重设密码的验证码
                sendCodeMessage(CODE_TYPE_CHANGE_PWD)
            }
            TYPE_LOGOUT -> {
                sendCodeMessage(CODE_TYPE_CLOSE_ACCOUNT)
            }
            TYPE_FORGET_PWD -> {
                sendCodeMessage(CODE_TYPE_FORGET_PWD)
            }
        }
    }

    /**
     * 获取验证码
     */
    private fun sendCodeMessage(type: String) {
        launchOnIO {
            repository.sendMessage(type, countryCode, phone).checkResult(
                onSuccess = {
                    showTip(CONTEXT.getString(R.string.login_send_success))
                    // 开始倒计时
                    startCountDown()
                    showContentView()
                },
                onError = { it, _ ->
                    showTip(
                        "$it",
                        R.color.business_color_ff5e5e
                    )
                    showContentView()
                }
            )
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