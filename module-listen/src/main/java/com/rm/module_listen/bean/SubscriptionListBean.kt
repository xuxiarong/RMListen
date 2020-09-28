package com.rm.module_listen.bean

data class SubscriptionListBean(
    var audio_cover: String?,//封面
    var audio_label: String?,//角标
    var audio_name: String?,//书籍名称
    var audio_id: Long?,//书籍id
    var chapter_name: String?,//最新章节名
    var created_at: String?,//发布时间
    var progress: Int,//1未开播,2连载中,3已完结
    var is_top: Int,//1: 已置顶；0:否
    var unread: Int,//未阅读的章节数
    var sequence: Int,//当前最新章节的集数
    var cover_url: String,//封面url
    var last_unread: Int//上一次请求未读数
) {
    fun getProgressStr(): String {
        return when (progress) {
            1 -> {
                "【未开播】"
            }
            2 -> {
                "【连载中】"
            }
            3 -> {
                "【已完结】"
            }
            else -> {
                ""
            }
        }
    }

    fun getUnreadStr(): String {
        return (unread - last_unread).toString()
    }

}