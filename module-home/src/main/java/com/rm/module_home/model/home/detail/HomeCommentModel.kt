package com.rm.module_home.model.home.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 详情评论列表
 */
data class HomeCommentBean(
    @SerializedName("list")
    val list_comment: MutableList<CommentList>
): Serializable

data class CommentList(
    var id: Int,
    var content: String,
    var member_id: String,
    var created_at: Long,
    var is_liked: Boolean,
    var likes: Int,
    var member: Member,
    var is_own: Boolean
):Serializable

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
):Serializable