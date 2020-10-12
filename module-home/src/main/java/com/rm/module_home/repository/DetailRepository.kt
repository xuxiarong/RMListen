package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.model.home.detail.*

class DetailRepository(val homeservice: HomeApiService) : BaseRepository() {

    suspend fun getDetailInfo(id: String): BaseResult<HomeDetailModel> {
        return apiCall { homeservice.homeDetail(id) }
    }

    /**
     * 评论列表
     */
    suspend fun getCommentInfo(audio_id: String,page: Int,page_size: Int): BaseResult<HomeCommentViewModel> {
        return apiCall { homeservice.HomeDetail_Comment(audio_id,page,page_size) }
    }


    /**
     * 章节列表
     */
    suspend fun chapterList(id: String ,page:Int ,page_size:Int ,sort:String): BaseResult<AudioChapterListModel> {
        return apiCall { homeservice.chapterList(id, page, page_size, sort) }
    }

    /**
     * 订阅听单
     */
    suspend fun subscribe(audioId: String): BaseResult<Any> {
        return apiCall { homeservice.homeAddSubscription(audioId) }
    }
    /**
     * 关注直播
     */

    /**
     * 取消
     */
}