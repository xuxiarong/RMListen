package com.rm.module_play.playview

import android.text.TextUtils
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.baseApplication
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.toLongSafe
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getFloattMMKV
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.module_play.activity.BookPlayerActivity
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
    private var lastPlayProcess = -1L

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

    fun continueLastPlay(playChapter: DownloadChapter,playChapterList : MutableList<DownloadChapter>){
        if(playChapterList.isNotEmpty()){
            addOnPlayerEventListener()
            val baseAudioList = mutableListOf<BaseAudioInfo>()
            playChapterList.forEach {
                baseAudioList.add(
                        BaseAudioInfo(
                                audioPath = it.path_url,
                                audioName = it.chapter_name,
                                filename = it.chapter_name,
                                audioId = it.audio_id.toString(),
                                chapterId = it.chapter_id.toString(),
                                duration = it.realDuration,
                                playCount = it.play_count.toString()
                        )
                )
            }
            DLog.d("music-exoplayer-lib","continueLastPlay")
            lastPlayProcess = BaseConstance.basePlayProgressModel.get()?.currentDuration?:-1L
            musicPlayerManger.updateMusicPlayerData(audios = baseAudioList, chapterId = playChapter.chapter_id.toString())
            musicPlayerManger.startPlayMusic(chapterId = playChapter.chapter_id.toString())
        }
    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        DLog.d("suolong", "播放出错 playerState = $playerState 出错信息 = ${message ?: "为空"}")
        playStatusListener?.onMusicPlayerState(playerState, message)
        BaseConstance.basePlayStatusModel.set(BasePlayStatusModel(false, STATE_READY))
        PlayGlobalData.playIsError.set(true)
    }

    override fun onPrepared(totalDurtion: Long) {
        PlayGlobalData.playIsError.set(false)
        if(lastPlayProcess>0){
            musicPlayerManger.seekTo(lastPlayProcess)
            lastPlayProcess = -1L
            return
        }
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
        PlayGlobalData.playIsError.set(false)
        BaseConstance.updateBaseChapterId(musicInfo.chapterId)
        PlayGlobalData.savePlayChapter(position)
        PlayGlobalData.setPlayHasNextAndPre(musicPlayerManger.getCurrentPlayList(), position)
        if(lastPlayProcess>0){
            return
        }
        BusinessInsertManager.doInsertKeyAndChapter(
                BusinessInsertConstance.INSERT_TYPE_CHAPTER_PLAY,
                musicInfo.audioId,
                musicInfo.chapterId
        )

        DLog.i("======>>", "oldAudio:$oldAudio    audioId: ${musicInfo.audioId}")
        if (!TextUtils.equals(oldAudio, musicInfo.audioId)) {
            oldAudio = musicInfo.audioId
            BusinessInsertManager.doInsertKeyAndAudio(
                    BusinessInsertConstance.INSERT_TYPE_AUDIO_PLAY,
                    musicInfo.audioId
            )
        }
        BaseConstance.updateBaseProgress(0L, musicInfo.duration * 1000)
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