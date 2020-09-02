package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.String
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityLoginByPassowrdBinding
import com.rm.module_login.viewmodel.LoginByPasswordViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_passowrd.*

/**
 * desc   : 密码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordActivity :
    BaseVMActivity<LoginActivityLoginByPassowrdBinding, LoginByPasswordViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByPasswordActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_passowrd

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.callBackToActivity = { callBackFromViewModel(it) }
    }

    override fun initData() {
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(
            login_by_password_tips,
            resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByPasswordActivity, "用户协议")
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_privacy),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByPasswordActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }

    private fun callBackFromViewModel(flag: Int, params: String = "") {
        when (flag) {
            0 -> ToastUtil.show(this, String(R.string.login_agree_deal_tips)) // 未同意协议

            1 -> ForgetPasswordActivity.startActivity(this) // 忘记密码

            2 -> {
                // 验证码登陆
                LoginByVerifyCodeActivity.startActivity(this)
                finish()
            }

            3 -> {
                // 登陆成功
                ToastUtil.show(this, String(R.string.login_success))
                finish()
            }

        }
    }
}