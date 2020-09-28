package com.example.music_exoplayer_lib.manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import com.rm.music_exoplayer_lib.constants.ACTION_ALARM_REPLENISH_STOCK
import com.rm.music_exoplayer_lib.receiver.AlarmBroadcastReceiver


/**
 *定时任务管理类
 * @des:
 * @data: 8/22/20 3:28 PM
 * @Version: 1.0.0
 */
internal class BookAlarmManger(val context: Context) {
    /**
     * 设置定时
     */
    var alarmService: AlarmManager? = null
    var alarmIntent: Intent? = null
    var broadcast: PendingIntent? = null

    init {
        alarmService = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            setAction(
                ACTION_ALARM_REPLENISH_STOCK
            )
        }
        broadcast = PendingIntent.getBroadcast(
            context, 0, alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        ) //通过广播接收
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setAlarm(times: Long) {
        alarmService?.set(AlarmManager.RTC_WAKEUP, times, broadcast)



    }

    /**
     * 取消定时
     */
    fun cancelAlarm() {
        alarmService?.cancel(
            PendingIntent.getBroadcast(
                context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}