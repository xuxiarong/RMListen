package com.rm.component_comm.play

import android.content.Context
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : play module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface PlayService : ApplicationProvider {

    /**
     * 跳转播放器页面
     */
    fun startPlayActivity(
        context: Context,
        audioId: String = "",
        audioInfo: DownloadAudio = DownloadAudio(),
        chapterId: String = "",
        chapterList: MutableList<DownloadChapter> = mutableListOf(),
        currentDuration : Long = 0L,
        sortType: String = AudioSortType.SORT_ASC
    )

    /**
     * 评论中心跳转
     */
    fun toCommentCenterActivity(
        context: Context,
        audioID: String
    )

    /**
     * 暂停播放
     */

    fun pausePlay()

    /**
     * 请求焦点
     */
    fun requestAudioFocus()

    /**
     * 初始化播放服务
     */
    fun initPlayService(context: Context)

    /**
     * 继续上一次播放
     */
    fun continueLastPlay(playChapter : DownloadChapter,playList : MutableList<DownloadChapter>)

}