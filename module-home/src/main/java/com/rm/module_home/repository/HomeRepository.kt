package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.*
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.CategoryTabListBean
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.model.home.HomeModel
import com.rm.module_home.model.home.detail.HomeCommentBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeRepository(private val homeService: HomeApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getHomeData(): BaseResult<HomeModel> {
        return apiCall { homeService.getHomeData() }
    }

    /**
     * 获取听书详情
     */
    suspend fun getDetailInfo(id: String): BaseResult<HomeDetailBean> {
        return apiCall { homeService.homeDetail(id) }
    }

    /**
     * 评论列表
     */
    suspend fun getCommentInfo(
        audio_id: String,
        page: Int,
        page_size: Int
    ): BaseResult<HomeCommentBean> {
        return apiCall { homeService.homeDetailComment(audio_id, page, page_size) }
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
        return apiCall { homeService.chapterList(id, page, page_size, sort) }
    }

    /**
     * 订阅
     */
    suspend fun subscribe(audioId: String): BaseResult<Any> {
        return apiCall { homeService.homeAddSubscription(audioId) }
    }

    /**
     * 取消订阅
     */
    suspend fun unSubscribe(audioId: String): BaseResult<Any> {
        return apiCall { homeService.homeCancelSubscription(audioId) }
    }

    suspend fun getTabList(): BaseResult<CategoryTabListBean> {
        return apiCall { homeService.getBoutiqueTabList() }
    }

    suspend fun getBoutiqueRecommendInfoList(
        classId: Int,
        page: Int,
        pageSize: Int = 10
    ): BaseResult<AudioListBean> {
        return apiCall { homeService.getCategoryList(classId, page, pageSize) }
    }

    /**
     * 获取听单详情
     */
    suspend fun sheet(): BaseResult<MenuSheetBean> {
        return apiCall { homeService.homeSheet() }
    }

    /**
     * 获取听单列表
     */
    suspend fun getSheetList(page: Int, pageSize: Int): BaseResult<SheetListBean> {
        return apiCall { homeService.homeSheetList(page, pageSize) }
    }


    /**
     * 获取专题列表数据
     * @param page_id Int
     * @param block_id Int
     * @param topic_id Int
     * @param page Int
     * @param page_size Int
     * @return BaseResult<AudioListBean>
     */
    suspend fun getTopicList(
        page_id: Int,
        block_id: Int,
        topic_id: Int,
        page: Int,
        page_size: Int
    ): BaseResult<AudioListBean> {
        return apiCall { homeService.homeTopicList(page_id, block_id, topic_id, page, page_size) }
    }

    /**
     * 榜单
     */
    suspend fun getTopList(
        rankType: String,
        rankSeg: String,
        page: Int,
        pageSize: Int
    ): BaseResult<HomeTopListBean> {
        return apiCall { homeService.homeTopList(rankType, rankSeg, page, pageSize) }
    }

    /**
     * 听单详情
     */
    suspend fun getData(sheetId: String): BaseResult<SheetDetailInfoBean> {
        return apiCall { homeService.homeSheetInfo(sheetId) }
    }

    /**
     * 听单音频列表
     */
    suspend fun getAudioList(
        sheetId: String,
        page: Int,
        page_size: Int
    ): BaseResult<AudioListBean> {
        return apiCall { homeService.homeAudioList( sheetId, page, page_size) }
    }

    /**
     * 收藏听单
     * @param sheetId String
     */
    suspend fun favoritesSheet(sheetId: String): BaseResult<Any> {
        return apiCall { homeService.homeFavoritesSheet(sheetId) }
    }

    /**
     * 取消收藏
     * @param sheetId String
     */
    suspend fun unFavoritesSheet(sheetId: String): BaseResult<Any> {
        return apiCall { homeService.homeUnFavoriteSheet(sheetId) }
    }

    /**
     * 关注主播接口
     * @param follow_id String
     */
    suspend fun attentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { homeService.homeAttentionAnchor(follow_id) }
    }

    /**
     * 取消关注主播接口
     * @param follow_id String
     */
    suspend fun unAttentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { homeService.homeUnAttentionAnchor(follow_id) }
    }

    /**
     * 发表评论(听书详情页)
     * @param content 评论内容
     * @param audio_id 音频ID
     * @param anchor_id 音频主播ID
     */
    suspend fun homeSendComment(
        content: String,
        audio_id: String,
        anchor_id: String
    ): BaseResult<Any> {
        return apiCall { homeService.homeSendComment(content, audio_id, anchor_id) }
    }

    /**
     * 评论点赞
     * @param comment_id 评论ID
     */
    suspend fun homeLikeComment(comment_id: String): BaseResult<Any> {
        return apiCall { homeService.homeLikeComment(comment_id) }
    }

    /**
     * 取消点赞
     * @param comment_id 评论ID
     */
    suspend fun homeUnLikeComment(comment_id: String): BaseResult<Any> {
        return apiCall { homeService.homeUnLikeComment(comment_id) }
    }

}