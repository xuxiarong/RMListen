package com.rm.component_comm.play

import android.content.Context
import android.view.View
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
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

    fun startPlayActivity(
        context: Context,
        audioId: String = "",
        audioInfo: DownloadAudio = DownloadAudio(),
        chapterId: String = "",
        chapterList: MutableList<DownloadChapter> = mutableListOf(),
        chapterProgress : Int = 0,
        sortType: String = AudioSortType.SORT_ASC
    )

    //播放历史
    fun queryPlayBookList(): List<ListenAudioEntity>?

    /**
     * 评论中心跳转
     */
    fun toCommentCenterActivity(
        context: Context,
        audioID: String
    )
}