package com.rm.business_lib.bean.download

data class DownloadFileBean(
    var pathUrl: String,
    var chapterName: String,
    var audioName : String
) : BaseDownloadFileBean {
    override val url = pathUrl
    override val fileName = chapterName
    override val parentFileDir = audioName

}