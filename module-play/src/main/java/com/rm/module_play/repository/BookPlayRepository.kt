package com.rm.module_play.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_play.api.PlayApiService
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.test.ResultData
import com.rm.module_play.test.SearchMusicData
import com.rm.module_play.test.SearchResult
import com.rm.module_play.test.SearchResultInfo

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

    //评论
    suspend fun commentAudioComments(
        audioID: String,
        page: Int,
        pageSize: Int
    ): BaseResult<AudioCommentsModel> {
        return apiCall { playApi.commentAudioComments(audioID, page, pageSize) }
    }

    //评论
    suspend fun playerReport(audio_id: String, chapter_id: String): BaseResult<Any> {
        return apiCall { playApi.playerReport("player", audio_id, chapter_id) }
    }


}