package com.rm.module_listen.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/24
 * version: 1.0
 */
data class ListenChapterList(
    val list: ArrayList<ListenAudioChapter>,
    val msg: String,
    val last_unread: Int,
    val total_unread:Int
)

data class ListenAudioChapter(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_name: String,
    val chapter_id: String,
    val cover_url: String,
    val duration: Long,
    val path: String,
    val play_count: Int,
    val sequence: String,
    val size: String,
    val upgrade_time: Long,
    var position: Int = 0
):MultiItemEntity{
    override var itemType = R.layout.listen_item_subs_list_chapter
}

data class ListenAudio(
    val info : ListenAudioInfo,
    val chapter : ArrayList<ListenAudioChapter>):MultiItemEntity{
    override var itemType = R.layout.listen_item_audio_rv
}

data class ListenAudioInfo(
    val audio_id: String,
    val cover_url: String,
    val audio_name: String,
    val upgrade_time: Long):MultiItemEntity{
    override var itemType = R.layout.listen_item_subs_list_audio
}


data class ListenSubsDateModel constructor(
    val date: String = "",
    var isSelected : Boolean = false,
    var isTopRvItem : Boolean = false
):MultiItemEntity{
    override var itemType = if(isTopRvItem){
        R.layout.listen_item_subs_top_date
    }else{
        R.layout.listen_item_subs_list_audio_date
    }
}
