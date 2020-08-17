package com.rm.component_comm.interceptor

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.rm.component_comm.navigateTo
import com.rm.component_comm.router.ConstantsARouter

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
open class LoginNavigationCallbackImpl : NavigationCallback {
    override fun onLost(postcard: Postcard?) {
        //找到了
    }

    override fun onFound(postcard: Postcard?) {
        //找不到了
    }

    override fun onInterrupt(postcard: Postcard?) {
        //拦截进行登陆
        navigateTo(ConstantsARouter.F_LOGIN_PATH)
    }

    override fun onArrival(postcard: Postcard?) {
        //跳转成功了
    }
}