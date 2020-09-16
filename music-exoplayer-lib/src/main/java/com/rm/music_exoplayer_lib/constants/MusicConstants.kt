package com.rm.music_exoplayer_lib.constants

/**
 *
 * @des:
 * @data: 8/22/20 3:24 PM
 * @Version: 1.0.0
 */

const val ACTION_ALARM_REPLENISH_STOCK = "witmoon.auto.replenish.stock.action"
const val ACTION_ALARM_SYNCHRONIZE = "witmoon.auto.synchronize.action"
//播放模式
val PLAY_MODEL="PLAY_MODEL"
//播放闹钟设置
val SP_KEY_ALARM_MODEL = "SP_ALARM_MODEL"
val SP_KEY_ALARM_MODEL_TIME="SP_KEY_ALARM_MODEL_TIME"
//前台进程通知群组ID
var CHANNEL_ID = "com.android.rm.music_exoplayer_lib"

/**
 * 通知交互ACTION
 */
//点击通知栏
val MUSIC_INTENT_ACTION_ROOT_VIEW = "IMUSIC_INTENT_ACTION_CLICK_ROOT_VIEW"

//上一首
val MUSIC_INTENT_ACTION_CLICK_LAST = "IMUSIC_INTENT_ACTION_CLICK_LAST"

//下一首
val MUSIC_INTENT_ACTION_CLICK_NEXT = "IMUSIC_INTENT_ACTION_CLICK_NEXT"

//暂停、开始
val MUSIC_INTENT_ACTION_CLICK_PAUSE = "IMUSIC_INTENT_ACTION_CLICK_PAUSE"

//关闭通知栏
val MUSIC_INTENT_ACTION_CLICK_CLOSE = "IMUSIC_INTENT_ACTION_CLICK_CLOSE"

//收藏
val MUSIC_INTENT_ACTION_CLICK_COLLECT = "IMUSIC_INTENT_ACTION_CLICK_COLLECT"

//参数传入
val MUSIC_KEY_MEDIA_ID = "MUSIC_KEY_MEDIA_ID"
var BASE_SCREEN_WIDTH = 1080.0.toFloat()
//锁屏唱盘比例
var SCALE_DISC_LOCK_SIZE = (680.0 / BASE_SCREEN_WIDTH).toFloat()

/**
 * 播放器内部各种状态，替换enum类型
 */
const val MUSIC_PLAYER_STOP = 0 //已结束，或未开始
const val MUSIC_PLAYER_PREPARE = 1 //准备中
const val MUSIC_PLAYER_BUFFER = 2 //缓冲中
const val MUSIC_PLAYER_PLAYING = 3 //播放中
const val MUSIC_PLAYER_PAUSE = 4 //暂停
const val MUSIC_PLAYER_ERROR = 5 //错误

/**
 * 定时闹钟档次，替换enum类型
 */
const val MUSIC_ALARM_MODEL_10 = 10 //10分钟后

const val MUSIC_ALARM_MODEL_20 = 20
const val MUSIC_ALARM_MODEL_30 = 30
const val MUSIC_ALARM_MODEL_40 = 40
const val MUSIC_ALARM_MODEL_60 = 60
const val MUSIC_ALARM_MODEL_0 = 0 //无限期
const val MUSIC_ALARM_MODEL_CURRENT = 5 //当前歌曲播放完成立即结束


/**
 * 播放模式，替换enum类型
 */

const val MUSIC_MODEL_SINGLE = 1 //单曲模式

const val MUSIC_MODEL_ORDER = 2 //顺序播放

