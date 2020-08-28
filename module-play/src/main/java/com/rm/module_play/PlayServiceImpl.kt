package com.rm.module_play

import android.content.Context
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_play.playview.GlobalplayHelp

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_PLAY_SERVICE)
class PlayServiceImpl : PlayService {

    override fun addGlobalPlay(viewParent: FrameLayout, layoutParams: FrameLayout.LayoutParams) {
        viewParent.addView(GlobalplayHelp.instance.globalView,layoutParams)
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PlayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }


}