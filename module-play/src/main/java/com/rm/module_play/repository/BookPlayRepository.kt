package com.rm.module_play.repository

import com.mei.orc.util.json.toJson
import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioDetailBean
import com.rm.business_lib.bean.AudioRecommendList
import com.rm.business_lib.bean.BusinessAdRequestModel
import com.rm.business_lib.bean.ChapterListModel
import com.rm.module_play.api.PlayApiService
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.model.PlayAdChapterModel
import com.rm.module_play.model.PlayAdResultModel
import com.rm.module_play.model.PlayFloorAdModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @des:
 * @data: 9/3/20 5:47 PM
 * @Version: 1.0.0
 */
class BookPlayRepository(private val playApi: PlayApiService) : BaseRepository() {


    /**
     * 获取书本详情
     */
    suspend fun getDetailInfo(id: String): BaseResult<AudioDetailBean> {
        return apiCall { playApi.homeDetail(id) }
    }

    suspend fun getAudioRecommend(
        audio_id: Long,
        page_size: Int = 5
    ): BaseResult<AudioRecommendList> {
        return apiCall { playApi.getAudioRecommend(audio_id, page_size) }
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
    ): BaseResult<ChapterListModel> {
        return apiCall { playApi.chapterList(id, page, page_size, sort) }
    }

    /**
     * 根据章节id获取章节列表和对应的page
     */
    suspend fun getChapterListWithId(
        audioId: String,
        page_size: Int,
        chapterId: String,
        sort: String
    ): BaseResult<ChapterListModel> {
        return apiCall {
            playApi.getChapterListWithId(
                audioId = audioId,
                chapterId = chapterId,
                page_size = page_size,
                sort = sort
            )
        }
    }

    /**
     * 评论点赞
     * @param comment_id 评论ID
     */
    suspend fun homeLikeComment(comment_id: String): BaseResult<Any> {
        return apiCall { playApi.homeLikeComment(comment_id) }
    }

    /**
     * 取消点赞
     * @param comment_id 评论ID
     */
    suspend fun homeUnLikeComment(comment_id: String): BaseResult<Any> {
        return apiCall { playApi.homeUnLikeComment(comment_id) }
    }


    /**
     * 关注主播接口
     * @param follow_id String
     */
    suspend fun attentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { playApi.homeAttentionAnchor(follow_id) }
    }

    /**
     * 取消关注主播接口
     * @param follow_id String
     */
    suspend fun unAttentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { playApi.homeUnAttentionAnchor(follow_id) }
    }

    /**
     * 订阅
     */
    suspend fun subscribe(audioId: String): BaseResult<Any> {
        return apiCall { playApi.homeAddSubscription(audioId) }
    }

    /**
     * 取消订阅
     */
    suspend fun unSubscribe(audioId: String): BaseResult<Any> {
        return apiCall { playApi.homeCancelSubscription(audioId) }
    }


    /**
     *  获取评论列表的广告
     */
    suspend fun getCommentAd(): BaseResult<PlayAdResultModel> {
        return apiCall {
            val requestBean = BusinessAdRequestModel(arrayOf("ad_player_comment"))
            playApi.getCommentAd(
                requestBean.toJson().toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaType())
            )
        }
    }

    /**
     *  获取章节的封面广告和音频流广告
     */
    suspend fun getChapterAd(): BaseResult<PlayAdChapterModel> {
        return apiCall {
            val requestBean =
                BusinessAdRequestModel(arrayOf("ad_player_voice", "ad_player_audio_cover"))
            playApi.getChapterAd(
                requestBean.toJson().toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaType())
            )
        }
    }

    /**
     *  获取楼层广告
     */
    suspend fun getAudioFloorAd(): BaseResult<PlayFloorAdModel> {
        return apiCall {
            val requestBean = BusinessAdRequestModel(arrayOf("ad_player_streamer","ad_player_audio_cover"))
            playApi.getAudioFloorAd(
                requestBean.toJson().toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaType())
            )
        }
    }


}