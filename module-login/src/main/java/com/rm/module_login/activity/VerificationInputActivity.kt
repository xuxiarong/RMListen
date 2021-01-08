package com.rm.module_login.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityVerificationCodeInputBinding
import com.rm.module_login.viewmodel.VerificationInputViewModel


/**
 * desc   : 验证码输入界面
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputActivity :
    BaseVMActivity<LoginActivityVerificationCodeInputBinding, VerificationInputViewModel>() {

    companion object {
        // 登录类型
        const val TYPE_LOGIN = 0

        // 重置密码类型
        const val TYPE_RESET_PWD = 1

        //注销用户
        const val TYPE_LOGOUT = 2

        // 忘记密码
        const val TYPE_FORGET_PWD = 3

        fun getIntent(countryCode: String, phoneNumber: String, type: Int): HashMap<String, Any> {
            return hashMapOf(
                Pair("countryCode", countryCode),
                Pair("phone", phoneNumber),
                Pair("type", type)
            )
        }

        fun startActivity(activity: Activity, countryCode: String, phoneNumber: String, type: Int) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    VerificationInputActivity::class.java
                ).apply {
                    putExtra("countryCode", countryCode)
                    putExtra("phone", phoneNumber)
                    putExtra("type", type)
                }, 1007
            )
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_verification_code_input

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) // 设置默认键盘弹出
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
        mViewModel.codeType.set(intent.getIntExtra("type", 0))
        mViewModel.countryCode = intent.getStringExtra("countryCode") ?: "+86"
    }

    override fun initData() {
        val phone = intent.getStringExtra("phone") as String
        mViewModel.phone = phone
        var hidePhone = phone
        hidePhone = if (phone.length == 11) {
            phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        } else {
            phone.replace(
                mViewModel.phone.substring(
                    3,
                    7
                ), "****"
            )
        }
        mViewModel.phoneStr.set(
            resources.getString(
                R.string.login_format_verification_phone_tips,
                mViewModel.countryCode + " " + hidePhone
            )
        )

        // 开始倒计时
        mViewModel.startCountDown()
    }

    override fun finish() {
        if (mViewModel.codeType.get() == TYPE_LOGOUT) {
            setResult(1001)
        }
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.countDownTime.set(0)
    }

}

