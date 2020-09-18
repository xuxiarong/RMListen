package com.rm.business_lib

import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * desc   : businessApplication
 * date   : 2020/09/18
 * version: 1.0
 */
open class BusinessApplication : BaseApplication() {
    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.base_activity_bg_color) //全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.base_activity_bg_color) //全局设置主题颜色
            layout.setEnableLoadMoreWhenContentNotFull(false)
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableLoadMore(true)
            ClassicsFooter(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BusinessApplication)
        }
        Cxt.context = CONTEXT
    }
}