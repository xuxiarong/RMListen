package com.example.music_exoplayer_lib.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.SystemClock
import com.example.music_exoplayer_lib.constants.ACTION_ALARM_REPLENISH_STOCK
import com.example.music_exoplayer_lib.receiver.AlarmBroadcastReceiver


/**
 *定时任务管理类
 * @des:
 * @data: 8/22/20 3:28 PM
 * @Version: 1.0.0
 */
internal class AlarmManger(val context: Context) {
    /**
     * 设置定时
     */
    fun setAlarm(times:Int) {
        val interval = SystemClock.elapsedRealtime() + times
        val alarmService = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).setAction(
            ACTION_ALARM_REPLENISH_STOCK
        )
        val broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, 0) //通过广播接收
        alarmService.set(AlarmManager.ELAPSED_REALTIME, interval, broadcast)


    }

    /**
     * 取消定时
     */
    fun cancelAlarm(requestCode: Int, action: String) {
        var alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(context, AlarmBroadcastReceiver::class.java).setAction(
                ACTION_ALARM_REPLENISH_STOCK
            )
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}