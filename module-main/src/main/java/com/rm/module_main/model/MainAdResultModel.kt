package com.rm.module_main.model

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */

data class MainAdResultModel(
    var ad_screen: List<MainAdScreen>?
)

data class AdIndexAlert(
    var ad_id: Int,
    var ad_name: String,
    var ad_owner: String,
    var ad_position_id: Int,
    var ad_sub_title: String,
    var ad_title: String,
    var ad_type_key: String,
    var audio_path: String,
    var created_at: Int,
    var creator: String,
    var end_time: Int,
    var image_path: String,
    var is_attach_to_block: Int,
    var is_audio: String,
    var is_count_down: Int,
    var is_image: String,
    var is_need_sort: Int,
    var is_video: String,
    var jump_url: String,
    var start_time: Int,
    var status: Int,
    var updated_at: Int,
    var video_path: String
)

data class MainAdScreen(
    var ad_id: Int,
    var ad_name: String,
    var ad_owner: String,
    var ad_position_id: Int,
    var ad_sub_title: String,
    var ad_title: String,
    var ad_type_key: String,
    var audio_path: String,
    var created_at: Int,
    var creator: String,
    var end_time: Int,
    var image_path: String,
    var is_attach_to_block: Int,
    var is_audio: String,
    var is_count_down: Int,
    var is_image: String,
    var is_need_sort: Int,
    var is_video: String,
    var jump_url: String,
    var start_time: Int,
    var status: Int,
    var updated_at: Int,
    var video_path: String
)