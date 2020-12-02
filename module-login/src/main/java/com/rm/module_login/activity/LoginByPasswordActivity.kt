package com.rm.module_login.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.view.View
import com.rm.baselisten.binding.bindKeyboardVisibilityListener
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
        login_by_verify_code_input.bindKeyboardVisibilityListener {
            if (it) {
                startScaleAnim(layout_top_logo, 0.5f)
                val translation = -resources.getDimension(R.dimen.dp_30)
                startTranslationAnim(rootLayout, translation)
                startTranslationAnim(inputLayout, -resources.getDimension(R.dimen.dp_50))
            } else {
                startScaleAnim(layout_top_logo, 1f)
                startTranslationAnim(rootLayout, 0f)
                startTranslationAnim(inputLayout, 0f)
            }
        }
    }

    private fun startTranslationAnim(view: View, translation: Float) {
        val animator = ObjectAnimator.ofFloat(view, "translationY", translation)
        animator.duration = 200
        animator.start()
    }

    private fun startScaleAnim(view: View, scale: Float) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", scale)
        animator.duration = 200
        animator.start()

        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", scale)
        animator1.duration = 200
        animator1.start()
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