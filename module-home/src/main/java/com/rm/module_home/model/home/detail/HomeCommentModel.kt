package com.rm.module_home.model.home.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 详情评论列表
 */
data class HomeCommentBean(
    @SerializedName("list")
    val list_comment: MutableList<CommentList>?,
    val total: Int
) : Serializable

data class CommentList(
    var id: Int,
    var content: String,
    var member_id: String,
    var created_at: Long,
    var is_liked: Boolean,
    var likes: Int,//点赞数量
    var member: Member,//评论者
    var is_own: Boolean,//是否自己的评论
    var is_hot: Boolean,//是否为热门评论
    var topped: String//是否置顶评论，0否1是

) : Serializable

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
) : Serializable