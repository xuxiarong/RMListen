package com.rm.music_exoplayer_lib

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.router.ARouterModuleServicePath


/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_MUSIC_SERVICE)
class MusicServiceImpl : MusicService {

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return MusicApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }
}