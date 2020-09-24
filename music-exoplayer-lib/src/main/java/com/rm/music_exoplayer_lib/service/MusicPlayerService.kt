package com.rm.music_exoplayer_lib.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.music_exoplayer_lib.manager.BookAlarmManger
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.manager.MusicAudioFocusManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.notification.NOTIFICATION_ID
import com.rm.music_exoplayer_lib.notification.NotificationManger
import com.rm.music_exoplayer_lib.receiver.AlarmBroadcastReceiver
import com.rm.music_exoplayer_lib.utils.CacheUtils
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
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
    private var mMusicPlayerState = MUSIC_PLAYER_STOP

    //播放器绝对路径、锁屏绝对路径
    private var mLockActivityClass: String? = null

    //当前播放播放器正在处理的对象位置
    private var mCurrentPlayIndex = 0

    //待播放音频队列池子
    private val mAudios = ArrayList<Any>()

    //监听系统事件的广播
    private var mHeadsetBroadcastReceiver: AlarmBroadcastReceiver? = null

    //用户设定的内部播放器播放模式，默认顺序
    private var mPlayModel = MUSIC_MODEL_ORDER
    private var mNotificationEnable: Boolean = true

    //用户设定的闹钟模式,默认:MusicAlarmModel.MUSIC_ALARM_MODEL_0
    private var mMusicAlarmModel = MUSIC_ALARM_MODEL_0

    //自动停止播放器的剩余时间
    private var TIMER_DURTION = Long.MAX_VALUE

    //循环模式
    private val mLoop = false

    val notificationManger by lazy {
        getCurrentPlayerMusic()?.let {
            NotificationManger(this, it, getPlayerState())

        }
    }
    val alarmManger by lazy {
        BookAlarmManger(this)
    }

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
    private var cachedDataSourceFactory: CacheDataSourceFactory? = null
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(
            this,
            getUserAgent(this, this.packageName)
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
            onUpdateProgress(currentPosition, duration)
            sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_NOT_STICKY

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

        registerReceiver()
        initPlayerConfig()
        initAlarmConfig()
    }

    //注册广播
    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
//        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(ACTION_ALARM_REPLENISH_STOCK)
        intentFilter.addAction(ACTION_ALARM_SYNCHRONIZE)
        intentFilter.addAction(MUSIC_INTENT_ACTION_ROOT_VIEW)
        intentFilter.addAction(MUSIC_INTENT_ACTION_CLICK_LAST)
        intentFilter.addAction(MUSIC_INTENT_ACTION_CLICK_NEXT)
        intentFilter.addAction(MUSIC_INTENT_ACTION_CLICK_PAUSE)
        intentFilter.addAction(MUSIC_INTENT_ACTION_CLICK_CLOSE)
        intentFilter.addAction(MUSIC_INTENT_ACTION_CLICK_COLLECT)
        mHeadsetBroadcastReceiver = AlarmBroadcastReceiver()
        registerReceiver(mHeadsetBroadcastReceiver, intentFilter)
    }

    /**
     * 被动暂停播放，仅提供给失去焦点时内部调用
     */
    private fun passivePause() {
        mExoPlayer.playWhenReady = false
        this.mIsPassive = true
        showNotification();
    }

    /**MusicPlayerPresenter方法实现*/
    override fun onBind(intent: Intent): IBinder {
        if (null == mPlayerBinder) {
            mPlayerBinder = MusicPlayerBinder(this)
        }
        return mPlayerBinder as MusicPlayerBinder
    }

    private fun startPlay(musicInfo: BaseAudioInfo) =
        if (requestAudioFocus == AUDIOFOCUS_REQUEST_GRANTED) {
            if (musicInfo.audioPath.isNotEmpty()) {
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(musicInfo.audioPath))
                mExoPlayer.prepare(
                    source
                )
                mExoPlayer.playWhenReady = true
            } else {
                exoLog("没有链接")
            }
        } else {
            exoLog("未成功获取音频输出焦点")
        }

    override fun startPlayMusic(index: Int) {
        postViewHandlerCurrentPosition(index)
        startPlay(mAudios.getOrNull(index) as BaseAudioInfo)
    }

    override fun startPlayMusic(audios: List<*>?, index: Int) {

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
                    mExoPlayer.playWhenReady = false

                }
                MUSIC_PLAYER_PAUSE -> {
                    mIsPassive = false
                    mExoPlayer.playWhenReady = true
                }
                MUSIC_PLAYER_ERROR -> {

                }
                else -> {

                }
            }
        }

    }

    //暂停
    override fun pause() {
        mExoPlayer.playWhenReady = false
        mUpdateProgressHandler.removeMessages(0)
        showNotification()
    }

    fun showNotification() {
        getCurrentPlayerMusic()?.let {
            notificationManger?.showNotification(this, it)

        }

    }

    /**
     * 恢复播放
     */
    override fun play() {
        mExoPlayer.playWhenReady = true
        this.mIsPassive = false
        showNotification()
    }

    override fun setLoop(loop: Boolean) {
    }

    override fun onStop() {
        mUpdateProgressHandler.removeMessages(0)
        mExoPlayer.release()
        mExoPlayer.removeListener(mEventListener)
        mAudioFocusManager?.releaseAudioFocus()
    }

    override fun playLastMusic() {
        when (mPlayModel) {
            //顺序播放
            MUSIC_MODEL_ORDER -> {
                if (mCurrentPlayIndex > 0) {
                    mCurrentPlayIndex--
                }
                startPlayMusic(mCurrentPlayIndex)
            }
            //单曲循环
            MUSIC_MODEL_SINGLE -> {
                mCurrentPlayIndex--
                if (mCurrentPlayIndex < 0) {
                    mCurrentPlayIndex = mAudios.size - 1
                }
                startPlayMusic(mCurrentPlayIndex)
            }
            else -> {
            }
        }
    }

    //切换到下一首
    override fun playNextMusic() {
        when (mPlayModel) {
            //顺序播放
            MUSIC_MODEL_ORDER -> {
                if ((mAudios.size - 1) > mCurrentPlayIndex) {
                    mCurrentPlayIndex++
                }
                startPlayMusic(mCurrentPlayIndex)
            }
            //单曲循环
            MUSIC_MODEL_SINGLE -> {
                if (mCurrentPlayIndex >= mAudios.size - 1) {
                    mCurrentPlayIndex = 0
                } else {
                    mCurrentPlayIndex++
                }
                startPlayMusic(mCurrentPlayIndex)
            }
            else -> {
            }
        }
    }

    override fun playLastIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun playNextIndex(): Int {
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

    override fun getCurrentPlayerMusic(): BaseAudioInfo? =
        mAudios?.getOrNull(mCurrentPlayIndex) as? BaseAudioInfo

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
        if (times > 0) {

        }
    }

    override fun updateMusicPlayerData(audios: List<BaseAudioInfo>, index: Int) {
        mAudios.clear()
        mAudios.addAll(audios)
        mCurrentPlayIndex = index
    }

    /**
     * 播放完成
     */
    private fun onCompletionPlay() {
        when (mPlayModel) {
            //顺序播放
            MUSIC_MODEL_ORDER -> {
                if (mAudios.size - 1 > mCurrentPlayIndex) {
                    mCurrentPlayIndex++
                    startPlayMusic(mCurrentPlayIndex)
                }
            }
            //单曲播放
            MUSIC_MODEL_SINGLE -> {
                startPlayMusic(mCurrentPlayIndex)
            }

        }
        showNotification()

    }

    /**
     * 上报给UI组件，当前内部自动正在处理的对象位置
     * @param currentPlayIndex 数据源中的Index
     */
    private fun postViewHandlerCurrentPosition(currentPlayIndex: Int) {
        //最后更新通知栏
        showNotification()
        if (mAudios.size > currentPlayIndex) {
            mOnPlayerEventListeners.forEach {
                it.onPlayMusiconInfo(
                    (mAudios[currentPlayIndex] as BaseAudioInfo), currentPlayIndex
                )
            }
        }

    }

    override fun getPlayerState(): Int = mMusicPlayerState
    override fun setLockActivityName(className: String): MusicPlayerManager? {
        mLockActivityClass = className
        return null
    }

    override fun setDefaultAlarmModel(defaultAlarmModel: Int) {

    }

    override fun setDefaultPlayModel(defaultPlayModel: Int) {
    }

    override fun startServiceForeground() {
    }

    override fun startServiceForeground(notification: Notification?) {
    }

    override fun startServiceForeground(notification: Notification?, notifiid: Int) {
    }

    override fun stopServiceForeground() {
    }

    override fun startNotification() {
    }

    override fun startNotification(notification: Notification?) {
    }

    override fun startNotification(notification: Notification?, notifiid: Int) {
    }

    override fun updateNotification() {
    }

    /**
     * 清除通知，常驻进程依然保留(如果开启)
     * @param notifiid 通知栏ID
     */
    private fun cleanNotification(notifiid: Int) {
        notificationManger?.mNotificationManager?.cancel(notifiid)
    }

    @SuppressLint("WrongConstant")
    override fun cleanNotification() {
        cleanNotification(NOTIFICATION_ID)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(NOTIFICATION_ID)
            } else {
                stopForeground(true)
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override fun setPlayerModel(model: Int): Int {
        this.mPlayModel = model
        CacheUtils.instance.putInt(PLAY_MODEL, model)
        return mPlayModel

    }

    override fun getPlayerModel(): Int = mPlayModel

    //初始化闹钟
    fun initAlarmConfig() {
        if (CacheUtils.instance.getLong(SP_KEY_ALARM_MODEL_TIME) > System.currentTimeMillis()) {
            alarmManger.setAlarm(CacheUtils.instance.getLong(SP_KEY_ALARM_MODEL_TIME))
        }
    }

    /**
     * 设置闹钟模式
     */
    override fun setPlayerAlarmModel(model: Int) {
        mMusicAlarmModel = model
        CacheUtils.instance.putInt(SP_KEY_ALARM_MODEL, model)
        mMusicAlarmModel = model
        when (model) {
            MUSIC_ALARM_MODEL_10 -> {
                TIMER_DURTION = 10 * 60.toLong()
            }
            MUSIC_ALARM_MODEL_20 -> {
                TIMER_DURTION = 20 * 60.toLong()

            }
            MUSIC_ALARM_MODEL_30 -> {
                TIMER_DURTION = 30 * 60.toLong()

            }
            MUSIC_ALARM_MODEL_40 -> {
                TIMER_DURTION = 40 * 60.toLong()

            }
            MUSIC_ALARM_MODEL_60 -> {
                TIMER_DURTION = 60 * 60.toLong()
            }
            MUSIC_ALARM_MODEL_0 -> {
                TIMER_DURTION = Long.MAX_VALUE

            }
            MUSIC_ALARM_MODEL_CURRENT -> {

            }
        }
        val times = System.currentTimeMillis() + TIMER_DURTION * 1000
        CacheUtils.instance.putLong(
            SP_KEY_ALARM_MODEL_TIME,
            times
        )
        alarmManger.setAlarm(times)

    }

    override fun setNotificationEnable(enable: Boolean) {
        this.mNotificationEnable = enable

    }

    override fun getServiceName(): String ="com.rm.music_exoplayer_lib.service.${MusicPlayerService::class.simpleName.toString()}"

    /**
     * 播放器设计模式
     */
    fun initPlayerConfig() {
        this.mPlayModel = CacheUtils.instance.getInt(PLAY_MODEL, MUSIC_MODEL_ORDER)

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
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            mOnPlayerEventListeners.forEach {
                it.onPlayerStateChanged(playWhenReady, playbackState)
            }
            when (playbackState) {
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    if (!mUpdateProgressHandler.hasMessages(0)) {
                        mUpdateProgressHandler.sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
                        mMusicPlayerState = MUSIC_PLAYER_PLAYING
                    }
                    mMusicPlayerState =
                        if (playWhenReady) MUSIC_PLAYER_PLAYING else MUSIC_PLAYER_PAUSE
                }

                Player.STATE_ENDED -> {
                    mUpdateProgressHandler.removeMessages(0)
                    onCompletionPlay()
                }

                Player.STATE_IDLE -> {
                }
            }
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        }

        override fun onLoadingChanged(isLoading: Boolean) {

        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            exoLog("onPlayerStateChanged===>${playbackSuppressionReason}")
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            mMusicPlayerState = MUSIC_PLAYER_ERROR
            showNotification()
            exoLog("onPlayerError===>${error.message}")

        }

        override fun onSeekProcessed() {
        }

        override fun onPositionDiscontinuity(reason: Int) {
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            exoLog("onPlayerStateChanged===>${shuffleModeEnabled}")

        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                mOnPlayerEventListeners.forEach {
                    it.onPrepared(getDurtion())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHeadsetBroadcastReceiver?.let {
            unregisterReceiver(it)
        }
        mHeadsetBroadcastReceiver = null
    }


}

