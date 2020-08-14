package com.rm.module_play

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.play.PlayService

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ConstantsARouter.Play.PATH_PLAY_SERVICE)
class PlayServiceImpl : PlayService {
    override fun init(context: Context?) {
    }
}