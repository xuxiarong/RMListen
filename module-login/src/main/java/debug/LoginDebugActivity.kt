package debug

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityVerificationCodeInputBinding
import com.rm.module_login.utils.LoginQuicklyDialogHelper
import kotlinx.android.synthetic.main.login_activity_debug.*

/**
 * desc   : 验证码输入界面
 * date   : 2020/08/27
 * version: 1.0
 */
class LoginDebugActivity :
    BaseVMActivity<LoginActivityVerificationCodeInputBinding, LoginDebugViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginDebugActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_debug

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        login_debug_quickly_login.setOnClickListener {
            LoginQuicklyDialogHelper(mViewModel, this).show()
        }
    }

    override fun initData() {
    }
}

