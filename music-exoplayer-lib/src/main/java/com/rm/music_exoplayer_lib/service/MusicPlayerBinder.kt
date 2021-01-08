package com.rm.music_exoplayer_lib.service

import android.app.Notification
import android.os.Binder
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager

/**
 *
 * @des:MusicPlayerService 代理类
 * @data: 8/19/20 3:05 PM
 * @Version: 1.0.0
 */
class MusicPlayerBinder constructor(val presenter: MusicPlayerPresenter) : Binder() {

    fun startPlayMusic(position: String) {
        presenter.startPlayMusic(position)
    }

    //暂停
    fun pause() {
        presenter.pause()
    }

    fun setOnPlayerEventListener(listener: MusicPlayerEventListener) {
        presenter.addOnPlayerEventListener(listener)
    }

    fun removePlayerListener(listener: MusicPlayerEventListener) {
        presenter.removePlayerListener(listener)
    }

    fun removeAllPlayerListener() {
        presenter.removeAllPlayerListener()
    }

    fun getDurtion(): Long {
        //播放总长度
        return presenter.getDurtion()
    }

    /**
     * 播放倍数
     */
    fun setPlayerMultiple(p: Float) {
        presenter.setPlayerMultiple(p)
    }

    fun seekTo(currentTime: Long) {
        presenter.seekTo(currentTime)
    }

    /**
     * 返回当前播放时长
     */
    fun getCurDurtion(): Long {
        return presenter.getCurDurtion()
    }

    /**
     * 定时关闭播放器
     */
    fun setAlarm(times: Int) {
        presenter.setAlarm(times)
    }

    fun playLastMusic() {
        presenter.playLastMusic()
    }

    fun playNextMusic() {
        presenter.playNextMusic()
    }

    /**
     * 更新播放器
     */
    fun updateMusicPlayerData(audios: List<BaseAudioInfo>, index: String) {
        presenter.updateMusicPlayerData(audios, index)
    }

    //获取播放状态
    fun getPlayerState(): Int = presenter.getPlayerState()

    //播放或者暂停
    fun playOrPause() {
        presenter.playOrPause()
    }

    //播放
    fun play() {
        presenter.play()
    }

    //当前播放
    fun getCurrentPlayerMusic(): BaseAudioInfo? = presenter.getCurrentPlayerMusic()

    //锁屏对象
    fun setLockActivityName(className: String) {
        presenter.setLockActivityName(className)
    }

    //等待播放队列
    fun getCurrentPlayList(): List<*>? = presenter.getCurrentPlayList()

    fun isPlaying(): Boolean = presenter.isPlaying()

    fun setDefaultAlarmModel(defaultAlarmModel: Int) {
        presenter.setDefaultAlarmModel(defaultAlarmModel)
    }

    fun setDefaultPlayModel(defaultPlayModel: Int) {
        presenter.setDefaultPlayModel(defaultPlayModel)
    }


    fun startServiceForeground() {
        TODO("Not yet implemented")
    }

    fun startServiceForeground(notification: Notification?) {
        TODO("Not yet implemented")
    }

    fun startServiceForeground(notification: Notification?, notifiid: Int) {
        TODO("Not yet implemented")
    }

    fun stopServiceForeground() {
        TODO("Not yet implemented")
    }

    fun startNotification() {
        TODO("Not yet implemented")
    }

    fun startNotification(notification: Notification?) {
        TODO("Not yet implemented")
    }

    fun startNotification(notification: Notification?, notifiid: Int) {
        TODO("Not yet implemented")
    }

    fun updateNotification() {
        TODO("Not yet implemented")
    }

    fun cleanNotification() {
        presenter.cleanNotification()
    }

    fun setPlayerModel(model: Int): Int {
        presenter.setPlayerModel(model)
        return model
    }

    fun getPlayerModel(): Int = presenter.getPlayerModel()

    fun setNotificationEnable(enable: Boolean) {
        presenter.setNotificationEnable(enable)

    }

    fun getServiceName(): String = presenter.getServiceName()

    //获取播放速度
    fun getPlayerMultiple(): Float = presenter.getPlayerMultiple()


    //恢复页面播放状态
    fun resumePlayState(state:Boolean)=presenter.resumePlayState(state)

    fun getCurrentPlayIndex()  = presenter.getCurrentPlayIndex()

    fun setAdPath(adPathList: ArrayList<BaseAudioInfo>) = presenter.setAdPath(adPathList)
}