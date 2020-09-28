package com.rm.business_lib.bean.download

data class DownloadFileBean(
    var pathUrl: String,
    var name: String
) : BaseDownloadFileBean {
    override val url = pathUrl
    override val fileName = name
}