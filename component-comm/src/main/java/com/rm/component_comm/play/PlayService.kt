package com.rm.component_comm.play

import android.view.View
import android.widget.FrameLayout
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : play module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface PlayService : ApplicationProvider {
    fun getGlobalPlay():View
    fun showView()
}