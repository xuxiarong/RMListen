package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import android.view.Gravity
import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonFragmentDialog
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.String
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
import com.rm.module_login.bean.LoginDialogModel
import com.rm.module_login.databinding.LoginActivityLoginByVerifyCodeBinding
import com.rm.module_login.databinding.LoginDialongLoginStatusBinding
import com.rm.module_login.viewmodel.LoginByVerifyViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_verify_code.*

/**
 * desc   : 验证码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyCodeActivity :
    BaseVMActivity<LoginActivityLoginByVerifyCodeBinding, LoginByVerifyViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByVerifyCodeActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_verify_code

    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        mViewModel.callBackToActivity = { callBackFromViewModel(it) }
    }

    override fun startObserve() {
        mViewModel.testDialogData.observe(this, Observer {
            dialogAdapter.setList(mViewModel.testDialogData.value!!)
        })
    }

    private val dialogAdapter by lazy {
        CommonBindAdapter<LoginDialogModel>(
            mutableListOf(),
            R.layout.login_dialong_item_login,
            BR.item
        )
    }

    private lateinit var loginDialog: CommonMvFragmentDialog
    private lateinit var commonDialog: CommonFragmentDialog

    override fun onResume() {
        super.onResume()
        /**
         *  mViewModel ：与Dialog相关的ViewModel，建议使用该dialog依赖的Activity的ViewModel
         *  R.layout.login_dialong_login_status ： dialog的布局文件
         *  BR.viewModel ： 布局文件中使用的viewModel变量的名字
         */
        loginDialog = CommonMvFragmentDialog(
            mViewModel,
            R.layout.login_dialong_login_status,
            BR.viewModel
        ).apply {
            this.dialogBackgroundColor = R.color.businessColorPrimary
            this.gravity = Gravity.BOTTOM
            this.dialogHasBackground = true
            this.dialogWidthIsMatchParent = true
            this.initDialogDataRef = {
                initDialogView()
            }
        }.showCommonDialog(this)

//        commonDialog = CommonFragmentDialog(R.layout.login_dialong_login_common).
//        apply {
//            this.gravity = Gravity.BOTTOM
//            this.dialogHasBackground = true
//            this.dialogWidthIsMatchParent = true
//            }
//        commonDialog.showCommonDialog(this)

    }

    fun initDialogView() {
        val bind = loginDialog.mDataBind as LoginDialongLoginStatusBinding?
        bind?.loginDialogRv?.bindVerticalLayout(dialogAdapter)
        mViewModel.getDialogData()
    }

    override fun initData() {
//        mViewModel.countryCode.set("+86")
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(
            login_by_verify_code_tips,
            resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "用户协议")
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
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }

    /**
     * 所有需要ViewModel回调到界面处理的地方
     * @param status Int
     */
    private fun callBackFromViewModel(status: Int) {
        when (status) {
            0 -> ToastUtil.show(
                this@LoginByVerifyCodeActivity, String(R.string.login_agree_deal_tips)
            )

            1 -> ToastUtil.show(
                this@LoginByVerifyCodeActivity, String(R.string.login_input_right_number_tips)
            )

            2 -> {
                // 获取验证码成功
                ToastUtil.show(
                    this@LoginByVerifyCodeActivity, String(R.string.login_send_success)
                )
                // 跳转到登陆验证码输入界面
                VerificationInputActivity.startActivity(
                    this,
                    mViewModel.phoneInputViewModel.phone.get().toString(),
                    TYPE_LOGIN
                )
            }

            3 -> {
                // 密码登陆
                LoginByPasswordActivity.startActivity(this)
                finish()
            }

        }
    }
}