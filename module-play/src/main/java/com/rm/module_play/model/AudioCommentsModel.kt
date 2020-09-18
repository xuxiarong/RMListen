package com.rm.module_play.model

/**
 *
 * @des:
 * @data: 9/18/20 11:18 AM
 * @Version: 1.0.0
 */
data class AudioCommentsModel(
    val list: List<Comments>
)


data class Comments(
    val audio_id: String,
    val comment_id: String,
    val content: String,
    val created_at: String,
    val id: String,
    val is_liked: Boolean,
    val is_own: Boolean,
    val likes: String,
    val member: Member,
    val member_id: String
)

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
)