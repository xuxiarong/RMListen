package com.rm.music_exoplayer_lib.iinterface

import android.app.Notification
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager

/**
 *
 * @des:播放器接口
 * @data: 8/19/20 3:11 PM
 * @Version: 1.0.0.0
 */
interface MusicPlayerPresenter {
    /**
     * 开始播放指定位置音频文件
     * @param index 指定的位置 0-data.size()
     */
    fun startPlayMusic(chapterId: String)

    /**
     * 开始播放任务
     * @param audios 待播放的数据集，对象需要继承BaseaudioInfo
     * @param index 指定要播放的位置 0-data.size()
     */
    fun startPlayMusic(audios: List<*>?, chapterId: String)

    /**
     * 开始、暂停
     */
    fun playOrPause()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 恢复播放
     */
    fun play()

    /**
     * 设置循环模式
     * @param loop 为true循环播放
     */
    fun setLoop(loop: Boolean)

    /**
     * 播放器停止工作
     */
    fun onStop()

    /**
     * 播放上一首，内部维持上一首逻辑
     */
    fun playLastMusic()

    /**
     * 播放下一首，内部维持下一首逻辑
     */
    fun playNextMusic()

    /**
     * 试探上一首的位置，不会启动播放任务
     * @return 上一首的位置
     */
    fun playLastIndex(): Int

    /**
     * 试探下一首的位置，不会启动播放任务
     * @return 下一首的位置
     */
    fun playNextIndex(): Int

    /**
     * 播放器内部播放状态
     * @return 为true正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 返回正在播放的音频总时长
     * @return 单位毫秒
     */
    fun getDurtion(): Long

    /**
     * 返回正在播放的音频ID
     * @return 音频ID
     */
    fun getCurrentPlayerID(): Long

    /**
     * 跳转至某处播放
     * @param currentTime 时间位置，单位毫秒
     */
    fun seekTo(currentTime: Long)

    /**
     * 返回正在播放的音频对象
     * @return 音频对象
     */
    fun getCurrentPlayerMusic(): BaseAudioInfo?

    /**
     * 返回内部播放器正在播放的队列
     * @return 播放器持有的播放数据集
     */
    fun getCurrentPlayList(): List<*>?

    /**
     * 设置播放对象监听
     * @param listener 实现监听器的对象
     */
    fun setPlayInfoListener(listener: MusicPlayerInfoListener)

    /**
     * 移除监听播放对象事件
     */
    fun removePlayInfoListener()

    /**
     * 添加一个播放状态监听器到监听器池子
     * @param listener 实现监听器的对象
     */
    fun addOnPlayerEventListener(listener: MusicPlayerEventListener)

    /**
     * 从监听器池子中移除一个监听器
     * @param listener 实现监听器的对象
     */
    fun removePlayerListener(listener: MusicPlayerEventListener)

    /**
     * 清空监听器池子所有的监听器对象
     */
    fun removeAllPlayerListener()

    /**
     * 播放倍数
     */
    fun setPlayerMultiple(p: Float)

    /**
     * 返回当前时长
     */
    fun getCurDurtion(): Long

    /**
     * 设置定时关闭
     */
    fun setAlarm(times: Int)

    /**
     * 更新内部播放器的数据集合位置，此方法适合在外部列表已经改变了，需同步至内部播放器
     * @param audios 待播放列表
     * @param index 位置
     */
    fun updateMusicPlayerData(audios: List<BaseAudioInfo>, chapterId: String)

    /**
     * 返回播放器内部工作状态
     * @return 播放状态，详见MusicConstants常量定义
     */
    fun getPlayerState(): Int

    /**
     * 设置锁屏界面
     * @param className Activity绝对路径
     * @return MusicPlayerManager
     */
    fun setLockActivityName(className: String): MusicPlayerManager?

    /**
     * 设置闹钟模式
     */
    fun setDefaultAlarmModel(defaultAlarmModel: Int)

    /**
     * 设置播放模式
     */
    fun setDefaultPlayModel(defaultPlayModel: Int)


    /**
     * 打开常驻进程，在通知栏创建一个守护通知
     */
    fun startServiceForeground()

    /**
     * 打开常驻进程
     * @param notification 通知对象
     */
    fun startServiceForeground(notification: Notification?)

    /**
     * 打开常驻进程
     * @param notification 通知对象
     * @param notifiid 通知ID
     */
    fun startServiceForeground(
        notification: Notification?,
        notifiid: Int
    )

    /**
     * 结束常驻进程并清除默认通知
     */
    fun stopServiceForeground()

    /**
     * 打开常驻进程，在通知栏创建一个守护通知
     */
    fun startNotification()

    /**
     * 打开常驻进程
     * @param notification 通知对象
     */
    fun startNotification(notification: Notification?)

    /**
     * 打开常驻进程
     * @param notification 通知对象
     * @param notifiid 通知ID
     */
    fun startNotification(notification: Notification?, notifiid: Int)

    /**
     * 更新通知栏，一般在使用内部默认的通知栏时，收藏了音频后调用
     */
    fun updateNotification()

    /**
     * 结束常驻进程并清除默认通知
     */
    fun cleanNotification()

    /**
     * 设置播放模式
     * @param model 播放模式，参考MusicConstants定义
     * @return 成功设置的播放模式
     */
    fun setPlayerModel(model: Int): Int

    //获取播放模式
    fun getPlayerModel(): Int

    //设置定时模式
    fun setPlayerAlarmModel(model: Int)

    //获取闹钟模式
    fun getPlayerAlarmModel(): Int

    //获取闹钟时长
    fun getPlayerAlarmTime(): Long

    /**
     * 开启通知
     */
    fun setNotificationEnable(enable: Boolean)

    /**
     * 服务名称
     */
    fun getServiceName(): String

    //剩余集数
    fun getRemainingSetInt(): Int

    /**
     * 获取配速
     */
    fun getPlayerMultiple():Float

    //恢复页面播放状态
    fun resumePlayState(state:Boolean)



}