package com.rm.business_lib.insertpoint

/**
 *
 * @author yuanfang
 * @date 12/12/20
 * @description
 *
 */
class BusinessInsertConstance {
    companion object {
        const val INSERT_TYPE_ACTIVATION = "10000"//激活用户数
        const val INSERT_TYPE_ADD = "10001"//新增用户数
        const val INSERT_TYPE_ACTIVE = "10002"//活跃用户数
        const val INSERT_TYPE_LOGIN = "10003"//登录用户数
        const val INSERT_TYPE_START = "10006"//启动次数
        const val INSERT_TYPE_AUDIO_SUBSCRIPTION = "20000"//音频订阅量
        const val INSERT_TYPE_AUDIO_UNSUBSCRIBED = "20001"//音频被取消订阅量
        const val INSERT_TYPE_AUDIO_BROWSING = "20002"//音频浏览量
        const val INSERT_TYPE_AUDIO_PLAY = "20003"//音频播放量
        const val INSERT_TYPE_CHAPTER_PLAY = "20004"//章节播放量
        const val INSERT_TYPE_LISTEN_COLLECTION = "20005"//听单收藏量
        const val INSERT_TYPE_LISTEN_UN_FAVORITE = "20006"//听单被取消收藏量
        const val INSERT_TYPE_AD_EXPOSURE = "40000"//广告曝光量
        const val INSERT_TYPE_AD_CLICK = "40001"//广告点击量
        const val INSERT_TYPE_AD_CLOSE = "40002"//广告关闭量
    }

}