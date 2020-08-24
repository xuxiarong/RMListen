package com.rm.music_exoplayer_lib.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.service.MusicPlayerBinder
import com.rm.music_exoplayer_lib.service.MusicPlayerService

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


    internal class MusicPlayerServiceConnection : ServiceConnection {
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
        mConnection = MusicPlayerServiceConnection()
        mConnection?.let {
            val intent = Intent(context, MusicPlayerService::class.java)
            context.startService(intent)
            context.bindService(
                intent, it,
                Context.BIND_AUTO_CREATE
            )
            callBack.onSuccess()
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
        mBinder?.pause()
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
        return mBinder?.getDurtion() ?: 0
    }

    override fun getCurrentPlayerID(): Long {
        TODO("Not yet implemented")
    }

    override fun seekTo(currentTime: Long) {
        mBinder?.seekTo(currentTime)
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
        mBinder?.setOnPlayerEventListener(listener)
    }

    override fun removePlayerListener(listener: MusicPlayerEventListener) {
        mBinder?.removePlayerListener(listener);
    }

    override fun removeAllPlayerListener() {
        mBinder?.removeAllPlayerListener();
    }

    override fun setPlayerMultiple(p: Float) {
        mBinder?.setPlayerMultiple(p)
    }

    override fun getCurDurtion(): Long {
        return mBinder?.getCurDurtion()?:0
    }

    override fun setAlarm(times: Int) {
        mBinder?.setAlarm(times)
    }

}