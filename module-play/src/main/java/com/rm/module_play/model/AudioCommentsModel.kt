package com.rm.module_play.model

/**
 *
 * @des:
 * @data: 9/18/20 11:18 AM
 * @Version: 1.0.0
 */
data class AudioCommentsModel(
    val list: List<Comments>,
    val total: Int
)


data class Comments(
    var audio_id: String,
    var comment_id: String,
    var content: String,
    var created_at: String,
    var id: String,
    var is_liked: Boolean,
    var member_id: String,
    var likes: Int,//点赞数量
    var member: Member,//评论者
    var is_own: Boolean,//是否自己的评论
    var is_hot: Boolean,//是否为热门评论
    var topped: String//是否置顶评论，0否1是
)

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
)