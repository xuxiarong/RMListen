package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
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

        fun getIntent(countryCode: String, phoneNumber: String, type: Int): HashMap<String, Any> {
            return hashMapOf(
                Pair("countryCode", countryCode),
                Pair("phone", phoneNumber),
                Pair("type", type)
            )
        }

        fun startActivity(context: Context, countryCode: String, phoneNumber: String, type: Int) {
            context.startActivity(Intent(context, VerificationInputActivity::class.java).apply {
                putExtra("countryCode", countryCode)
                putExtra("phone", phoneNumber)
                putExtra("type", type)
            })
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_verification_code_input

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
        mViewModel.getCodeType = intent.getIntExtra("type", 0)
        mViewModel.countryCode = intent.getStringExtra("countryCode")
    }

    override fun initData() {
        mViewModel.phone = intent.getStringExtra("phone") as String
        mViewModel.phoneStr.set(
            resources.getString(
                R.string.login_format_verification_phone_tips,
                mViewModel.countryCode + " " + mViewModel.phone.replace(
                    mViewModel.phone.substring(
                        3,
                        7
                    ), "****"
                )
            )
        )

        // 开始倒计时
        mViewModel.startCountDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.countDownTime.set(0)
    }
}

