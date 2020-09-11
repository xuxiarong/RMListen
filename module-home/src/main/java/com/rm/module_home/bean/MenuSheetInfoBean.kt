package com.rm.module_home.bean

import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.AudioListBean

data class MenuSheetInfoBean(
    var audio_list: AudioListBean,
    var avatar_url: String,
    var cover_url: String,
    var created_at: String,
    var created_from: Int,
    var favor: Int,
    var member_id: String,
    var nickname: String,
    var num_audio: Int,
    var num_favor: Int,
    var num_play: Int,
    var sheet_cover: String,
    var sheet_id: String,
    var sheet_intro: String,
    var sheet_name: String,
    var status: Int
){
    fun test():String{
        return "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg"
    }

    fun getList():MutableList<AudioBean>{
        val data= mutableListOf<AudioBean>()
        val list = audio_list.list

        if (list != null) {
            data.addAll(list)
            data.addAll(list)
            data.addAll(list)
        }
        return data
    }
}
