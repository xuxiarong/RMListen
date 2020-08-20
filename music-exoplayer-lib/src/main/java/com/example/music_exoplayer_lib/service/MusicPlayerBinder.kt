package com.example.music_exoplayer_lib.service

import android.os.Binder
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter

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
}