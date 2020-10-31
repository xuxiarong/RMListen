package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityLoginByPassowrdBinding
import com.rm.module_login.utils.CountryListDialogHelper
import com.rm.module_login.viewmodel.LoginByPasswordViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_passowrd.*
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*

/**
 * desc   : 密码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordActivity :
    BaseVMActivity<LoginActivityLoginByPassowrdBinding, LoginByPasswordViewModel>() {

    companion object {
        fun startActivity(context: Context, inputPhone: String) {
            context.startActivity(
                Intent(
                    context,
                    LoginByPasswordActivity::class.java
                ).apply { putExtra("phone", inputPhone) })
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_passowrd

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        setTransparentStatusBar()
        login_include_phone_input_country_code.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }

        login_include_phone_input_arrow_view.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }
    }

    override fun initData() {

        mViewModel.phoneInputViewModel.phone.set(intent.getStringExtra("phone"))

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
}