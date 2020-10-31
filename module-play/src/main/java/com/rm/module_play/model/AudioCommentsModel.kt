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
    var audio_id: String,
    var comment_id: String,
    var content: String,
    var created_at: String,
    var id: String,
    var is_liked: Boolean,
    var is_own: Boolean,
    var likes: Int,
    var member: Member,
    var member_id: String
)

data class Member(
    val avatar_url: String,
    val id: String,
    val nickname: String
)