package com.rm.module_mine.bean

import java.io.Serializable


/**
 * 用户/主播详情信息
 */
data class MineInfoDetail(
    var id:String,
    var nickname:String,//昵称
    var member_intro : String,//简介
    var fans :Int,//粉丝数
    var follows :Int ,//关注数
    var avatar_url :String ,//头像链接
    var is_followed : Boolean ,//当前用户是否关注了对方
    var member_type :Int  //1普通用户，2主播
):Serializable



