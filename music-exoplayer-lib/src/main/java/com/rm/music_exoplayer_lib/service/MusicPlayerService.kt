package com.rm.music_exoplayer_lib.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.manager.AlarmManger
import com.rm.music_exoplayer_lib.manager.MusicAudioFocusManager
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger.exoLog
import java.util.*

/**
 * desc   :播放器核心类
 * date   : 2020/08/13
 * version: 1.0
 */
internal class MusicPlayerService : Service(), MusicPlayerPresenter {
    //更新播放进度时间频率
    val UPDATE_PROGRESS_DELAY = 500L
    private val mOnPlayerEventListeners = arrayListOf<MusicPlayerEventListener>()
    private val mEventListener = ExoPlayerEventListener()

    //播放器工作状态
    private val mMusicPlayerState = MUSIC_PLAYER_STOP

    //当前播放播放器正在处理的对象位置
    private var mCurrentPlayIndex = 0

    //待播放音频队列池子
    private val mAudios = ArrayList<Any>()

    //是否被动暂停，用来处理音频焦点失去标记
    private var mIsPassive = false
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

    //音频管理
    private var mAudioFocusManager: MusicAudioFocusManager? = null

    //进度条消息
    @SuppressLint("HandlerLeak")
    private val mUpdateProgressHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val duration = mExoPlayer.contentDuration
            val currentPosition = mExoPlayer.contentPosition
            exoLog("duration===>${duration.toInt()}")
            onUpdateProgress(currentPosition, duration)
            sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
        }
    }

    //音频焦点
    var requestAudioFocus = -1

    //Service委托代理人
    private var mPlayerBinder: MusicPlayerBinder? = null

    override fun onCreate() {
        super.onCreate()
        mAudioFocusManager = MusicAudioFocusManager(this.applicationContext)
        requestAudioFocus = mAudioFocusManager?.requestAudioFocus(
            object : MusicAudioFocusManager.OnAudioFocusListener {
                /**
                 * 恢复音频输出焦点，这里恢复播放需要和用户调用恢复播放有区别
                 * 因为当用户主动暂停，获取到音频焦点后不应该恢复播放，而是继续保持暂停状态
                 */
                override fun onFocusGet() {
                    //如果是被动失去焦点的，则继续播放，否则继续保持暂停状态
                    if (mIsPassive) {
                        play()
                    }
                }

                /**
                 * 失去音频焦点后暂停播放，这里暂停播放需要和用户主动暂停有区别，做一个标记，配合onResume。
                 * 当获取到音频焦点后，根据onResume根据标识状态看是否需要恢复播放
                 */
                override fun onFocusOut() {
                    passivePause()
                }

                /**
                 * 返回播放器是否正在播放
                 * @return 为true正在播放
                 */
                override val isPlaying: Boolean
                    get() = isPlaying()
            }) ?: -1


    }

    /**
     * 被动暂停播放，仅提供给失去焦点时内部调用
     */
    private fun passivePause() {
        mExoPlayer.playWhenReady = false
        this.mIsPassive = true
    }

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
    private fun startPlay(musicInfo: BaseAudioInfo) =
        if (requestAudioFocus == AUDIOFOCUS_REQUEST_GRANTED) {
            mExoPlayer.prepare(
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(RADIO_URL))
            )
            mExoPlayer.playWhenReady = true
        } else {
            exoLog("未成功获取音频输出焦点")
        }

    override fun startPlayMusic(index: Int) {
        startPlay(BaseAudioInfo())
    }

    override fun startPlayMusic(audios: List<*>?, index: Int) {
        TODO("Not yet implemented")
    }

    override fun playOrPause() {
        if (mAudios.size > 0) {
            when (mMusicPlayerState) {
                MUSIC_PLAYER_STOP -> {
                    startPlayMusic(mCurrentPlayIndex)
                }
                MUSIC_PLAYER_PREPARE -> {

                }
                MUSIC_PLAYER_BUFFER -> {
                }
                MUSIC_PLAYER_PLAYING -> {

                }
                MUSIC_PLAYER_PAUSE -> {
                }
                MUSIC_PLAYER_ERROR -> {

                }
                else -> {

                }
            }
        }

    }

    override fun pause() {
        mExoPlayer.playWhenReady = false
        mUpdateProgressHandler.removeMessages(0)
    }

    /**
     * 恢复播放
     */
    override fun play() {
        mExoPlayer.playWhenReady = true
        this.mIsPassive = false
    }

    override fun setLoop(loop: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        mUpdateProgressHandler.removeMessages(0)
        mExoPlayer.release()
        mExoPlayer.removeListener(mEventListener)
        mAudioFocusManager?.releaseAudioFocus()
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
        mExoPlayer.setPlaybackParameters(PlaybackParameters(p, 1.0f))
    }

    override fun getCurDurtion(): Long {
        return mExoPlayer.currentPosition
    }

    override fun setAlarm(times: Int) {
        AlarmManger(this.applicationContext).setAlarm(times)
    }

    override fun updateMusicPlayerData(audios: List<*>, index: Int) {

        mAudios.clear()
        mAudios.addAll(listOf(audios))
        mCurrentPlayIndex = index
    }

    override fun getPlayerState(): Int = mMusicPlayerState

    /**z
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
            if (isPlaying) {
                mOnPlayerEventListeners.forEach {
                    it.onPrepared(getDurtion())
                }
            }
        }
    }
}
