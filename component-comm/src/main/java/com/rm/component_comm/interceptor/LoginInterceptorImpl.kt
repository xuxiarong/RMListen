package com.rm.component_comm.interceptor

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.IS_LOGIN

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
@Interceptor(name = "login", priority = 6)
class LoginInterceptorImpl : IInterceptor {
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val path: String = postcard?.path ?: ""
        val bundle = postcard?.extras
        val isLogin = bundle?.getBoolean(IS_LOGIN)
        if (isLogin == true) { // 如果已经登录不拦截
            callback?.onContinue(postcard)
        } else {  // 如果没有登录
            when (path) {
                ConstantsARouter.Mine.F_TEST,
                ConstantsARouter.Mine.F_LOGIN_PATH -> callback?.onContinue(postcard)
                else -> callback?.onInterrupt(null)
            }
        }
    }

    override fun init(context: Context?) {
        //此方法只会走一次
        Log.e("LoginInterceptorImpl", "路由登录拦截器初始化成功")
    }

}