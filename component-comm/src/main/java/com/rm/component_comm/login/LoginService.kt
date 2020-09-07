package com.rm.component_comm.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rm.component_comm.login.bean.LoginUserBean
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Login module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface LoginService : ApplicationProvider {
    /**
     * 跳转到登陆界面
     * @param context Context
     */
    fun startLoginActivity(context: Context)

    /**
     * 获取此对象通过MutableLiveData方式可监听登陆状态的变化
     * @return MutableLiveData<Boolean>
     */
    fun getLoginStatusBean():MutableLiveData<Boolean>

    /**
     * 获取登陆用户的信息，也可通过次对象监听登陆用户信息状态的变化
     * @return MutableLiveData<LoginUserBean>
     */
    fun getLoginUserBean():MutableLiveData<LoginUserBean>
}