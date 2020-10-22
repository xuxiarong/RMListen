package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
data class MineMemberListBean(
    var list: MutableList<MineMemberListDetailBean>
)

data class MineMemberListDetailBean(
    var audio: MineAudioBean,//音频
    var audio_id: String,
    var comment_id: String,
    var content: String,//评论内容
    var created_at: String,//评论时间
    var id: String,
    var likes: String,
    var member: MineMemberBean,//评论者
    var member_id: String,//评论者ID
    var topped: String,
    var updated_at: String
)

data class MineAudioBean(
    var cover_url: String,//音频封面链接
    var id: String,//音频ID
    var name: String,//音频名称
    var type_text: Int//音频类型
)

data class MineMemberBean(
    var avatar_url: String,//评论者头像链接
    var nickname: String//评论者昵称
)