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
}