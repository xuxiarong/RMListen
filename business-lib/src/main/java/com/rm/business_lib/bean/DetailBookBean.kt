package com.rm.business_lib.bean

import java.io.Serializable

/**
 *
 * @des:
 * @data: 9/28/20 6:47 PM
 * @Version: 1.0.0
 */
data class DetailBookBean(
    val audio_id: String,
    val audio_name: String,
    val original_name: String,
    val author: String="",
    val audio_cover_url: String,
    val anchor: Anchor = Anchor.getDefault(),
    val chapter_list: MutableList<ChapterList>? = null
) : Serializable