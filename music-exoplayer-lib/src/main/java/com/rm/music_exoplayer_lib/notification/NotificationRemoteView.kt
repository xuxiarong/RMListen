package com.rm.music_exoplayer_lib.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.rm.baselisten.BaseConstance
import com.rm.business_lib.PlayGlobalData
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
     * @param playStatus 播放器的状态
     * @param musicInfo 音频对象
     * @return RemoteView
     *
     */

    fun getDefaultCustomRemoteView(
            playStatus: Int
            , musicInfo: BaseAudioInfo
    ): RemoteViews {
        val defaultRemoteView = RemoteViews(context.packageName, R.layout.music_notify_default_controller)

        PlayGlobalData.playChapter.get()?.let {
            defaultRemoteView.setTextViewText(R.id.play_notify_chapter_name, it.chapter_name)
        }
        PlayGlobalData.playAudioModel.get()?.let {
            defaultRemoteView.setTextViewText(R.id.play_notify_audio_name, it.audio_name)
        }
        BaseConstance.basePlayStatusModel.get()?.let {
            if(it.isStart()){
                defaultRemoteView.setImageViewResource(R.id.music_notice_def_btn_pause, R.drawable.music_ic_playing)
            }else{
                defaultRemoteView.setImageViewResource(R.id.music_notice_def_btn_pause, R.drawable.music_ic_pause)
            }
        }

        //上一首
        defaultRemoteView.setOnClickPendingIntent(
                R.id.music_notice_def_btn_last, getClickPending(MUSIC_INTENT_ACTION_CLICK_LAST)
        )
        //下一首
        defaultRemoteView.setOnClickPendingIntent(
                R.id.music_notice_def_btn_next,
                getClickPending(MUSIC_INTENT_ACTION_CLICK_NEXT)
        )
        //暂停、开始
        defaultRemoteView.setOnClickPendingIntent(
                R.id.music_notice_def_btn_pause,
                getClickPending(MUSIC_INTENT_ACTION_CLICK_PAUSE)
        )
        //关闭
        defaultRemoteView.setOnClickPendingIntent(
                R.id.music_notice_def_btn_close,
                getClickPending(MUSIC_INTENT_ACTION_CLICK_CLOSE)
        )
        return defaultRemoteView
    }

    /**
     * 通知栏开始、暂停 按钮
     * @param playerState 播放状态
     * @return 播放状态对应的RES
     */
    private fun getPauseIcon(playerState: Int): Int {
        return when (playerState) {
            MUSIC_PLAYER_PREPARE, MUSIC_PLAYER_PLAYING, MUSIC_PLAYER_BUFFER -> R.drawable.music_ic_playing
            MUSIC_PLAYER_STOP, MUSIC_PLAYER_ERROR -> R.drawable.music_ic_pause
            else -> {
                R.drawable.music_ic_pause
            }
        }
    }

    /**
     * 生成并绑定大通知栏View点击事件的默认RemoteView
     * @param audioInfo 音频对象
     * @param cover 封面
     * @return RemoteView
     */
    private fun getClickPending(action: String): PendingIntent = PendingIntent.getBroadcast(context, 1, Intent(action), PendingIntent.FLAG_UPDATE_CURRENT)

    fun getBigCoustomRemoteView(
            mMusicPlayerState: Int,
            musicInfo: BaseAudioInfo
    ): RemoteViews {
        val bigRemoteViews =
                RemoteViews(context.packageName, R.layout.music_notify_big_controller)
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