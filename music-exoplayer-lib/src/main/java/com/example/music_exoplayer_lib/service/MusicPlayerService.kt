package com.example.music_exoplayer_lib.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.music_exoplayer_lib.bean.BaseAudioInfo
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.example.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.example.music_exoplayer_lib.manager.MusicPlayerManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * desc   :播放器核心类
 * date   : 2020/08/13
 * version: 1.0
 */
internal class MusicPlayerService : Service(), MusicPlayerPresenter, Player.EventListener {

    private val playerMusic by lazy {
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
    private val playProgressHandler by lazy {
        PlayerHandler()
    }

    //进度条刷新
    class PlayerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
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
        playerMusic.addListener(this)
        concatenatingMediaSource.addEventListener(playProgressHandler,
            object : DefaultMediaSourceEventListener() {
                override fun onLoadStarted(
                    windowIndex: Int,
                    mediaPeriodId: MediaSource.MediaPeriodId?,
                    loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                    mediaLoadData: MediaSourceEventListener.MediaLoadData?
                ) {
                    super.onLoadStarted(windowIndex, mediaPeriodId, loadEventInfo, mediaLoadData)

                }
            })
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
        with(playerMusic) {
            prepare(mediaSource)
            playWhenReady=true
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
        TODO("Not yet implemented")
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
    /**--------播放器回调-----------*/

    /**
     * 播放错误信息
     */
    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    /**
     * 播放状态改变
     */
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
//            Player.STATE_IDLE -> //
//            Player.STATE_READY ->
//            Player.STATE_BUFFERING ->
//            Player.STATE_ENDED ->
        }
    }


}