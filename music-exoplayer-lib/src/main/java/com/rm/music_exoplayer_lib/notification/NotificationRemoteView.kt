package com.rm.music_exoplayer_lib.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import com.rm.music_exoplayer_lib.R
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*

/**
 *
 * @des: Notification布局类
 * @data: 8/31/20 6:56 PM
 * @Version: 1.0.0
 */
class NotificationRemoteView constructor(val context: Context) {
    /**
     * 生成并绑定点击事件的默认RemoteView
     * @param audioInfo 音频对象
     * @param cover 封面
     * @return RemoteView
     *
     */

    fun getDefaultCoustomRemoteView(
        cover: Bitmap,
        mMusicPlayerState: Int
        , musicInfo: BaseAudioInfo
    ): RemoteViews {
        val defaultremoteviews =
            RemoteViews(context.packageName, R.layout.music_notify_default_controller)
        defaultremoteviews.setImageViewBitmap(R.id.music_notice_def_cover, cover)
        defaultremoteviews.setImageViewResource(
            R.id.music_notice_def_btn_pause,
            getPauseIcon(mMusicPlayerState)
        )
        defaultremoteviews.setTextViewText(R.id.music_notice_def_title, musicInfo.audioName)
        defaultremoteviews.setTextViewText(R.id.music_notice_def_subtitle, musicInfo.filename)
        //上一首
        defaultremoteviews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_last, getClickPending(MUSIC_INTENT_ACTION_CLICK_LAST)
        )
        //下一首
        defaultremoteviews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_next,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_NEXT)
        )
        //暂停、开始
        defaultremoteviews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_pause,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_PAUSE)
        )
        //关闭
        defaultremoteviews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_close,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_CLOSE)
        )
        return defaultremoteviews
    }

    /**
     * 通知栏开始、暂停 按钮
     * @param playerState 播放状态
     * @return 播放状态对应的RES
     */
    private fun getPauseIcon(playerState: Int): Int {
        when (playerState) {
            MUSIC_PLAYER_PREPARE, MUSIC_PLAYER_PLAYING, MUSIC_PLAYER_BUFFER -> return R.drawable.ic_music_mini_pause_noimal
            MUSIC_PLAYER_STOP, MUSIC_PLAYER_ERROR -> return R.drawable.ic_music_mini_play_noimal
        }
        return R.drawable.ic_music_mini_play_noimal
    }

    /**
     * 生成并绑定大通知栏View点击事件的默认RemoteView
     * @param audioInfo 音频对象
     * @param cover 封面
     * @return RemoteView
     */
    private fun getClickPending(action: String): PendingIntent = PendingIntent.getBroadcast(context, 1, Intent(action), PendingIntent.FLAG_UPDATE_CURRENT)

    fun getBigCoustomRemoteView(
        cover: Bitmap,
        mMusicPlayerState: Int,
        musicInfo: BaseAudioInfo
    ): RemoteViews {
        val bigRemoteViews =
            RemoteViews(context.packageName, R.layout.music_notify_big_controller)
        bigRemoteViews.setImageViewBitmap(R.id.music_notice_def_cover, cover)
        bigRemoteViews.setImageViewResource(
            R.id.music_notice_def_btn_pause,
            getPauseIcon(mMusicPlayerState)
        )
        bigRemoteViews.setTextViewText(R.id.music_notice_def_title, musicInfo.audioName)
        bigRemoteViews.setTextViewText(R.id.music_notice_def_subtitle, musicInfo.filename)
        //上一首
        bigRemoteViews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_last,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_LAST)
        )
        //下一首
        bigRemoteViews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_next,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_NEXT)
        )
        //暂停、开始
        bigRemoteViews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_pause,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_PAUSE)
        )
        //关闭
        bigRemoteViews.setOnClickPendingIntent(
            R.id.music_notice_def_btn_close,
            getClickPending(MUSIC_INTENT_ACTION_CLICK_CLOSE)
        )

        return bigRemoteViews
    }


}