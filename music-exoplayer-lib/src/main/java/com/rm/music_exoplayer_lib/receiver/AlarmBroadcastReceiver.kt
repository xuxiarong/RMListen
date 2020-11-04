package com.rm.music_exoplayer_lib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY
import android.view.KeyEvent
import android.widget.Toast
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger


/**
 *
 * @des:定时播放时常
 * @data: 8/22/20 3:22 PM
 * @Version: 1.0.0
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var action = intent?.action
        //定时通知
        when (action) {
            ACTION_ALARM_SYNCHRONIZE -> {
                doSynchronizeAction(context)
            }
            ACTION_ALARM_REPLENISH_STOCK -> {
                doReplenishStockAction(context)
            }
            //通知栏操作
            //耳机拔出
            ACTION_AUDIO_BECOMING_NOISY -> {
                musicPlayerManger.pause()
                ExoplayerLogger.exoLog("耳机拔出")
            }
            //前台进程-通知栏根点击事件
            MUSIC_INTENT_ACTION_ROOT_VIEW -> {
                ExoplayerLogger.exoLog("通知栏根点击事件")
            }
            //前台进程上一首
            MUSIC_INTENT_ACTION_CLICK_LAST -> {
                musicPlayerManger.playLastMusic()
            }
            //前台进程 下一首
            MUSIC_INTENT_ACTION_CLICK_NEXT -> {
                musicPlayerManger.playNextMusic()
            }
            //前台进程-暂停、开始
            MUSIC_INTENT_ACTION_CLICK_PAUSE -> {
                musicPlayerManger.playOrPause()
            }
            //前台进程关闭进程
            MUSIC_INTENT_ACTION_CLICK_CLOSE -> {

            }
            else -> {
            }
        }


    }

    private fun doSynchronizeAction(context: Context?) {
        Toast.makeText(context, "同步", Toast.LENGTH_SHORT).show()
    }

    /**
     * 执行补充库存动作, 即下单/定货
     */
    private fun doReplenishStockAction(context: Context?) {
        ExoplayerLogger.exoLog("关闭音乐播放器")
//        Toast.makeText(context, "关闭音乐播放", Toast.LENGTH_SHORT).show()
        musicPlayerManger.pause()
    }
}