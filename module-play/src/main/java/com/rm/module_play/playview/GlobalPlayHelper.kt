package com.rm.module_play.playview

import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.DLog
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
    }


    fun addOnPlayerEventListener() {
        musicPlayerManger.addOnPlayerEventListener(this)
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        ExoplayerLogger.exoLog("playerState=${playerState},message=${message}")
    }

    override fun onPrepared(totalDurtion: Long) {
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
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == STATE_ENDED) {
            BaseConstance.updatePlayFinish()
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

}