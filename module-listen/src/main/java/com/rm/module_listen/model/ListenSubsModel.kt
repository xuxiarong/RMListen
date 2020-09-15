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
    val early: List<ListenSubsEarly>,
    val last_unread: Int,
    val today: List<ListenSubsToday>,
    val total_unread: Int,
    val yesterday: List<ListenSubsYesterday>
) : Parcelable

@Parcelize
data class ListenSubsEarly(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<ListenSubsChapter>,
    val upgrade_time: String
) : Parcelable,ListenAudioModel(audio_cover,audio_id,audio_name,chapter_list,upgrade_time)

@Parcelize
data class ListenSubsToday(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<ListenSubsChapter>
) : Parcelable,ListenAudioModel(audio_cover,audio_id,audio_name,chapter_list,"今天")

@Parcelize
data class ListenSubsYesterday(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<ListenSubsChapter>
) : Parcelable,ListenAudioModel(audio_cover,audio_id,audio_name,chapter_list,"昨天")

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
@Parcelize
open class ListenAudioModel(
    val audioCover: String,
    val audioId: String,
    val audioName: String,
    val chapterList: List<ListenSubsChapter>,
    val audioUpdateTime : String
) : Parcelable

