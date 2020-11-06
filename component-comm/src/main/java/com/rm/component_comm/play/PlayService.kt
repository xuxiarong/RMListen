package com.rm.component_comm.play

import android.content.Context
import android.view.View
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : play module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface PlayService : ApplicationProvider {
    //获取全局播放器按钮
    fun getGlobalPlay(): View

    //显示播放器按钮
    fun showView(context: Context)

    fun onGlobalPlayClick(context: Context)

    //跳转到播放器页面
    fun toPlayPage(
        context: Context, bean: DetailBookBean, from: String, sortType: String
    )

    //从播放历史和最近播放进入播放页面
    fun toPlayPage(
        context: Context, chapterId: String, audioId: String, from: String
    )

    //通过传入章节进来
    fun toPlayPage(context: Context, book: ChapterList, from: String, sortType: String)

    //通过订阅
    fun toPlayPage(
        context: Context,
        book: MutableList<ChapterList>,
        from: String,
        chapterId: String
    )

    //通过本地下载列表跳转过来
    fun toPlayPage(context: Context, audio: DownloadAudio, chapterId: String, from: String)

    //播放历史
    fun queryPlayBookList(): List<HistoryPlayBook>?

    /**
     * 评论中心跳转
     */
    fun toCommentCenterActivity(
        context: Context,
        audioID: String
    )
}