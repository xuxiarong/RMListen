package com.rm.module_login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.login.bean.LoginUserBean
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_login.activity.LoginByVerifyCodeActivity

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

    override fun getLoginStatusBean(): MutableLiveData<Boolean> {
        return LoginConstants.isLogin
    }

    override fun getLoginUserBean(): MutableLiveData<LoginUserBean> {
        return LoginConstants.loginUser
    }

}