package com.rm.business_lib.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import com.rm.business_lib.R
import com.tencent.bugly.Bugly


/**
 *
 * @author yuanfang
 * @date 12/10/20
 * @description
 *
 */
class NotifyManager(val context: Context) {
    private var notificationManager: NotificationManager? = null
    private var notification: Notification.Builder? = null
    private val notifyId = 0x100
    fun startNotificationManager() {
        val vibrate = longArrayOf(0, 500, 1000, 1500)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notification = Notification.Builder(
                context.applicationContext,
                System.currentTimeMillis().toString()
            )
                .setContentTitle("下载进度")
                .setContentText("app下载")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setVibrate(vibrate)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(context.packageName)
                .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI)

            val channel = NotificationChannel(
                context.packageName,
               "app下载",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager?.createNotificationChannel(channel)

            notificationManager?.notify(notifyId, notification?.build())
        } else {
            notification = Notification.Builder(context)
                .setContentTitle("下载进度")
                .setContentText("app下载")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        Bugly.applicationContext.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setVibrate(vibrate)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI)
            notificationManager?.notify(notifyId, notification?.build())
        }

    }
    fun updateProgress(progress: Int) {
        if (progress == 100) {
            notificationManager?.cancel(notifyId)
        } else {
            notification?.setProgress(100, progress, false)
            notificationManager?.notify(notifyId, notification?.build())
        }
    }
}