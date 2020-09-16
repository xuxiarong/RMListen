package com.rm.component_comm.play

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : play module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface PlayService : ApplicationProvider {
    //获取全局播放器按钮
    fun getGlobalPlay():View
    //显示播放器按钮
    fun showView()
    //跳著到播放器页面
    fun toPlayPage(context: Context)
}