package com.rm.module_home

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.fragment.HomeHomeFragment

/**
 * desc   : Home module 路由实现接口
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_HOME_SERVICE)
class HomeServiceImpl : HomeService {
    /**
     * 跳转到听单页面
     */
    override fun startHomeMenuActivity(context: Context) {
        HomeMenuActivity.startActivity(context)
    }

    override fun getHomeFragment(): HomeHomeFragment {
        return HomeHomeFragment()
    }

    /**
     * 跳转到home听单详情
     */
    override fun startHomeSheetDetailActivity(context: Activity, sheetId: String, pageId: Int) {
        HomeMenuDetailActivity.startActivity(context, sheetId, pageId)
    }

    override fun toDetailActivity(context: Context, audioID: String) {
        HomeDetailActivity.startActivity(context, audioID)
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return HomeApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}