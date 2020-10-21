package com.rm.module_mine.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * 用户/主播详情信息
 */
data class MineInfoDetail(
    var id:String,
    var nickname:String,
    var member_intro : String,
    var fans :String,
    var follows :String ,
    var avatar_url :String ,
    var is_followed : String ,
    var member_type :Int  //1普通用户，2主播
):Serializable

/**
 * 主页发布书籍/听单/收藏听单列表
 */
data class MineInfoProfile(
    val member_type: String, //用户类型；1:普通；2:主播，如果是主播，不需要展示“发布的书籍模块”
    val favor_sheet_info: FavorSheetInfo,
    val publish_info: PublishInfo,
    val sheet_info: SheetInfo

):Serializable

//收藏的听单
data class FavorSheetInfo(
    @SerializedName("list")
    val list_favorsheet: MutableList<FavorSheetBean>,
    val total: String
)
//发布的书籍
data class PublishInfo(
    @SerializedName("list")
    val list_publist: MutableList<PubListBean>,
    val total: String
)
//创建的听单
data class SheetInfo(
    @SerializedName("list")
    val list_sheet: MutableList<SheetBean>,
    val total: String
)

data class FavorSheetBean(
    val audio_label: String,
    val audio_list: MutableList<String>,
    val audio_total: String,
    val avatar_url: String,
    val created_at: String,
    val member_id: String,
    val member_name: String,
    val num_audio: String,
    val num_favor: String,
    val pre_deleted_from: String,
    val sheet_id: String,
    val sheet_name: String
)

data class PubListBean(
    val audio_cover_url: String,
    val audio_id: String,
    val audio_intro: String,
    val audio_label: String,
    val audio_name: String,
    val last_sequence: String,
    val play_count: String,
    val short_intro: String
)

data class SheetBean(
    val created_at: String,
    val created_from: String,
    val num_audio: String,
    val num_favor: String,
    val pre_deleted_from: String,
    val sheet_cover: String,
    val sheet_id: String,
    val sheet_label: String,
    val sheet_name: String,
    val status: String
)

