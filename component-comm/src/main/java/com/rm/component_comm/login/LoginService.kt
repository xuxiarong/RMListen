package com.rm.component_comm.login

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
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
     * @param fragmentActivity FragmentActivity
     */
    fun quicklyLogin(
        fragmentActivity: FragmentActivity,
        loginSuccess: () -> Unit
    )

    fun quicklyLogin(
        fragmentActivity: FragmentActivity
    )

    /**
     * 跳转国家选择节目
     */
    fun startCountry(activity: Activity, code: Int)

    /**
     * 输入验证码节目
     */
    fun startVerificationInput(
        activity: Activity,
        countryCode: String,
        phoneNumber: String,
        type: Int
    )
}