package com.rm.module_mine.bean

import java.io.Serializable


/**
 * 用户/主播详情信息
 */
data class MineInfoDetail(
    var id:String,
    var nickname:String,
    var member_intro : String,
    var fans :String,
    var follows :String ,
    var avatar_url :String ,
    var is_followed : String ,
    var member_type :String  //1普通用户，2主播
):Serializable

/**
 * 主页发布书籍/听单/收藏听单列表
 */
data class MineInfoProfile(
    var member_type : String,
    var total:String

):Serializable
