package com.rm.module_play

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_play.activity.BookPlayerActivity
import com.rm.module_play.activity.CommentCenterActivity
import com.rm.module_play.enum.Jump
import com.rm.module_play.playview.GlobalplayHelp

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_PLAY_SERVICE)
class PlayServiceImpl : PlayService {

    override fun getGlobalPlay(): View = GlobalplayHelp.instance.globalView
    override fun showView(context: Context) {
//        GlobalplayHelp.instance.globalView.mainShow()
//        GlobalplayHelp.instance.globalView.setOnClickNotDoubleListener {
//            BookPlayerActivity.startActivity(context, Jump.DOTS.from)
//        }
    }

    override fun onGlobalPlayClick(context: Context) {
        BookPlayerActivity.startActivity(context, Jump.DOTS.from)
    }

    override fun toPlayPage(
        context: Context, bean: DetailBookBean, from: String, sortType: String
    ) {
        BookPlayerActivity.startActivity(context, bean, from,sortType)

    }


    override fun toPlayPage(context: Context, chapterId: String, audioId: String, from: String) {
        BookPlayerActivity.startActivity(context, chapterId, audioId, from)

    }

    override fun toPlayPage(context: Context, book: ChapterList, from: String,sortType: String) {
        BookPlayerActivity.startActivity(context, book, from,sortType)

    }

    override fun toPlayPage(
        context: Context,
        book: MutableList<ChapterList>,
        from: String,
        chapterId: String
    ) {
        BookPlayerActivity.startActivity(context, book, chapterId, from)
    }

    override fun toPlayPage(
        context: Context,
        audio: DownloadAudio,
        chapterId: String,
        from: String
    ) {
        BookPlayerActivity.startActivity(
            context = context,
            audio = audio,
            chapterId = chapterId,
            from = from
        )
    }


    override fun queryPlayBookList(): List<HistoryPlayBook>? =
        DaoUtil(HistoryPlayBook::class.java, "").queryAll()

    override fun toCommentCenterActivity(context: Context, audioID: String) {
        CommentCenterActivity.toCommentCenterActivity(context, audioID)
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PlayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {

    }
}