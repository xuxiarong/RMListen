package com.rm.module_login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_login.activity.CountryListActivity
import com.rm.module_login.activity.LoginByVerifyCodeActivity
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.utils.LoginQuicklyDialogHelper

/**
 * desc   : login module 路由服务实现类
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_LOGIN_SERVICE)
class LoginServiceImpl : LoginService {
    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return LoginApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }

    override fun startLoginActivity(context: Context) {
        LoginByVerifyCodeActivity.startActivity(context)
    }

    override fun quicklyLogin(
        baseViewModel: BaseVMViewModel,
        fragmentActivity: FragmentActivity,
        loginSuccess: () -> Unit
    ) {
        val loginQuicklyDialogHelper = LoginQuicklyDialogHelper(baseViewModel, fragmentActivity)
        loginQuicklyDialogHelper.loginSuccess = loginSuccess
        loginQuicklyDialogHelper.show()
    }

    override fun quicklyLogin(baseViewModel: BaseVMViewModel, fragmentActivity: FragmentActivity) {
        LoginQuicklyDialogHelper(baseViewModel, fragmentActivity).show()
    }

    override fun startCountry(activity: Activity, code: Int) {
        CountryListActivity.newInstance(activity, code)
    }

    override fun startVerificationInput(context: Context, countryCode: String, phoneNumber: String, type: Int) {
        VerificationInputActivity.startActivity(context, countryCode, phoneNumber, type)
    }

}