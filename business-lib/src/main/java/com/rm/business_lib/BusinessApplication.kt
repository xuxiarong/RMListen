package com.rm.business_lib

import android.text.TextUtils
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.rm.baselisten.util.getLongMMKV
import com.rm.baselisten.util.getStringMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.db.DaoManager
import com.rm.business_lib.helpter.parseToken
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.net.api.BusinessApiService
import com.rm.business_lib.wedgit.smartrefresh.BaseLoadMoreFooter
import com.rm.business_lib.wedgit.smartrefresh.BaseRefreshHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            BaseRefreshHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.base_activity_bg_color) //全局设置主题颜色
            layout.setEnableLoadMoreWhenContentNotFull(false)
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableAutoLoadMore(true)
//            layout.setEnableLoadMore(true)
            BaseLoadMoreFooter(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }

    }

    private val apiService by lazy {
        BusinessRetrofitClient().getService(
            BusinessApiService::class.java
        )
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BusinessApplication)
        }
        Cxt.context = CONTEXT
        DaoManager.daoManager.initDaoManger()
        // 检测token是否过期
        checkToken()
    }


    /**
     * 检测访问token是否过期
     */
    private fun checkToken() {
        if (TextUtils.isEmpty(ACCESS_TOKEN.getStringMMKV())
            || TextUtils.isEmpty(REFRESH_TOKEN.getStringMMKV(""))
            || ACCESS_TOKEN_INVALID_TIMESTAMP.getLongMMKV(0) > System.currentTimeMillis() / 1000
        ) return // 证明未登陆 或者 访问令牌token还未过期


        // 到这里，说明刷新token已经过期了，则去刷新接口
        GlobalScope.launch(Dispatchers.IO) {
            apiService.refreshToken2(REFRESH_TOKEN.getStringMMKV("")).let {
                if (it.code != 0) return@launch // 刷新不成功，直接返回，不处理了
                ACCESS_TOKEN.putMMKV(it.data.access)
                REFRESH_TOKEN.putMMKV(it.data.refresh)
                // 更新访问token的过期时间
                ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(parseToken(it.data.access))
            }
        }
    }

    /**
     * 从数据库获取已下载书籍列表
     */
    private fun initAppData(){
        DownloadMemoryCache.getDownAudioOnAppCreate()
    }
}