package com.rm.component_comm.home

import android.content.Context
import androidx.fragment.app.Fragment
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Home工程module路由服务接口
 * date   : 2020/08/12
 * version: 1.0
 */
interface HomeService : ApplicationProvider {
    /**
     * 跳转到听单界面
     * @param context Context
     */
    fun startHomeMenuActivity(context: Context)

    /**
     * 跳转到听单详情
     * @param sheetId 听单ID
     * @param pageId 页面Id
     */
    fun startHomeSheetDetailActivity(context: Context, sheetId: String, pageId: Int)

    // 获取主页Fragment
    fun getHomeFragment(): Fragment
}