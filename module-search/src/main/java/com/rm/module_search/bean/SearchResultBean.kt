package com.rm.module_search.bean

import com.rm.business_lib.bean.AudioBean

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
data class SearchResultBean(
    var audio: Long,//搜索到的书籍总量
    var audio_list: List<AudioBean>,//书籍列表
    var member: Long,//搜索到的主播数量
    var member_list: List<MemberBean>,//主播列表
    var sheet: Long,//搜索到的听单数量
    var sheet_list: List<SearchSheetBean>//听单列表
)

data class MemberBean(
    var avatar: String,//头像
    var avatar_url: String,//头像url
    var fans: String,//粉丝数
    var is_follow: Long,//是否已经关注，0:未关注；1:已经关注，未登陆默认返回0
    var level: Long,//等级
    var member_id: String,//主播用户id
    var nickname: String,//主播名
    var member_intro: String//简介
)

data class SearchSheetBean(
    var audio_num: Long,//书籍数量
    var created_at: String,//创建时间
    var favour_num: Int,//收藏人数
    var sheet_cover: String,//封面
    var sheet_id: String,//听单id
    var sheet_name: String//名称
)