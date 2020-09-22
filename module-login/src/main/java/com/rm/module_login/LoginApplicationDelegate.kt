package com.rm.module_login

import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getObjectMMKV
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Login 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class LoginApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG, "Module Login onCreate()!!!")
        loadKoinModules(loginModules)
        // 初始化登陆相关信息
        initLogin()
    }

    override fun onTerminate() {
        DLog.d(TAG, "Module Login onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG, "Module Login onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG, "Module Login onTrimMemory(),---level--->>>$level")
    }

    /**
     * 初始化登陆相关信息
     */
    private fun initLogin() {
        val loginUserInfo = LOGIN_USER_INFO.getObjectMMKV(LoginUserBean::class.java, null)
        DLog.i("llj","刚进入app初始化获取到的登陆用户信息------>>>$loginUserInfo")
        isLogin.set(loginUserInfo != null)
        loginUser.set(loginUserInfo)
    }
}