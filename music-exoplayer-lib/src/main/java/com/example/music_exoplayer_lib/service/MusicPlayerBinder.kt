package com.example.music_exoplayer_lib.service

import android.os.Binder
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.example.music_exoplayer_lib.listener.MusicPlayerEventListener

/**
 *
 * @des:MusicPlayerService 代理类
 * @data: 8/19/20 3:05 PM
 * @Version: 1.0.0
 */
class MusicPlayerBinder constructor(val presenter:MusicPlayerPresenter): Binder() {

    fun startPlayMusic(position: Int) {
        presenter.startPlayerMusic(0)
    }
    //暂停
    fun pause(){
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
}