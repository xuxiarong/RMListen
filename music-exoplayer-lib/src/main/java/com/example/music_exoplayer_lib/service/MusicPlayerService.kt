package com.example.music_exoplayer_lib.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.music_exoplayer_lib.bean.BaseAudioInfo
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.example.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.example.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.example.music_exoplayer_lib.utils.ExoplayerLogger.exoLog
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * desc   :播放器核心类
 * date   : 2020/08/13
 * version: 1.0
 */
internal class MusicPlayerService : Service(), MusicPlayerPresenter {
    val UPDATE_PROGRESS_DELAY = 500L
    private val mOnPlayerEventListeners = arrayListOf<MusicPlayerEventListener>()
    private val mEventListener = ExoPlayerEventListener()
    private val mExoPlayer: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build()
            addListener(mEventListener)
        }

    }
    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            this, Util.getUserAgent(this, packageName), null
        )
    }


    //进度条消息
    @SuppressLint("HandlerLeak")
    private val mUpdateProgressHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val duration = mExoPlayer.contentDuration ?: 0
            val currentPosition = mExoPlayer.contentPosition ?: 0
            exoLog("duration===>${duration?.toInt()}")
            onUpdateProgress(currentPosition, duration)
            sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
        }
    }

    //Service委托代理人
    private var mPlayerBinder: MusicPlayerBinder? = null


    /**MusicPlayerPresenter方法实现*/
    override fun onBind(intent: Intent): IBinder {
        if (null == mPlayerBinder) {
            mPlayerBinder = MusicPlayerBinder(this)
        }
        return mPlayerBinder as MusicPlayerBinder
    }

    val RADIO_URL =
        "https://webfs.yun.kugou.com/202008211909/9887fb4705db0d4413a61ed8448c20c0/G164/M02/19/09/hJQEAF1ou5aAVrTxADlcwhIMwfQ617.mp3"

//    val RADIO_URL = "http://kastos.cdnstream.com/1345_32"

    @Synchronized
    private fun startPlay(musicInfo: BaseAudioInfo) {
        mExoPlayer.prepare(
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(RADIO_URL))
        )
        mExoPlayer.playWhenReady = true

    }

    override fun startPlayerMusic(index: Int) {
        startPlay(BaseAudioInfo())
    }

    override fun startPlayMusic(audios: List<*>?, index: Int) {
        TODO("Not yet implemented")
    }

    override fun playOrPause() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        mExoPlayer.playWhenReady = false
        mUpdateProgressHandler.removeMessages(0)
    }

    override fun play() {
        TODO("Not yet implemented")
    }

    override fun setLoop(loop: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        mUpdateProgressHandler.removeMessages(0)
        mExoPlayer.release()
        mExoPlayer.removeListener(mEventListener)
    }

    override fun playLastMusic() {
        TODO("Not yet implemented")
    }

    override fun playNextMusic() {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        return mExoPlayer.playWhenReady == true
    }

    override fun getDurtion(): Long {
        //播放总长度
        return mExoPlayer.contentDuration
    }

    override fun getCurrentPlayerID(): Long {
        TODO("Not yet implemented")
    }

    override fun seekTo(currentTime: Long) {
        mExoPlayer.seekTo(currentTime)
    }

    override fun getCurrentPlayerMusic(): BaseAudioInfo {
        TODO("Not yet implemented")
    }

    override fun getCurrentPlayList(): List<*> {
        TODO("Not yet implemented")
    }

    override fun setPlayInfoListener(listener: MusicPlayerInfoListener) {
        TODO("Not yet implemented")
    }

    override fun removePlayInfoListener() {
        TODO("Not yet implemented")
    }

    override fun addOnPlayerEventListener(listener: MusicPlayerEventListener) {
        mOnPlayerEventListeners.add(listener)
    }

    override fun removePlayerListener(listener: MusicPlayerEventListener) {
        mOnPlayerEventListeners.remove(listener)
    }

    override fun removeAllPlayerListener() {
        mOnPlayerEventListeners.clear()
    }

    override fun setPlayerMultiple(p: Float) {
        mExoPlayer.setPlaybackParameters(PlaybackParameters(p,1.0f))
    }

    /**
     * 更新播放进度
     */
    private fun onUpdateProgress(currentPosition: Long, duration: Long) {

        mOnPlayerEventListeners.forEach {
            it.onTaskRuntime(duration, currentPosition, 0, 0)
        }
    }

    private inner class ExoPlayerEventListener : Player.EventListener {
        /**
         * 播放状态改变
         */
        var i = 0
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE, Player.STATE_BUFFERING, Player.STATE_READY -> {
                    if (!mUpdateProgressHandler.hasMessages(0)) {
                        mUpdateProgressHandler.sendEmptyMessage(0)
                    }
                }
                Player.STATE_ENDED -> {
                    mUpdateProgressHandler.removeMessages(0)
                }
            }
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            exoLog(mExoPlayer.contentDuration)
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            exoLog(mExoPlayer.contentDuration)

        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying){
                mOnPlayerEventListeners.forEach {
                    it.onPrepared(getDurtion())
                }
            }
        }
    }
}

