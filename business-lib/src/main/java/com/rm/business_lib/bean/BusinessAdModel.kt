package com.rm.business_lib.bean

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
data class BusinessAdModel(
    var ad_id: Long,
    var ad_name: String,
    var ad_owner: String,
    var ad_position_id: Int,
    var ad_sub_title: String,
    var ad_title: String,
    var ad_type_key: String,
    var audio_path: String,
    var created_at: Long,
    var creator: String,
    var end_time: Long,
    var image_path: String,
    var is_attach_to_block: Int,
    var is_audio: String,
    var is_count_down: Int,
    var is_image: String,
    var is_need_sort: Long,
    var is_video: String,
    var jump_url: String,
    var start_time: Int,
    var status: Int,
    var updated_at: Int,
    var video_path: String
)
