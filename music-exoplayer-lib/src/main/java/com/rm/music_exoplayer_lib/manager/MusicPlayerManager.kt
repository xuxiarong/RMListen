package com.rm.music_exoplayer_lib.manager

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.MUSIC_PLAYER_PLAYING
import com.rm.music_exoplayer_lib.constants.MUSIC_PLAYER_PREPARE
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.service.MusicPlayerBinder
import com.rm.music_exoplayer_lib.service.MusicPlayerService
import org.jetbrains.annotations.NotNull

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

        //播放器界面路径、锁屏界面路径、主界面路径
        private var mActivityLockClassName: String? = null //播放器界面路径、锁屏界面路径、主界面路径

    }

    //跳转到锁洁面
    fun startLockActivity(@NotNull context: Context) {
        mActivityLockClassName?.let {
            val playerState: Int = getPlayerState()
            if (playerState == MUSIC_PLAYER_PREPARE || playerState == MUSIC_PLAYER_PLAYING) {
                val startIntent = Intent()
                startIntent.setClassName(context.packageName, it)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                context.applicationContext.startActivity(startIntent)
            }
        }
    }

    internal class MusicPlayerServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as? MusicPlayerBinder)?.let {
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


    override fun startPlayMusic(index: Int) {
        mBinder?.startPlayMusic(index)
    }

    override fun startPlayMusic(audios: List<*>?, index: Int) {
        mBinder?.startPlayMusic(audios, index)
    }

    override fun playOrPause() {
        mBinder?.playOrPause()
    }

    override fun pause() {
        mBinder?.pause()
    }

    override fun play() {
        mBinder?.play()
    }

    override fun setLoop(loop: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun playLastMusic() {
        mBinder?.playLastMusic()
    }

    override fun playNextMusic() {
        mBinder?.playNextMusic()
    }

    override fun playLastIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun playNextIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean = mBinder?.isPlaying() == true
    override fun getDurtion(): Long {
        return mBinder?.getDurtion() ?: 0
    }

    override fun getCurrentPlayerID(): Long {
        TODO("Not yet implemented")
    }

    override fun seekTo(currentTime: Long) {
        mBinder?.seekTo(currentTime)
    }

    override fun getCurrentPlayerMusic(): BaseAudioInfo? {
        return mBinder?.getCurrentPlayerMusic()
    }

    override fun getCurrentPlayList(): List<*>? {
        return mBinder?.getCurrentPlayList()
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
        return mBinder?.getCurDurtion() ?: 0
    }

    override fun setAlarm(times: Int) {
        mBinder?.setAlarm(times)
    }

    override fun updateMusicPlayerData(audios: List<BaseAudioInfo>, index: Int) {
        mBinder?.updateMusicPlayerData(audios, index)

    }

    override fun getPlayerState(): Int = mBinder?.getPlayerState() ?: -1
    override fun setLockActivityName(activityClassName: String): MusicPlayerManager {
        mActivityLockClassName = activityClassName;
        mBinder?.setLockActivityName(mActivityLockClassName ?: "")
        return this
    }

    override fun setDefaultAlarmModel(defaultAlarmModel: Int) {
        mBinder?.setDefaultAlarmModel(defaultAlarmModel)
    }

    override fun setDefaultPlayModel(defaultPlayModel: Int) {
        mBinder?.setDefaultPlayModel(defaultPlayModel)
    }

    override fun startServiceForeground() {
        TODO("Not yet implemented")
    }

    override fun startServiceForeground(notification: Notification?) {
        TODO("Not yet implemented")
    }

    override fun startServiceForeground(notification: Notification?, notifiid: Int) {
        TODO("Not yet implemented")
    }

    override fun stopServiceForeground() {
        TODO("Not yet implemented")
    }

    override fun startNotification() {
        TODO("Not yet implemented")
    }

    override fun startNotification(notification: Notification?) {
        TODO("Not yet implemented")
    }

    override fun startNotification(notification: Notification?, notifiid: Int) {
        TODO("Not yet implemented")
    }

    override fun updateNotification() {
        TODO("Not yet implemented")
    }

    override fun cleanNotification() {
        TODO("Not yet implemented")
    }


}