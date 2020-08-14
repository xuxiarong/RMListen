package com.rm.component_comm.interceptor

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.navigateTo

/**
 *
 * @ClassName: LoginNavigationCallbackImpl
 * @Description: 登陆拦截器回调
 * @Author: 鲸鱼
 * @CreateDate: 8/13/20 4:58 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/13/20 4:58 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
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
        navigateTo(ConstantsARouter.Mine.F_LOGIN_PATH)
    }

    override fun onArrival(postcard: Postcard?) {
        //跳转成功了
    }
}