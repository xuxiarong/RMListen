package debug

import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_login.R
import com.rm.module_login.activity.ForgetPasswordActivity
import com.rm.module_login.activity.LoginByPasswordActivity
import com.rm.module_login.activity.LoginByVerifyCodeActivity
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
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
            VerificationInputActivity.startActivity(this, "15139492334", TYPE_LOGIN)
        }

        btnForget.setOnClickListener {
            ForgetPasswordActivity.startActivity(this)
        }
    }

    override fun initData() {
    }
}