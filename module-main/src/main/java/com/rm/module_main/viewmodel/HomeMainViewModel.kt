package com.rm.module_main.viewmodel

import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.PlayGlobalData
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.repository.MainRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeMainViewModel(private val repository: MainRepository) : BaseVMViewModel() {
    /**
     * 通过音频ID，章节ID获取指定的章节列表，该接口会返回一个用于下次分页的page字段，需要记录
     */
    fun getChapterListWithId(
        audioId: String,
        chapterId: String
    ) {
        PlayGlobalData.playNextPage = PlayGlobalData.PLAY_FIRST_PAGE
        PlayGlobalData.playNextPage = PlayGlobalData.PLAY_FIRST_PAGE
        PlayGlobalData.playChapterId.set(chapterId)
        PlayGlobalData.playAudioId.set(audioId)
        DLog.d("music-exoplayer-lib","首页获取章节列表 audioId = $audioId --- chapterId = $chapterId")
        launchOnIO {
            repository.chapterPageList(
                audioId = audioId,
                chapterId = chapterId,
                page_size = PlayGlobalData.playChapterPageSize,
                sort = AudioSortType.SORT_ASC
            ).checkResult(onSuccess = {
                DLog.d("music-exoplayer-lib","首页获取章节列表成功  onSuccess ")
                val chapterList = it.list
                PlayGlobalData.playNextPage = it.page
                PlayGlobalData.playPrePage = it.page
                showContentView()
                if (chapterList != null) {
                    PlayGlobalData.chapterRefreshModel.noMoreData.set(chapterList.size < PlayGlobalData.playChapterPageSize)
                    chapterList.forEach { chapter ->
                        if (chapter.chapter_id.toString() == chapterId) {
                            PlayGlobalData.initPlayChapter(chapter)
                            if (!PlayGlobalData.isSortAsc()) {
                                chapterList.reverse()
                            }
                            DLog.d("music-exoplayer-lib","首页获取章节列表成功  ${chapterList.size} ")
                            DLog.d("music-exoplayer-lib","首页设置公共数据 setPrePagePlayData")
                            PlayGlobalData.setNextPagePlayData(chapterList)
                            RouterHelper.createRouter(PlayService::class.java).continueLastPlay(chapter,chapterList)
                            return@forEach
                        }
                    }
                    PlayGlobalData.chapterRefreshModel.canRefresh.set(PlayGlobalData.playNextPage > PlayGlobalData.PLAY_FIRST_PAGE)
                    PlayGlobalData.playNextPage++
                } else {
                    PlayGlobalData.setNextPagePlayData(mutableListOf())
                }
            }, onError = {it,_->
                PlayGlobalData.chapterRefreshModel.finishLoadMore(false)
                showContentView()
                DLog.d("music-exoplayer-lib","首页获取章节列表失败   $it ")
            })
        }
    }

}