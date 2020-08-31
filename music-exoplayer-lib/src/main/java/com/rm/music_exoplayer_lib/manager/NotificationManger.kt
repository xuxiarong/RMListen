package com.rm.music_exoplayer_lib.manager

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.rm.music_exoplayer_lib.R
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.utils.MusicRomUtil

var NOTIFICATION_ID: Int = 10001

class NotificationManger constructor(val context: Service) {
    /**
     * 创建并显示一个通知栏
     * @param notification 通知栏
     * @param notifiid 通知栏ID
     * @param foregroundEnable 是否常驻进程
     */
    //通知栏
    private val mNotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    //前台进程默认是开启的,通知交互默认是开启的
    var mForegroundEnable: Boolean = true //前台进程默认是开启的,通知交互默认是开启的
    var mNotificationEnable = true
     fun showNotification(
        notification: Notification?,
        notifiid: Int,
        foregroundEnable: Boolean
    ) {
        NOTIFICATION_ID = notifiid
        this.mForegroundEnable = foregroundEnable
        mNotificationManager.notify(notifiid, notification)
        if (mForegroundEnable) {
            context.startForeground(NOTIFICATION_ID, notification)
        }
    }

    /**
     * 清除通知，常驻进程依然保留(如果开启)
     * @param notifiid 通知栏ID
     */
     fun cleanNotification(notifiid: Int) {
        mNotificationManager.cancel(notifiid)
    }

    /**
     * 构建一个前台进程通知
     * @param audioInfo 播放器正在处理的多媒体对象
     * @param cover 封面
     * @return 通知对象
     */
     fun buildNotifyInstance(
        audioInfo: BaseAudioInfo,
        defaultCoustomRemoteView:RemoteViews,
        bigCoustomRemoteView:RemoteViews

    ): Notification? {
        if (null == audioInfo) {
            return null
        }
        //8.0及以上系统需创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = context.getResources().getString(R.string.music_text_notice_name)
            val channel = NotificationChannel(
                CHANNEL_ID,
                name, NotificationManager.IMPORTANCE_LOW
            )
            channel.enableVibration(false)
            mNotificationManager.createNotificationChannel(channel)
        }
        //通知栏根部点击意图
        val rootIntent = Intent(MUSIC_INTENT_ACTION_ROOT_VIEW)
        rootIntent.putExtra(MUSIC_KEY_MEDIA_ID, 1)
        val pendClickIntent = PendingIntent.getBroadcast(
            context, 1,
            rootIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val appName: String = context.getString(R.string.music_text_now_play)
        //构造通知栏
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context,"001")
        builder.setContentIntent(pendClickIntent)
            .setTicker(appName)
            .setWhen(System.currentTimeMillis())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCustomContentView(defaultCoustomRemoteView)
            .setCustomBigContentView(bigCoustomRemoteView)
            .setChannelId(CHANNEL_ID).priority = Notification.PRIORITY_HIGH
        if (MusicRomUtil.instance?.isMiui==true) {
            builder.setFullScreenIntent(pendClickIntent, false) //禁用悬挂
        } else {
            builder.setFullScreenIntent(null, false) //禁用悬挂
        }
        return builder.build()
    }


}