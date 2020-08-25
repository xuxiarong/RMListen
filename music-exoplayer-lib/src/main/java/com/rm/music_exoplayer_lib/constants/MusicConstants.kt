package com.rm.music_exoplayer_lib.constants

/**
 *
 * @des:
 * @data: 8/22/20 3:24 PM
 * @Version: 1.0.0
 */

const val ACTION_ALARM_REPLENISH_STOCK = "witmoon.auto.replenish.stock.action"
const val ACTION_ALARM_SYNCHRONIZE = "witmoon.auto.synchronize.action"

/**
 * 播放器内部各种状态，替换enum类型
 */
const val MUSIC_PLAYER_STOP = 0 //已结束，或未开始
const val MUSIC_PLAYER_PREPARE = 1 //准备中
const val MUSIC_PLAYER_BUFFER = 2 //缓冲中
const val MUSIC_PLAYER_PLAYING = 3 //播放中
const val MUSIC_PLAYER_PAUSE = 4 //暂停
const val MUSIC_PLAYER_ERROR = 5 //错误
