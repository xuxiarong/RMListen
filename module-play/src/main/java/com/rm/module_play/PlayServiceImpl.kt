package com.rm.module_play

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.contains
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


    override fun getGlobalPlay(): View = GlobalplayHelp.instance.globalView
    override fun showView(){
        GlobalplayHelp.instance.globalView.play(R.drawable.business_defualt_img)
        GlobalplayHelp.instance.globalView.mainShow()
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PlayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }


}