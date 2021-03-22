package com.rm.module_login.viewmodel.dialog

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.helpter.loginIn
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_login.R
import com.rm.module_login.api.LoginApiService
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_LOGIN
import com.rm.module_login.viewmodel.view.PhoneInputViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * desc   : 快速登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginQuicklyViewModel : BaseVMViewModel() {
    private val repository: LoginRepository = LoginRepository(
        BusinessRetrofitClient().getService(
            LoginApiService::class.java
        )
    )

    // 输入电话号码部分
    // 输入电话model
    var phoneInputViewModel = PhoneInputViewModel()

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    // 标识是否显示手机输入框，true = 显示手机输入框界面，false = 显示验证码输入界面
    var isShowPhoneInputLay = ObservableField<Boolean>(true)


    // 输入验证码部分
    // 区号 + 带有 "****" 的手机字符
    var formatPhone = ObservableField<String>("")

    // 倒计时时间
    var countDownTime = ObservableField(-1)

    //是否清空输入
    var inputClear = ObservableField(false)
    val inputNeedAnim = ObservableField(true)


    // 监听绑定输入框内容变化
    var completeInput: (String) -> Unit = {
        inputClear.set(false)
        completeInput(it)
    }

    // 是否登陆成功的标识
    var isLoginSuccess = MutableLiveData<Boolean>(false)

    /**
     * 获取验证码
     */
    fun getCode() {
        // 网络通过手机获取验证码
        if (!isCheck.get()!!) {
            // 未选中check box
            showToast(R.string.login_agree_deal_tips)
            return
        }
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            showToast(R.string.login_input_right_number_tips)
            return
        }

        // 发送登陆短信验证码
        launchOnIO {
            repository.sendMessage(
                CODE_TYPE_LOGIN,
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    showToast(R.string.login_send_success)
                    formatPhone.set(
                        "${phoneInputViewModel.countryCode.get()!!} ${
                            phoneInputViewModel.phone.get()!!
                                .replace(phoneInputViewModel.phone.get()!!.substring(3, 7), "****")
                        }"
                    )
                    // 输入验证码界面切换
                    isShowPhoneInputLay.set(false)
                    startCountDown()
                },
                onError = { it, _ ->
                    showContentView()
                    inputNeedAnim.set(true)
                    inputClear.set(true)
                    errorTips.set(it)
                }
            )
        }
    }

    fun back() {
        // 切换回获取验证码界面
        countDownTime.set(0)
        countDownTime.notifyChange()

        inputNeedAnim.set(false)
        inputNeedAnim.notifyChange()

        inputClear.set(true)
        inputClear.notifyChange()
        isShowPhoneInputLay.set(true)
    }


    /**
     * 重置状态
     */
    fun clear() {
        isShowPhoneInputLay.set(true)
        phoneInputViewModel.phone.set("")
        isCheck.set(false)
        countDownTime.set(0)
        errorTips.set("")
        isLoginSuccess.value = false
    }


    private fun completeInput(content: String) {
        // 验证码输入完成
        showLoading()
        // 登录类型,验证码登陆
        launchOnIO {
            repository.loginByVerifyCode(
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!,
                content
            ).checkResult(
                onSuccess = {
                    // 设置登陆成功数据
                    loginIn(it.access, it.refresh, it.member)

                    // 登陆成功
                    showToast(R.string.login_success)
                    showContentView()
                    isLoginSuccess.value = true

                },
                onError = { it, _ ->
                    inputNeedAnim.set(true)
                    inputClear.set(true)
                    showContentView()
                    it?.let { showToast(it) }
                })
        }

    }

    /**
     * 重新获取验证码
     */
    fun reGetVerifyCode() {
        showLoading()
        // 重新获取登陆验证码
        launchOnIO {
            repository.sendMessage(
                CODE_TYPE_LOGIN,
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            )
                .checkResult(
                    onSuccess = {
                        showToast(R.string.login_send_success)
                        startCountDown()
                        showContentView()
                    },
                    onError = { _, _ ->
                        showToast(R.string.login_send_failed)
                        showContentView()
                    }
                )
        }
    }


    /**
     * 开始倒计时
     */
    private fun startCountDown() {
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