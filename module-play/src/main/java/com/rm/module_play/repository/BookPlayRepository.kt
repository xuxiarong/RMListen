package com.rm.module_play.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.HomeDetailBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.module_play.api.PlayApiService
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.test.SearchMusicData
import com.rm.module_play.test.SearchResult

/**
 *
 * @des:
 * @data: 9/3/20 5:47 PM
 * @Version: 1.0.0
 */
class BookPlayRepository(val playApi: PlayApiService) : BaseRepository() {

    /**
     * 发送忘记密码短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun getBookList(params: Map<String, String>): BaseResult<SearchResult> {
        return apiCall { playApi.getBookList(params) }
    }

    suspend fun getPlayPath(params: Map<String, String>): BaseResult<SearchMusicData> {
        return apiCall { playApi.getPlayPath(params) }
    }

    //获取用户评论列表
    suspend fun getMemberComments(page: Int, pageSize: Int): BaseResult<Any> {
        return apiCall { playApi.getMemberComments(page, pageSize) }
    }

    //点赞和取消点赞
    suspend fun likeComment(commentID: Int): BaseResult<Any> {
        return apiCall { playApi.likeComment(commentID) }
    }

    /**
     * 获取书本详情
     */
    suspend fun getDetailInfo(id: String): BaseResult<HomeDetailBean> {
        return apiCall { playApi.homeDetail(id) }
    }
    //评论
    suspend fun commentAudioComments(
        audioID: String,
        page: Int,
        pageSize: Int
    ): BaseResult<AudioCommentsModel> {
        return apiCall { playApi.commentAudioComments(audioID, page, pageSize) }
    }

    //上报
    suspend fun playerReport(audio_id: String, chapter_id: String): BaseResult<Any> {
        return apiCall { playApi.playerReport("player", audio_id, chapter_id) }
    }

    /**
     * 章节列表
     */
    suspend fun chapterList(
        id: String,
        page: Int,
        page_size: Int,
        sort: String
    ): BaseResult<AudioChapterListModel> {
        return apiCall { playApi.chapterList(id, page, page_size, sort) }
    }

    /**
     * 返回
     */
    suspend fun chapterPageList(
        audioId: String,
        chapterId: String,
        sort: String
    ): BaseResult<AudioChapterListModel> {
        return apiCall { playApi.chapterPage(audioId, chapterId,sort) }
    }

    var daoUtil: DaoUtil<HistoryPlayBook, Long>? = null

    init {
        daoUtil = DaoUtil(
            HistoryPlayBook::class.java, 0L
        )
    }

    /**
     * 记录播放的章节
     */
    fun insertPlayBook(historyPlayBook: HistoryPlayBook) {
        val history = daoUtil?.querySingle(historyPlayBook.audio_id)
        if (history == null) {
            daoUtil?.save(historyPlayBook)
        }


    }

    /**
     * 记录播放的章节
     */
    fun updatePlayBook(chapter: ChapterList) {
        val historyPlayBook = daoUtil?.querySingle(chapter.audio_id.toLong())

        val chapterFind = historyPlayBook?.listBean?.find { it.chapter_id == chapter?.chapter_id }
        if (chapterFind == null) {
            historyPlayBook?.listBean?.add(chapter)
        } else {
            chapterFind.recentPlay = System.currentTimeMillis()
        }
        historyPlayBook?.let { daoUtil?.update(it) }
    }
    /**
     * 记录播放的章节
     */
    fun updatePlayBookProcess( chapter: ChapterList,progress:Long=0L) {
        val historyPlayBook = daoUtil?.querySingle(chapter.audio_id.toLong())
        val chapterFind = historyPlayBook?.listBean?.find { it.chapter_id == chapter?.chapter_id }
        if (chapterFind == null) {
            historyPlayBook?.listBean?.add(chapter)
        } else {
            chapterFind.progress=progress
        }
        historyPlayBook?.let { daoUtil?.update(it) }
    }
    /**
     * 查询
     */
    fun queryPlayBookList(): List<HistoryPlayBook>? = daoUtil?.queryAll()
}