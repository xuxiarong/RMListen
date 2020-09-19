package com.rm.module_listen.bean

import com.rm.business_lib.bean.AudioBean

data class ListenSheetCollectedBean(
    var list: List<ListenSheetCollectedDataBean>,
    var total: Long
) {
    fun getIndex(sheetId: String?): Int {
        list.forEachIndexed { index, bean ->
            if (bean.sheet_id.toString() == sheetId) {
                return index
            }
        }
        return -1
    }
}

data class ListenSheetCollectedDataBean(
    var audio_label: String,
    var audio_list: List<AudioBean>?,
    var audio_total: Int,
    var avatar_url: String,
    var member_id: Int,
    var member_name: String,
    var num_audio: Long,
    var num_favor: Int,
    var sheet_id: Long,
    var sheet_name: String
)
