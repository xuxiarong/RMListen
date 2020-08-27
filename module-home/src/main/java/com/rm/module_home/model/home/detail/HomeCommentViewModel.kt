package com.rm.module_home.model.home.detail

/**
 * 评论列表
 */
data class HomeCommentViewModel(
    val list: List<CommentList>
)

data class CommentList(
    val content: String,
    val created_at: Int,
    val id: Long,
    val liked: Boolean,
    val likes: Int,
    val member: Member,
    val member_id: String,
    val own: Boolean
)

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
)