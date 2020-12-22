package com.rm.music_exoplayer_lib.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioManager
import android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
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
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger.exoLog
import kotlin.collections.ArrayList
import kotlin.random.Random


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
    private val mAdListener = AdPlayerEventListener()

    //播放器工作状态
    private var mMusicPlayerState = MUSIC_PLAYER_STOP

    //播放器绝对路径、锁屏绝对路径
    private var mLockActivityClass: String? = null

    //当前播放播放器正在处理的对象位置
    private var mCurrentPlayIndex = 0

    //待播放音频队列池子
    private val mAudios = ArrayList<BaseAudioInfo>()
    private val mAdAudios = ArrayList<BaseAudioInfo>()


    //监听系统事件的广播
    private var mHeadsetBroadcastReceiver: AlarmBroadcastReceiver? = null

    //用户设定的内部播放器播放模式，默认顺序
    private var mPlayModel = MUSIC_MODEL_ORDER
    private var mNotificationEnable: Boolean = true

    //播放速度
    var playerMultiples = 1f

    //显示播放状态而不重新播放
    var showState = false
    var isPlayAd = false


    private val notificationManger by lazy {
        getCurrentPlayerMusic()?.let {
            NotificationManger(this, it, getPlayerState())
        }
    }

    //是否被动暂停，用来处理音频焦点失去标记
    private var mIsPassive = false
    private val mExoPlayer: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build()
        }
    }
    private val dataSourceFactory: DataSource.Factory by lazy {

        DefaultDataSourceFactory(
            this,
            getUserAgent(this, this.packageName)
        )
    }
    val mAudioFocusManager by lazy {
        MusicAudioFocusManager(applicationContext)
    }

    //进度条消息
    @SuppressLint("HandlerLeak")
    private val mUpdateProgressHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            val duration =
                (getCurrentPlayerMusic()?.duration ?: mExoPlayer.contentDuration)
            val currentPosition = mExoPlayer.contentPosition
            onUpdateProgress(currentPosition, duration)
            sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val CHANNEL_ID = "CHANNEL_ID"
        val CHANNEL_NAME = "听书"
        val notificationChannel: NotificationChannel
        //进行8.0的判断
        //进行8.0的判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }

        val notifyIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.baidu.com")
        )
        val pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0)

        var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, CHANNEL_ID)
                .setTicker("听书")
                .setContentTitle("听书标题")
                .setContentIntent(pendingIntent)
                .setContentText("听书需要启动音乐播放器服务")
                .build()
            notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
            startForeground(1, notification)
        }

        return START_STICKY
    }

    //音频焦点
    var requestAudioFocus = AUDIOFOCUS_REQUEST_GRANTED

    //Service委托代理人
    private var mPlayerBinder: MusicPlayerBinder? = null


    override fun onCreate() {
        super.onCreate()
        mAudioFocusManager.setFocusListener(
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

                override fun onFocusSeize(i: Int) {
                    //音频被抢占
                    passivePause()
                    requestAudioFocus = i
                }

                /**
                 * 返回播放器是否正在播放
                 * @return 为true正在播放
                 */
                override val isPlaying: Boolean
                    get() = isPlaying()
            })
        registerReceiver()
        initPlayerConfig()
    }

    //注册广播
    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
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
        showNotification()
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
                mExoPlayer.removeListener(mAdListener)
                mExoPlayer.addListener(mEventListener)
                mExoPlayer.prepare(source)
                mExoPlayer.playWhenReady = true
            } else {
                exoLog("没有链接")
            }
        } else {
            exoLog("未成功获取音频输出焦点")
        }

    private fun startPlayAd(adPath: String) {
        mUpdateProgressHandler.removeMessages(0)
        if (requestAudioFocus == AUDIOFOCUS_REQUEST_GRANTED) {
            isPlayAd = true
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(adPath))
            mExoPlayer.removeListener(mEventListener)
            mExoPlayer.addListener(mAdListener)
            mExoPlayer.prepare(source)
            mExoPlayer.playWhenReady = true
        }
    }

    override fun startPlayMusic(chapterId: String) {
        mCurrentPlayIndex = mAudios.indexOfFirst { it.chapterId == chapterId }
        postViewHandlerCurrentPosition(mCurrentPlayIndex)
        if(mAdAudios.isNotEmpty()){
            startPlayAd(mAdAudios[Random.nextInt(mAdAudios.size)].audioPath)
        }else{
            playChapter()
        }
    }

    private fun startPlayMusic(playIndex: Int) {
        mCurrentPlayIndex = playIndex
        postViewHandlerCurrentPosition(mCurrentPlayIndex)
        if(mAdAudios.isNotEmpty()){
            startPlayAd(mAdAudios[Random.nextInt(mAdAudios.size)].audioPath)
        }else{
            playChapter()
        }
    }

    private fun playChapter(){
        if (mCurrentPlayIndex != -1 && mCurrentPlayIndex < mAudios.size) {
            startPlay(mAudios[mCurrentPlayIndex])
        }
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
                    pause()
                }
                MUSIC_PLAYER_PAUSE -> {
                    play()
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
            notificationManger?.showNotification(this, it, "")
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
        mAudioFocusManager.releaseAudioFocus()
    }

    override fun playLastMusic() {
        when (mPlayModel) {
            //顺序播放
            MUSIC_MODEL_ORDER -> {
                if (mCurrentPlayIndex > 0) {
                    mCurrentPlayIndex--
                }
//                resetMusicAlarmModel()
                startPlayMusic(mCurrentPlayIndex)
            }
            //单曲循环
            MUSIC_MODEL_SINGLE -> {
                mCurrentPlayIndex--
                if (mCurrentPlayIndex < 0) {
                    mCurrentPlayIndex = mAudios.size - 1
                }
//                resetMusicAlarmModel()
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
        return mExoPlayer.playWhenReady
    }

    override fun getDurtion(): Long {
        //播放总长度
        return mExoPlayer.contentDuration
    }

    override fun getCurrentPlayerID(): Long {
        return 0L
    }

    override fun seekTo(currentTime: Long) {
        if (currentTime <= 0) {
            mExoPlayer.seekTo(0L)
        } else {
            mExoPlayer.seekTo(currentTime)
        }
    }

    override fun getCurrentPlayerMusic(): BaseAudioInfo? =
        mAudios.getOrNull(mCurrentPlayIndex)

    override fun getCurrentPlayList(): List<*> {
        return mAudios
    }

    override fun setPlayInfoListener(listener: MusicPlayerInfoListener) {

    }

    override fun removePlayInfoListener() {

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
        this.playerMultiples = p
        mExoPlayer.setPlaybackParameters(PlaybackParameters(p, 1.0f))
    }

    override fun getCurDurtion(): Long {
        return mExoPlayer.currentPosition
    }

    override fun setAlarm(times: Int) {
    }


    override fun updateMusicPlayerData(audios: List<BaseAudioInfo>, chapterId: String) {
        mAudios.clear()
        mAudios.addAll(audios)
        mCurrentPlayIndex = audios.indexOfFirst { it.chapterId == chapterId }
    }

    /**
     * 播放完成
     */
    private fun onFinishPlay() {
        mUpdateProgressHandler.removeMessages(0)
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
                    if (mAudios.size > currentPlayIndex && currentPlayIndex >= 0) {
            mOnPlayerEventListeners.forEach {
                it.onPlayMusiconInfo(
                    mAudios[currentPlayIndex], currentPlayIndex
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


    override fun setNotificationEnable(enable: Boolean) {
        this.mNotificationEnable = enable

    }

    override fun getServiceName(): String =
        "com.rm.music_exoplayer_lib.service.${MusicPlayerService::class.simpleName.toString()}"


    override fun getPlayerMultiple(): Float = playerMultiples
    override fun resumePlayState(state: Boolean) {
        if (requestAudioFocus != 1) {
            requestAudioFocus = mAudioFocusManager.requestAudioFocus()
        }
        showState = state
    }

    override fun getCurrentPlayIndex(): Int {
        return mCurrentPlayIndex
    }

    override fun setAdPath(adPathList: ArrayList<BaseAudioInfo>) {
        mAdAudios.clear()
        mAdAudios.addAll(adPathList)
    }

    /**
     * 播放器设计模式
     */
    private fun initPlayerConfig() {
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
                //正在播放
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    if (!mUpdateProgressHandler.hasMessages(0)) {
                        mUpdateProgressHandler.sendEmptyMessageDelayed(0, UPDATE_PROGRESS_DELAY)
                        mMusicPlayerState = MUSIC_PLAYER_PLAYING
                    }
                    mMusicPlayerState =
                        if (playWhenReady) MUSIC_PLAYER_PLAYING else MUSIC_PLAYER_PAUSE
                }
                //播放结束
                Player.STATE_ENDED -> {
                    onFinishPlay()
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
            mOnPlayerEventListeners.forEach {
                it.onMusicPlayerState(-1, error.message)
            }

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
            mOnPlayerEventListeners.forEach {
                it.onStopPlayAd()
                if (isPlaying) {
                    it.onPrepared(getDurtion())
                }
            }
        }
    }

    private inner class AdPlayerEventListener : Player.EventListener {
        /**
         * 播放状态改变
         */
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

            when (playbackState) {
                //正在播放
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    mMusicPlayerState = if (playWhenReady) MUSIC_PLAYER_PLAYING else MUSIC_PLAYER_PAUSE
                    mOnPlayerEventListeners.forEach {
                        it.onPlayerStateChanged(playWhenReady, playbackState)
                    }
                }
                //播放结束
                Player.STATE_ENDED -> {
                    mOnPlayerEventListeners.forEach {
                        it.onStopPlayAd()
                    }
                    playChapter()
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
            mOnPlayerEventListeners.forEach {
                it.onMusicPlayerState(-1, error.message)
            }
            playChapter()
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
                    it.onStartPlayAd()
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

