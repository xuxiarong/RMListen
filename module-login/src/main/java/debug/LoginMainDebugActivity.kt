package debug

import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.baselisten.util.DLog
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.module_login.R
import com.rm.module_login.activity.*
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_RESET_PWD
import kotlinx.android.synthetic.main.login_activity_main.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class LoginMainDebugActivity : BaseDebugActivity() {
    override fun getLayoutResId(): Int = R.layout.login_activity_main

    override fun initView() {
        btnVerify.setOnClickListener {
            LoginByVerifyCodeActivity.startActivity(this)
        }

        btnPassword.setOnClickListener {
            LoginByPasswordActivity.startActivity(this)
        }

        btnVerifyInput.setOnClickListener {
            VerificationInputActivity.startActivity(this, "+86", "13510753483", TYPE_RESET_PWD)
        }

        btnForget.setOnClickListener {
            ForgetPasswordActivity.startActivity(this)
        }

        btnReset.setOnClickListener {
            ResetPasswordActivity.startActivity(this, "123456", "+86", "13766669999")
        }

        btnVMActivity.setOnClickListener {
            LoginDebugActivity.startActivity(this)
        }
        btnTipsDialog.setOnClickListener {
            TipsFragmentDialog().apply {
                titleText = "显示标题"
                contentText = "显示的提示内容"
                leftBtnText = "我知道了"
                rightBtnText = "去看看"
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    DLog.i("llj","左边按钮点击事件")
                    dismiss()
                }
                rightBtnClick = {
                    DLog.i("llj","右边按钮点击事件")
                    dismiss()
                }
            }.show(this)



//            TipsFragmentDialog().setTipsContent("测试的地方对方的放到").show(this)
        }
    }

    override fun initData() {
    }
}