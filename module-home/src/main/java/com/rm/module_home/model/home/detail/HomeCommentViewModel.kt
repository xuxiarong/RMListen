package com.rm.module_home.model.home.detail

import java.io.Serializable

/**
 * 详情评论列表
 */
data class HomeCommentViewModel(
    val list: List<CommentList>
): Serializable

data class CommentList(
    val content: String,
    val created_at: Int,
    val id: Long,
    val liked: Boolean,
    val likes: Int,
    val member: Member,
    val member_id: String,
    val own: Boolean
):Serializable

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
):Serializable