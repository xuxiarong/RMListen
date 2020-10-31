package debug

import android.widget.ImageView
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.baselisten.util.DLog
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.helpter.parseToken
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
            LoginByPasswordActivity.startActivity(this,"13888888888")
        }

        btnVerifyInput.setOnClickListener {
            VerificationInputActivity.startActivity(this, "+86", "13510753483", TYPE_RESET_PWD)
        }

        btnForget.setOnClickListener {
            ForgetPasswordActivity.startActivity(this,"")
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
                    DLog.i("llj", "左边按钮点击事件")
                    dismiss()
                }
                rightBtnClick = {
                    DLog.i("llj", "右边按钮点击事件")
                    dismiss()
                }
            }.show(this)
        }

        btnCustomTipsDialog.setOnClickListener {
            CustomTipsFragmentDialog().apply {
                titleText = "订阅成功"
                contentText = "可在“我听-订阅”中查看"
                leftBtnText = "我知道了"
                rightBtnText = "去看看"
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    DLog.i("llj", "左边按钮点击事件")
                    dismiss()
                }
                rightBtnClick = {
                    DLog.i("llj", "右边按钮点击事件")
                    dismiss()
                }
//                customView = View.inflate(this@LoginMainDebugActivity,R.layout.login_activity_login_by_verify_code,null)
                customView =
                    ImageView(this@LoginMainDebugActivity).apply { setImageResource(R.mipmap.login_ic_launcher) }
            }.show(this)
        }

        btnParseToken.setOnClickListener {
            parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDAzMzA4NDEsIm5iZiI6MTYwMDMzMDg0MSwiZXhwIjoxNjAyNzUwMDQxLCJqdGkiOiIxNzE4MDhlZGNiMTZmNjMzZWRjZDc2NzI0OWM1YTczNyIsInR5cCI6InJlZnJlc2giLCJ1aWQiOiIxNjUwOTY3ODQwMDYzMDc4NDAifQ.kubohSBhEzOK-SjRjLFf8DjDJIgjsSh2Rd7OaN5cIkM")
        }
    }

    override fun initData() {
    }
}