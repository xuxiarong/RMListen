package com.rm.business_lib.bean.download

/**
 * 音频文件下载bean
 * audioUrl:音频url
 * bookId: 所属书籍id
 * audioName:存储文件名称
 * bookName:所属书籍名称
 */

data class DownloadAudioBean(
    var audioUrl: String,
    var bookId: String,
    var chapter_id : String,
    var audioName: String,
    var bookName: String,
    var fileSize: Long
) : BaseDownloadFileBean {
    override val url = audioUrl
    override val fileName = audioName
    override val parentFileDir = bookId
}