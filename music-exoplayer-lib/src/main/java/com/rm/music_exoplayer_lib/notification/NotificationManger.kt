package com.rm.music_exoplayer_lib.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.rm.baselisten.BaseConstance
import com.rm.music_exoplayer_lib.R
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.MUSIC_INTENT_ACTION_ROOT_VIEW
import com.rm.music_exoplayer_lib.constants.MUSIC_KEY_MEDIA_ID
import com.rm.music_exoplayer_lib.utils.MusicRomUtil

var NOTIFICATION_ID: Int = 10001

class NotificationManger constructor(
    val context: Service,
    val audioInfo: BaseAudioInfo,
    val playState: Int
) {
    /**
     * 创建并显示一个通知栏
     * @param notification 通知栏
     * @param notifiid 通知栏ID
     * @param foregroundEnable 是否常驻进程
     */
    //通知栏
    private val notificationView by lazy {
        NotificationRemoteView(context)
    }
    val mNotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    //前台进程默认是开启的,通知交互默认是开启的
    var mForegroundEnable: Boolean = true //前台进程默认是开启的,通知交互默认是开启的
    var mNotificationEnable = true

    /**
     * 构造一个默认的通知栏并显示
     */
    fun showNotification(context: Context, musicInfo: BaseAudioInfo,bookImgUrl:String) {
        if (mNotificationEnable) {
            val manager: NotificationManagerCompat =
                NotificationManagerCompat.from(context)
            val isOpen: Boolean = manager.areNotificationsEnabled()
            if (isOpen) {
                buildNotifyInstance(musicInfo,bookImgUrl)
            }
        }
    }

    private fun showNotificationForeground(
        notification: Notification?
    ) {
        mNotificationManager.notify(NOTIFICATION_ID, notification)
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
    fun buildNotifyInstance(musicInfo: BaseAudioInfo,bookImgUrl:String) {
        val CHANNEL_ID = "CHANNEL_ID"
        val CHANNEL_NAME = "听书"
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
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "CHANNEL_ID")
        val remoteView = notificationView.getDefaultCustomRemoteView(
                playState,
                musicInfo
        )
        builder.setContentIntent(pendClickIntent)
                .setTicker(musicInfo.audioName)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteView)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setChannelId(CHANNEL_ID).priority = Notification.PRIORITY_HIGH
        if (MusicRomUtil.instance?.isMiui == true) {
            builder.setFullScreenIntent(pendClickIntent, false) //禁用悬挂
        } else {
            builder.setFullScreenIntent(null, false) //禁用悬挂
        }

        val notifyTarget = NotificationTarget(
                context,
                R.id.music_notice_def_cover,
                remoteView,
                builder.build(),
                NOTIFICATION_ID)
        BaseConstance.basePlayInfoModel.get()?.let {
            val notification = builder.build()
            Glide.with(context)
                    .asBitmap()
                    .load(it.playUrl)
                    .placeholder(R.drawable.ic_music_default_cover)
                    .error(R.drawable.ic_music_default_cover)
                    .into(notifyTarget)

            showNotificationForeground(notification)
        }

    }


}