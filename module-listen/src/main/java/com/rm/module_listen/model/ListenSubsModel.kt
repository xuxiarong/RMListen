package com.rm.module_listen.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * desc   :
 * date   : 2020/09/11
 * version: 1.0
 */
@Parcelize
data class ListenSubsModel(
    val listenSubsEarly: List<ListenSubsEarly>,
    val last_unread: Int,
    val listenSubsToday: List<ListenSubsToday>,
    val total_unread: Int,
    val listenSubsYesterday: List<ListenSubsYesterday>
) : Parcelable

@Parcelize
data class ListenSubsEarly(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val listenSubsChapter_list: List<ListenSubsChapter>,
    val upgrade_time: String
) : Parcelable

@Parcelize
data class ListenSubsToday(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val listenSubsChapter_list: List<ListenSubsChapter>
) : Parcelable

@Parcelize
data class ListenSubsYesterday(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val listenSubsChapter_list: List<ListenSubsChapter>
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
) : Parcelable

