package com.rm.module_play

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.util.setOnClickNotDoubleListener
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_play.activity.BookPlayerActivity
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_PLAY_SERVICE)
class PlayServiceImpl : PlayService {

    override fun getGlobalPlay(): View = GlobalplayHelp.instance.globalView
    override fun showView(context: Context) {
        GlobalplayHelp.instance.globalView.mainShow()
        GlobalplayHelp.instance.globalView.setOnClickNotDoubleListener {
            BookPlayerActivity.startActivity(context, BookPlayerActivity.fromGlobal)
        }
    }

    override fun toPlayPage(
        context: Context,
        bean: DetailBookBean?,
        index: Int
    ) {
        BookPlayerActivity.startActivity(context, bean, index)
    }

    override fun toPlayPage(context: Context, chapterId: String, audioId: String) {
        BookPlayerActivity.startActivity(context, chapterId, audioId)

    }

    override fun queryPlayBookList(): List<HistoryPlayBook>? =
        DaoUtil(HistoryPlayBook::class.java, "").queryAll()

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PlayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }
}