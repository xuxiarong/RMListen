package com.rm.module_play.playview

import android.text.TextUtils
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.baseApplication
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.toLongSafe
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getFloattMMKV
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.STATE_ENDED
import com.rm.music_exoplayer_lib.constants.STATE_READY
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger

/**
 *
 * @des:播放状态栏
 * @data: 8/28/20 10:48 AM
 * @Version: 1.0.0
 */
class GlobalPlayHelper private constructor() : MusicPlayerEventListener,
    BaseApplication.IOnAllActivityDestroy {
    companion object {
        val INSTANCE: GlobalPlayHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalPlayHelper()
        }
        var listener: MusicPlayerEventListener? = null

    }

    private var oldAudio: String = ""

    var playStatusListener: IPlayStatusListener? = null


    fun addOnPlayerEventListener() {
        if (listener == null) {
            listener = this
            musicPlayerManger.addOnPlayerEventListener(this)
            baseApplication.registerAllActivityDestroy(this)
        }
    }

    fun registerPlayStatusListener(playStatusListener: IPlayStatusListener) {
        this.playStatusListener = playStatusListener
    }

    fun unRegisterPlayStatusListener() {
        this.playStatusListener = null
    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        DLog.d("suolong", "播放出错 playerState = $playerState 出错信息 = ${message ?: "为空"}")
        playStatusListener?.onMusicPlayerState(playerState, message)
        BaseConstance.basePlayStatusModel.set(BasePlayStatusModel(false, STATE_READY))
    }

    override fun onPrepared(totalDurtion: Long) {
        if (PlayGlobalData.playAdIsPlaying.get()) {
            musicPlayerManger.setPlayerMultiple(1f)
            PlayGlobalData.playSpeed.set(1f)
        } else {
            SAVA_SPEED.getFloattMMKV(1f).let {
                PlayGlobalData.playSpeed.set(it)
                musicPlayerManger.setPlayerMultiple(it)
            }
            PlayGlobalData.maxProcess.set(totalDurtion.toFloat())
            musicPlayerManger.getCurrentPlayerMusic()?.let {
                it.duration = totalDurtion
            }
            PlayGlobalData.playChapter.get()?.let {
                it.realDuration = totalDurtion
            }
            if (PlayGlobalData.playNeedQueryChapterProgress.get()) {
                PlayGlobalData.playNeedQueryChapterProgress.set(false)
                PlayGlobalData.playAudioId.get()?.let { audioId ->
                    PlayGlobalData.playChapterId.get()?.let { chapterId ->
                        val listenChapter = ListenDaoUtils.queryChapterRecentUpdate(
                            audioId.toLongSafe(),
                            chapterId.toLongSafe()
                        )
                        listenChapter?.let {
                            if (it.listen_duration != 0L) {
                                musicPlayerManger.seekTo(it.listen_duration)
                            }
                        }
                    }
                }
            }
        }
//        musicPlayerManger.play()

    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {

        BusinessInsertManager.doInsertKeyAndChapter(
            BusinessInsertConstance.INSERT_TYPE_CHAPTER_PLAY,
            musicInfo.audioId,
            musicInfo.chapterId
        )

        if (!TextUtils.equals(oldAudio, musicInfo.audioId)) {
            oldAudio = musicInfo.audioId
            BusinessInsertManager.doInsertKeyAndAudio(
                BusinessInsertConstance.INSERT_TYPE_AUDIO_PLAY,
                musicInfo.audioId
            )
        }

        BaseConstance.updateBaseChapterId(musicInfo.chapterId)
        BaseConstance.updateBaseProgress(0L, musicInfo.duration * 1000)
        PlayGlobalData.savePlayChapter(position)
        PlayGlobalData.setPlayHasNextAndPre(musicPlayerManger.getCurrentPlayList(), position)
        PlayGlobalData.updateCountChapterSize()
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        BaseConstance.updateBaseProgress(currentDurtion, totalDurtion)
        PlayGlobalData.updatePlayChapterProgress(currentDurtion, totalDurtion)
        PlayGlobalData.updateCountSecond()

        DLog.d(
            "suolong",
            " totalDurtion = $totalDurtion --- status = $currentDurtion --- time = $currentDurtion"
        )
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == STATE_ENDED) {
            BaseConstance.updatePlayFinish()
            PlayGlobalData.updatePlayChapterProgress(isPlayFinish = true)
            PlayGlobalData.checkCountChapterPlayEnd(playWhenReady)
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
        DLog.d("suolong", " playWhenReady = $playWhenReady --- status = $playbackState ")
    }

    override fun onStartPlayAd() {
        PlayGlobalData.process.set(0F)
        PlayGlobalData.maxProcess.set(0F)
        if ("00:00/00:00" != PlayGlobalData.updateThumbText.get()) {
            PlayGlobalData.updateThumbText.set("00:00/00:00")
        }
        PlayGlobalData.playAdIsPlaying.set(true)
    }

    override fun onStopPlayAd() {
        PlayGlobalData.playAdIsPlaying.set(false)
        PlayGlobalData.playVoiceAdClose.set(true)
        PlayGlobalData.playVoiceImgAd.set(null)
    }

    interface IPlayStatusListener {
        fun onMusicPlayerState(playerState: Int, message: String?)
    }

    override fun onAllActivityDestroy() {
        if (musicPlayerManger.isPlaying()) {
            musicPlayerManger.pause()
        }
    }

}