package com.example.music_exoplayer_lib.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.music_exoplayer_lib.bean.BaseAudioInfo
import com.example.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.example.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.example.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.example.music_exoplayer_lib.utils.ExoplayerLogger.exoLog
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.*

/**
 * desc   :播放器核心类
 * date   : 2020/08/13
 * version: 1.0
 */
internal class MusicPlayerService : Service(), MusicPlayerPresenter, Player.EventListener {
    val UPDATE_PROGRESS_DELAY = 500L
    private val mOnPlayerEventListeners = arrayListOf<MusicPlayerEventListener>()
    private val mExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(),
            DefaultLoadControl()
        ).apply {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
        }
    }
    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            this, Util.getUserAgent(this, packageName), null
        )
    }

    private val concatenatingMediaSource by lazy {
        ConcatenatingMediaSource()
    }

    //进度条消息
    @SuppressLint("HandlerLeak")
    private val mUpdateProgressHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val duration = mExoPlayer?.duration ?: 0
            val position = mExoPlayer?.currentPosition ?: 0
            onUpdateProgress(position, duration)
            sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
        }
    }

    //Service委托代理人
    private var mPlayerBinder: MusicPlayerBinder? = null
    override fun onCreate() {
        super.onCreate()
        initPlayerConfig()

    }

    /**
     * 初始化播放器
     */
    private fun initPlayerConfig() {
        mExoPlayer.addListener(this)

    }

    /**MusicPlayerPresenter方法实现*/
    override fun onBind(intent: Intent): IBinder {
        if (null == mPlayerBinder) {
            mPlayerBinder = MusicPlayerBinder(this)
        }
        return mPlayerBinder as MusicPlayerBinder
    }

    val RADIO_URL = "http://kastos.cdnstream.com/1345_32"

    @Synchronized
    private fun startPlay(musicInfo: BaseAudioInfo) {
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(RADIO_URL))
        concatenatingMediaSource.addMediaSource(mediaSource)
        with(mExoPlayer) {
            prepare(mediaSource)
            playWhenReady = true
        }

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
        TODO("Not yet implemented")
    }

    override fun playLastMusic() {
        TODO("Not yet implemented")
    }

    override fun playNextMusic() {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDurtion(): Long {
        TODO("Not yet implemented")
    }

    override fun getCurrentPlayerID(): Long {
        TODO("Not yet implemented")
    }

    override fun seekTo(currentTime: Long) {
        TODO("Not yet implemented")
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
    /**--------播放器回调-----------*/

    /**
     * 播放错误信息
     */
    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
        exoLog("onPlayerStateChanged")
    }

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
        exoLog(mExoPlayer.getCurrentWindowIndex())
    }

    override fun onPositionDiscontinuity(reason: Int) {
        exoLog(mExoPlayer.currentPosition)

    }

    /**
     * 更新播放进度
     */
    private fun onUpdateProgress(currentPosition: Long, duration: Long) {

        mOnPlayerEventListeners.forEach {
            it.onTaskRuntime(0,currentPosition,0,0)
        }
    }
}