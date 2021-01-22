package com.rm.music_exoplayer_lib.manager

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.rm.baselisten.BaseApplication
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.iinterface.MusicPlayerPresenter
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.listener.MusicPlayerInfoListener
import com.rm.music_exoplayer_lib.service.MusicPlayerBinder
import com.rm.music_exoplayer_lib.service.MusicPlayerService
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger

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


    internal class MusicPlayerServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ExoplayerLogger.exoLog("2 链接播放服务成功")

            (service as? MusicPlayerBinder)?.let {
                mBinder = service
                mBinder?.resumePlayState(true)
                mBinder?.setNotificationEnable(true)
                ExoplayerLogger.exoLog("3 服务 MusicPlayerBinder 启动成功")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            ExoplayerLogger.exoLog("2 链接播放服务失败")
            mBinder?.close()
            mBinder = null
            musicPlayerManger.initialize(BaseApplication.CONTEXT)
        }

    }

    fun initialize(context: Context) {
        if (mBinder != null) {
            return
        }

        ExoplayerLogger.exoLog("1 开始链接播放服务")

        mConnection = MusicPlayerServiceConnection()
        mConnection?.let {
            val intent = Intent(context, MusicPlayerService::class.java)
            context.startService(intent)
            ExoplayerLogger.exoLog("1 开始bindService")
            context.bindService(
                intent, it,
                Context.BIND_NOT_FOREGROUND
            )
        }
    }

    override fun startPlayMusic(chapterId: String) {
        mBinder?.startPlayMusic(chapterId)
        if(mBinder == null){
            initialize(BaseApplication.CONTEXT)
        }
        ExoplayerLogger.exoLog("startPlayMusic mBinder == null ${mBinder == null}chapterId = $chapterId")
        ExoplayerLogger.exoLog("startPlayMusic isPlaying == null ${mBinder?.isPlaying()} ")
    }

    override fun updateMusicPlayerData(audios: List<BaseAudioInfo>, chapterId: String) {
        mBinder?.updateMusicPlayerData(audios, chapterId)
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
        return mBinder?.getCurrentPlayerMusic()?.chapterId?.toLong() ?: 0L
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
        mBinder?.cleanNotification()
    }

    override fun setPlayerModel(model: Int): Int {
        mBinder?.setPlayerModel(model)
        return model
    }

    override fun getPlayerModel(): Int = mBinder?.getPlayerModel() ?: 0

    override fun setNotificationEnable(enable: Boolean) {
        mBinder?.setNotificationEnable(enable)

    }

    override fun getServiceName(): String = mBinder?.getServiceName() ?: ""
    override fun getPlayerMultiple(): Float = mBinder?.getPlayerMultiple() ?: 1f
    override fun resumePlayState(state: Boolean) {
        mBinder?.resumePlayState(state)
    }

    override fun getCurrentPlayIndex(): Int = mBinder?.getCurrentPlayIndex() ?: -1
    override fun setAdPath(adPathList: ArrayList<BaseAudioInfo>) {
        mBinder?.setAdPath(adPathList)
    }


}