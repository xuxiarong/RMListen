package com.rm.module_play

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_play.activity.BookPlayerActivity
import com.rm.module_play.playview.GlobalPlayHelper

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_PLAY_SERVICE)
class PlayServiceImpl : PlayService {

    override fun getGlobalPlay(): View = GlobalPlayHelper.INSTANCE.globalView
    override fun showView(context: Context) {
//        GlobalPlayHelper.INSTANCE.globalView.mainShow()
//        GlobalPlayHelper.INSTANCE.globalView.setOnClickNotDoubleListener {
//            BookPlayerActivity.startActivity(context, Jump.DOTS.from)
//        }
    }

    override fun onGlobalPlayClick(context: Context) {
//        BookPlayerActivity.startActivity(context, Jump.DOTS.from)
    }

    override fun startPlayActivity(
        context: Context,
        audioId: String,
        audioInfo: DownloadAudio,
        chapterId: String,
        chapterList: MutableList<DownloadChapter>,
        sortType: String
    ) {
        BookPlayerActivity.startPlayActivity(
            context = context,
            audioId = audioId,
            audioModel = audioInfo,
            chapterId = chapterId,
            chapterList = chapterList,
            sortType = sortType
        )
    }

    override fun queryPlayBookList(): List<ListenAudioEntity>? =
        DaoUtil(ListenAudioEntity::class.java, "").queryAll()

    override fun toCommentCenterActivity(context: Context, audioID: String) {
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PlayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }
}