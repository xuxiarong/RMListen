package com.rm.module_home.model.home.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 详情评论列表
 */
data class HomeCommentViewModel(
    @SerializedName("list")
    val list_comment: MutableList<CommentList>
): Serializable

data class CommentList(
    val id: Int,
    val content: String,
    val member_id: String,
    val created_at: Long,
    val is_liked: Boolean,
    val likes: Int,
    val member: Member,
    val is_own: Boolean
):Serializable

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
):Serializable