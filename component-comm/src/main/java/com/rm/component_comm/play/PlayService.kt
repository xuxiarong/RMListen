package com.rm.component_comm.play

import android.content.Context
import android.view.View
import com.rm.business_lib.bean.BaseAudioModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.db.HistoryPlayBook
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

    fun startPlayActivity(
        context: Context,
        audioId: String = "",
        audioInfo: BaseAudioModel = BaseAudioModel(),
        chapterId: String = "",
        chapterList: List<ChapterList> = mutableListOf(),
        sortType: String = "scs"
    )

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