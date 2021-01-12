package com.rm.module_mine.bean


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

data class User(val admin: Boolean,
                val chapterTops: List<String>,
                val collectIds: List<Int>,
                val email: String,
                val icon: String,
                val id: Int,
                val nickname: String,
                val password: String,
                val publicName: String,
                val token: String,
                val type: Int,
                val username: String){

    override fun equals(other: Any?): Boolean {
        return if (other is User){
            this.id == other.id
        }else false
    }
}