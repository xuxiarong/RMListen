package com.rm.module_listen.model

import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_listen.R
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * desc   :
 * date   : 2020/09/11
 * version: 1.0
 */
@Parcelize
data class ListenSubsModel(
    val early: List<ListenAudioModel>,
    val last_unread: Int,
    val today: List<ListenAudioModel>,
    val total_unread: Int,
    val yesterday: List<ListenAudioModel>
) : Parcelable

@Parcelize
data class ListenSubsChapter(
    val chapter_id: String,
    val chapter_name: String,
    val duration: String,
    val path: String,
    val play_count: Long,
    val sequence: String,
    val size: String,
    val upgrade_time: String
) : Parcelable,MultiItemEntity{
    @IgnoredOnParcel
    override var itemType = R.layout.listen_item_subs_list_chapter
}
@Parcelize
data class ListenAudioModel(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<ListenSubsChapter>,
    var upgrade_time: String
) : Parcelable,MultiItemEntity{
    override var itemType = R.layout.listen_item_subs_list_audio
}
@Parcelize
data class ListenAudioDateModel constructor(
    val date: String,
    val isSelected : Boolean
) : Parcelable,MultiItemEntity{
    @IgnoredOnParcel
    override var itemType = R.layout.listen_item_subs_list_audio_date
}
