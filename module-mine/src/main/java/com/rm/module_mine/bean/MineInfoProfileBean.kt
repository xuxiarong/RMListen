package com.rm.module_mine.bean

import java.io.Serializable

/**
 *
 * @author yuanfang
 * @date 10/22/20
 * @description 主页发布书籍/听单/收藏听单列表
 *
 */

data class MineInfoProfileBean(
    val member_type: String, //用户类型；1:普通；2:主播，如果是主播，不需要展示“发布的书籍模块”
    val favor_sheet_info: FavorSheetInfo,//收藏的听单
    val publish_info: MinePublishBean,//发布的书籍
    val sheet_info: SheetInfo//创建的听单

) : Serializable


//收藏的听单
data class FavorSheetInfo(
    val list: MutableList<FavorSheetBean>?,
    val total: Int
)

//创建的听单
data class SheetInfo(
    val list: MutableList<SheetBean>?,
    val total: Int
)


data class FavorSheetBean(
    val audio_label: String,//标签
    val audio_list: MutableList<Any>?,//书籍列表，本接口不需要展示
    val audio_total: String,//听单包含的书籍总数
    val avatar_url: String,//头像
    val created_at: String,//创建时间
    val member_id: String,//创建者id
    val member_name: String,//创建者名字
    val num_audio: String,//音频数
    val num_favor: String,//收藏数
    val pre_deleted_from: String,//预删除来源；0:不打算删除；1:后管删除；2:创建者删除
    val sheet_id: String,//书单id
    val sheet_name: String,//书单名
    val sheet_cover: String//书单封面
)

data class SheetBean(
    val created_at: String,//创建时间
    val created_from: Int,//听单创建来源；3:默认听单，不允许删除
    val num_audio: String,// 书籍数量
    val num_favor: String,//被收藏数
    val pre_deleted_from: String,//预删除来源；0:不打算删除；1:后管删除；2:创建者删除
    val sheet_cover: String,//封面
    val sheet_id: String,//听单id
    val sheet_label: String,//角标
    val sheet_name: String,//听单名
    val status: String//听单状态；1:正常
)

