package com.rm.module_search.bean

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
data class SearchHotRecommendBean(val cate_name: String,//tab名称，热搜，历史等
                                  val list: MutableList<SearchHotDetailBean>)

data class SearchHotDetailBean(
    val audio_id: String,//书籍ID
    val audio_name: String,//书籍名
    val level_up: String,//升降类型；0:不变；1:升；2:降
    val play_count: Int//播放量
)