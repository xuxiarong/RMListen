package com.example.music_exoplayer_lib.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.music_exoplayer_lib.constants.ACTION_ALARM_REPLENISH_STOCK
import com.example.music_exoplayer_lib.constants.ACTION_ALARM_SYNCHRONIZE
import com.example.music_exoplayer_lib.manager.MusicPlayerManager
import com.example.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.example.music_exoplayer_lib.utils.ExoplayerLogger

/**
 *
 * @des:定时播放时常
 * @data: 8/22/20 3:22 PM
 * @Version: 1.0.0
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var action = intent?.action
        when {
            ACTION_ALARM_SYNCHRONIZE.equals(action) -> doSynchronizeAction(context)
            ACTION_ALARM_REPLENISH_STOCK.equals(action) -> doReplenishStockAction(context)
        }
    }
    private fun doSynchronizeAction(context: Context?) {
        Toast.makeText(context, "同步", Toast.LENGTH_SHORT).show()
    }

    /**
     * 执行补充库存动作, 即下单/定货
     */
    private fun doReplenishStockAction(context: Context?) {
        ExoplayerLogger.exoLog("广比")
        musicPlayerManger.pause()
    }
}