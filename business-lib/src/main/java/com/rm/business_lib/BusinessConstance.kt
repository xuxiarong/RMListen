package com.rm.business_lib

import android.content.Context
import androidx.annotation.IntDef
import androidx.databinding.*
import androidx.lifecycle.MutableLiveData
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.play.PlayState

/**
 * desc   : 基础业务常量类
 * date   : 2020/09/01
 * version: 1.0
 */

// ******** login ********
// 访问令牌(token)
const val ACCESS_TOKEN = "accessToken"

// 刷新令牌(token)
const val REFRESH_TOKEN = "refreshToken"

// 当前访问token失效时间
const val ACCESS_TOKEN_INVALID_TIMESTAMP = "accessTokenInvalidTimestamp"

// 当前登陆用户信息
const val LOGIN_USER_INFO = "loginUserInfo"

// 当前是否登陆
var isLogin = ObservableBoolean(false)

// 当前登陆的用户信息
var loginUser = ObservableField<LoginUserBean>()

object HomeGlobalData {
    var isHomeDouClick = MutableLiveData(false)
    var homeGlobalSelectTab = ObservableInt(0)
}


object PlayGlobalData {

    /**
     * 以下状态是Google的播放器原生状态，请不要随意修改
     */
    //空闲
    var STATE_IDLE = 1
    //缓冲
    var STATE_BUFFERING = 2
    //播放准备好
    var STATE_READY = 3
    //全部播放完毕
    var STATE_ENDED = 4

    //全局播放器定时时间
    var playTimerDuration = ObservableLong(0L)

    //全局播放器播放速度
    var playSpeed = ObservableFloat(1.0f)

    var playState = ObservableField<PlayState>(PlayState())

    const val PLAY_FIRST_PAGE = 1
    const val PLAY_PAGE_SIZE = 20
}

object AudioSortType {

    //倒序
    const val SORT_DESC = "desc"

    //正序
    const val SORT_ASC = "asc"
}

object LoginPhoneReminder {
    var phoneMap = hashMapOf<Int, String>()

    fun getCurrentActivityInputPhone(context: Context): String {
        if (phoneMap.size > 0 && phoneMap[context.applicationContext.hashCode()] != null) {
            return phoneMap[context.applicationContext.hashCode()]!!
        }
        return ""
    }

    fun putCurrentActivityInputPhone(context: Context, phone: String) {
        phoneMap.clear()
        phoneMap[context.applicationContext.hashCode()] = phone
    }

}

// ******** home ********
//是否是第首次收藏
const val IS_FIRST_FAVORITES = "is_first_favorites"

//是否是首次添加听单
const val IS_FIRST_ADD_SHEET = "is_first_add_sheet"

//是否是首次订阅
const val IS_FIRST_SUBSCRIBE = "is_first_subscribe"


// ******** 我听 ********
@IntDef(LISTEN_SHEET_LIST_MY_LIST, LISTEN_SHEET_LIST_COLLECTED_LIST)
annotation class ListenSheetListType(val type: Int = LISTEN_SHEET_LIST_MY_LIST)

const val LISTEN_SHEET_LIST_MY_LIST = 0 //我的听单
const val LISTEN_SHEET_LIST_COLLECTED_LIST = 1 //收藏听单


val SAVA_SPEED = "savaSpeed"
