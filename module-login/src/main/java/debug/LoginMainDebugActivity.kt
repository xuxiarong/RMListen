package debug

import com.rm.baselisten.debug.BaseDebugActivity
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
            ResetPasswordActivity.startActivity(this,"123456","13766669999")
        }
    }

    override fun initData() {
    }
}