package com.rm.component_comm.login

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
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
     * 快捷登陆dialog
     * @param baseViewModel BaseVMViewModel
     * @param fragmentActivity FragmentActivity
     */
    fun quicklyLogin(
        baseViewModel: BaseVMViewModel,
        fragmentActivity: FragmentActivity,
        loginSuccess: () -> Unit
    )

    fun quicklyLogin(
        baseViewModel: BaseVMViewModel,
        fragmentActivity: FragmentActivity
    )
}