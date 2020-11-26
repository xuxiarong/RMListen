package com.rm.module_play.playview

import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.DLog
import com.rm.business_lib.PlayGlobalData
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.STATE_ENDED
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger

/**
 *
 * @des:播放状态栏
 * @data: 8/28/20 10:48 AM
 * @Version: 1.0.0
 */
class GlobalPlayHelper private constructor() : MusicPlayerEventListener {
    companion object {
        val INSTANCE: GlobalPlayHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalPlayHelper()
        }
        var listener: MusicPlayerEventListener? = null
    }

    var playStatusListener : IPlayStatusListener? = null


    fun addOnPlayerEventListener() {
        if(listener == null){
            listener = this
            musicPlayerManger.addOnPlayerEventListener(this)
        }
    }

    fun registerPlayStatusListener(playStatusListener : IPlayStatusListener){
        this.playStatusListener = playStatusListener
    }

    fun unRegisterPlayStatusListener(){
        this.playStatusListener = null
    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        ExoplayerLogger.exoLog("playerState=${playerState},message=${message}")
        playStatusListener?.let {
            it.onMusicPlayerState(playerState,message)
        }
    }

    override fun onPrepared(totalDurtion: Long) {
        PlayGlobalData.maxProcess.set(totalDurtion.toFloat())
        musicPlayerManger.getCurrentPlayerMusic()?.let {
            it.duration = totalDurtion
        }
        PlayGlobalData.playChapter.get()?.let {
            it.realDuration = totalDurtion
        }
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
        BaseConstance.updateBaseChapterId(chapterId = musicInfo.chapterId)
        BaseConstance.updateBaseProgress(
            currentDuration = 0L,
            totalDuration = musicInfo.duration * 1000
        )
        PlayGlobalData.savePlayChapter(position)

        val playList = musicPlayerManger.getCurrentPlayList()
        if (playList != null && playList.isNotEmpty()) {
            val size = playList.size
            if (position == 0) {
                PlayGlobalData.hasPreChapter.set(false)
            } else {
                PlayGlobalData.hasPreChapter.set(true)
            }
            if (position == size - 1) {
                PlayGlobalData.hasNextChapter.set(false)
            } else {
                PlayGlobalData.hasNextChapter.set(true)
            }
        } else {
            PlayGlobalData.hasNextChapter.set(false)
            PlayGlobalData.hasNextChapter.set(false)
        }

    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        BaseConstance.updateBaseProgress(
            currentDuration = currentDurtion,
            totalDuration = totalDurtion
        )
        PlayGlobalData.updatePlayChapterProgress(currentDurtion, totalDurtion)
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == STATE_ENDED) {
            BaseConstance.updatePlayFinish()
            PlayGlobalData.updatePlayChapterProgress(isPlayFinish = true)
        }
        val currentStatus = BaseConstance.basePlayStatusModel.get()
        if (currentStatus != null) {
            if (currentStatus.playReady == playWhenReady && currentStatus.playStatus == playbackState) {
                return
            } else {
                BaseConstance.basePlayStatusModel.set(
                    BasePlayStatusModel(
                        playWhenReady,
                        playbackState
                    )
                )
            }
        } else {
            BaseConstance.basePlayStatusModel.set(BasePlayStatusModel(playWhenReady, playbackState))
        }
        DLog.d(
            "suolong",
            " playWhenReady = $playWhenReady --- status = $playbackState --- time = ${System.currentTimeMillis()}"
        )
    }

    override fun onCompletionPlay() {
    }

    interface IPlayStatusListener{
        fun onMusicPlayerState(playerState: Int, message: String?)
    }

}