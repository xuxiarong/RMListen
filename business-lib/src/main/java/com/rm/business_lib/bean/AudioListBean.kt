package com.rm.business_lib.bean

import com.rm.business_lib.db.download.DownloadAudio

data class AudioListBean(
    var list: MutableList<DownloadAudio>?,
    var total: Int,
    var block_name : String = ""
)
