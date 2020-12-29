package com.rm.music_exoplayer_lib.listener

import com.rm.music_exoplayer_lib.bean.BaseAudioInfo

/**
 *
 * @des:
 * @data: 8/19/20 3:21 PM
 * @Version: 1.0.0
 */
interface MusicPlayerEventListener {
    /**
     * 播放器所有状态回调
     * @param playerState 播放器内部状态
     */
    fun onMusicPlayerState(playerState: Int, message: String?)

    /**
     * 播放器准备好了
     * @param totalDurtion 总时长
     */
    fun onPrepared(totalDurtion: Long,isAd: Boolean)

    /**
     * 缓冲百分比
     * @param percent 百分比
     * 此方法已被废弃，缓冲进度播放器内部使用变量储存，每隔500mm连同播放进度回调至组件
     * 合并至onTaskRuntime方法
     */
    fun onBufferingUpdate(percent: Int)

    /**
     * 播放器反馈信息
     * @param event 事件
     * @param extra
     */
    fun onInfo(event: Int, extra: Int)

    /**
     * 当前正在播放的任务
     * @param musicInfo 正在播放的对象
     * @param position 当前正在播放的位置
     */
    fun onPlayMusiconInfo(
        musicInfo: BaseAudioInfo,
        position: Int
    )

    /**
     * 音频地址无效,组件可处理付费购买等逻辑
     * @param musicInfo 播放对象
     * @param position 索引
     */
    fun onMusicPathInvalid(
        musicInfo: BaseAudioInfo,
        position: Int
    )

    /**
     * @param totalDurtion 音频总时间
     * @param currentDurtion 当前播放的位置
     * @param alarmResidueDurtion 闹钟剩余时长
     * @param bufferProgress 当前缓冲进度
     */
    fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    )

    /**
     * @param playModel 播放模式
     * @param alarmModel 闹钟模式
     * @param isToast 是否吐司提示
     */
    fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean)

    /**
     * 播放状态
     */
    fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)

    /**
     * 开始播放广告
     */
    fun onStartPlayAd()

    /**
     * 结束播放广告
     */
    fun onStopPlayAd()

}