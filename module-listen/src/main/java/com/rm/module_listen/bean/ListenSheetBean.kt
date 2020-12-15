package com.rm.module_listen.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListenSheetBean(
    var sheet_id: Long,//听单id
    var sheet_cover: String,//封面
    var sheet_label: String,//角标
    var sheet_name: String,//听单名
    var num_audio: Int,//书籍数量
    var num_favor: Int,//被收藏数
    var created_from: Int,//听单创建来源；2:默认听单，不允许删除
    var created_at: String,//创建时间
    var pre_deleted_from: String//预删除来源；0:不打算删除；1:后管删除；2:创建者删除
) : Parcelable