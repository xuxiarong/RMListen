package com.example.music_exoplayer_lib.manager

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.music_exoplayer_lib.bean.BaseAudioInfo
import com.example.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.example.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.example.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.example.music_exoplayer_lib.service.MusicPlayerBinder
import com.example.music_exoplayer_lib.service.MusicPlayerService

/**
 *
 * @des:
 * @data: 8/19/20 3:27 PM
 * @Version: 1.0.0
 */
class MusicPlayerManager private constructor() : MusicPlayerPresenter {

    companion object {
        val musicPlayerManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicPlayerManager()
        }
        private var mBinder: MusicPlayerBinder? = null
        private var mConnection: ServiceConnection? = null
    }


    private class MusicPlayerServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if ((service as? MusicPlayerBinder) != null) {
                mBinder = service

            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    fun initialize(
        context: Context,
        callBack: MusicInitializeCallBack
    ) {
        if (null != context && context is Activity) {
            val mConnection = MusicPlayerServiceConnection()
            val intent = Intent(context, MusicPlayerService::class.java)
            context.startService(intent)
            MusicPlayerManager.mConnection?.let {
                context.bindService(
                    intent, it,
                    Context.BIND_AUTO_CREATE
                )
            }
        } else {
            IllegalStateException("Must pass in Activity type Context!")
        }
    }

    override fun startPlayerMusic(index: Int) {
        mBinder?.startPlayMusic(index)
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

}