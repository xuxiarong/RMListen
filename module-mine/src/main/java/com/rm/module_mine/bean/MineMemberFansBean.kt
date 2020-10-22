package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
data class MineMemberFansBean(
    var list: List<MineMemberFansDetailBean>,
    var total: Int//粉丝总数
)

data class MineMemberFansDetailBean(
    var avatar: String,//用户头像路径
    var avatar_url: String,//用户头像URL
    var is_follow: Int,//当前登录用户是否关注该用户（0：未关注，1：已关注）
    var member_id: String,//用户ID
    var nickname: String,//用户昵称
    var signature: String//用户签名
)