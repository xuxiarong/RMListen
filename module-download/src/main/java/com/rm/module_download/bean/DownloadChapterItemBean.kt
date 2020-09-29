package com.rm.module_download.bean

/**
 * 章节下载列表item bean
 */
data class DownloadChapterItemBean(
    val chapter_id: Long,//章节id
    val audio_id: String,   //书籍id
    val sequence: Int,   //章节序号
    val chapter_name: String,   //章节名称
    val size: Long,  //音频大小(单位:字节byte)
    val duration: Long,   //音频时长(单位:秒)
    val need_pay: Int,   //是否付费（预留）2 会员  1 付费 0 免费
    val amount: Double,  //金额(单位:元)
    val play_count: Long,   //播放次数
    val created_at: String,   //创建时间
    val path_url: String    //文件地址
)