package com.rm.module_mine.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.StringBuilder

data class MineFollowFans (
    var total : String,
    @SerializedName("list")
    var folloFansList : FolloFansList
)

data class FolloFansList(
    var member_id : String ,
    var nickname : String ,
    var signature : String ,
    var avatar : String ,
    var avatar_url : String ,
    var is_follow : String
):Serializable{
    fun getFolow():String{
        val result = StringBuilder()
        result.append(is_follow)
        when(is_follow){
            "0" -> result.append("关注")

            "1" -> result.append("已关注")
        }
        return result.toString()
    }
}