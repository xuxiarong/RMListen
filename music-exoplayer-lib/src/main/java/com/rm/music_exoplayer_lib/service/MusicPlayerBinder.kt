package com.rm.music_exoplayer_lib.service

import android.os.Binder
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener

/**
 *
 * @des:MusicPlayerService 代理类
 * @data: 8/19/20 3:05 PM
 * @Version: 1.0.0
 */
class MusicPlayerBinder constructor(val presenter: MusicPlayerPresenter) : Binder() {

    fun startPlayMusic(position: Int) {
        presenter.startPlayMusic(0)
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
}