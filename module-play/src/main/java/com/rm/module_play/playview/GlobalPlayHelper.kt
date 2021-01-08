package com.rm.module_play.playview

import android.text.TextUtils
import com.mei.orc.util.json.toJson
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.baseApplication
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.toLongSafe
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.NetWorkUtils
import com.rm.baselisten.util.getFloattMMKV
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.bean.BusinessAdRequestModel
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.net.api.BusinessApiService
import com.rm.module_play.activity.BookPlayerActivity
import com.rm.module_play.api.PlayApiService
import com.rm.module_play.repositoryModule
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_ORDER
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_SINGLE
import com.rm.music_exoplayer_lib.constants.STATE_ENDED
import com.rm.music_exoplayer_lib.constants.STATE_READY
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.dsl.viewModel
import kotlin.random.Random

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

        private val playApiService by lazy {
            BusinessRetrofitClient().getService(
                PlayApiService::class.java
            )
        }
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
            PlayGlobalData.playLastPlayProcess.set(BaseConstance.basePlayProgressModel.get()?.currentDuration?:-1L)
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

    override fun onPrepared(totalDurtion: Long,isAd : Boolean) {
        DLog.d("suolong_GlobalPlayHelper","onPrepared isAd = $isAd")
        PlayGlobalData.playIsError.set(false)
        //播放广告的时候，播放速度不生效，正常播放生效
        if (isAd) {
            musicPlayerManger.setPlayerMultiple(1f)
            BaseConstance.updateBaseProgress(0,totalDurtion)
            PlayGlobalData.process.set(0F)
            PlayGlobalData.updateThumbText.set("00:00/00:00")
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
            //如果需要接着上一次的播放记录，直接跳转到该进度
            if(PlayGlobalData.playLastPlayProcess.get()>0 && !isAd){
                if(PlayGlobalData.playLastPlayProcess.get()>=totalDurtion){
                    musicPlayerManger.seekTo(0L)
                }else{
                    musicPlayerManger.seekTo(PlayGlobalData.playLastPlayProcess.get())
                }
                PlayGlobalData.playLastPlayProcess.set(-1L)
            }
        }
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int,isAd: Boolean) {
        PlayGlobalData.playIsError.set(false)
        BaseConstance.updateBaseChapterId(musicInfo.audioId,musicInfo.chapterId)
        PlayGlobalData.setPlayHasNextAndPre(musicPlayerManger.getCurrentPlayList(), position)
        if(PlayGlobalData.playAdIsPlaying.get()){
            BusinessInsertManager.doInsertKeyAndChapter(
                BusinessInsertConstance.INSERT_TYPE_CHAPTER_PLAY,
                musicInfo.audioId,
                musicInfo.chapterId
            )
            DLog.d("suolong_GlobalPlayHelper","onPlayMusiconInfo")
            if (!TextUtils.equals(oldAudio, musicInfo.audioId)) {
                oldAudio = musicInfo.audioId
                BusinessInsertManager.doInsertKeyAndAudio(
                    BusinessInsertConstance.INSERT_TYPE_AUDIO_PLAY,
                    musicInfo.audioId
                )
            }
        }else{
            BaseConstance.updateBaseProgress(0L, musicInfo.duration * 1000)
        }
        PlayGlobalData.savePlayChapter(position,isAd)

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
        if (playbackState == STATE_ENDED && playWhenReady) {
            BaseConstance.updatePlayFinish()
            PlayGlobalData.updatePlayChapterProgress(isPlayFinish = true)
            PlayGlobalData.playNeedQueryChapterProgress.set(false)

            when (musicPlayerManger.getPlayerModel()) {
                //顺序播放
                MUSIC_MODEL_ORDER -> {
                    if(PlayGlobalData.checkCountChapterPlayEnd(PlayGlobalData.hasNextChapter.get())){
                        musicPlayerManger.pause()
                    }else{
                        if (PlayGlobalData.hasNextChapter.get()) {
                            PlayGlobalData.playNeedQueryChapterProgress.set(false)
                            getChapterAd {
                                musicPlayerManger.playNextMusic()
                            }
                        }
                    }
                }
                //单曲播放
                MUSIC_MODEL_SINGLE -> {
                    if(PlayGlobalData.checkCountChapterPlayEnd(true)){
                        musicPlayerManger.pause()
                    }else{
                        PlayGlobalData.playChapterId.get()?.let {
                            PlayGlobalData.playNeedQueryChapterProgress.set(false)
                            getChapterAd {
                                musicPlayerManger.startPlayMusic(it)
                            }
                        }
                    }
                }
            }
        }
        val currentStatus = BaseConstance.basePlayStatusModel.get()
        if (currentStatus != null) {
            if (currentStatus.playReady == playWhenReady && currentStatus.playStatus == playbackState) {
                return
            } else {
                BaseConstance.basePlayStatusModel.set(BasePlayStatusModel(playWhenReady, playbackState))
            }
        } else {
            BaseConstance.basePlayStatusModel.set(BasePlayStatusModel(playWhenReady, playbackState))
        }
        DLog.d("suolong", " playWhenReady = $playWhenReady --- status = $playbackState ")
    }

    fun getChapterAd(actionPlayAd: () -> Unit) {
        if(!NetWorkUtils.isNetworkAvailable(BaseApplication.CONTEXT)){
            actionPlayAd()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val requestBean =
                BusinessAdRequestModel(arrayOf("ad_player_voice", "ad_player_audio_cover"))
            val chapterAd = playApiService.getChapterAd(
                requestBean.toJson().toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaType())
            )
            if(chapterAd.code!=0){
                PlayGlobalData.playAdIsPlaying.set(false)
                PlayGlobalData.playVoiceAdClose.set(true)
                PlayGlobalData.playVoiceImgAd.set(null)
                musicPlayerManger.setAdPath(arrayListOf())
                actionPlayAd()
                DLog.d("suolong", "error = ${chapterAd.msg}")
            }else{
                chapterAd.data.let {
                    val result = arrayListOf<BaseAudioInfo>()
                    if (it.ad_player_voice != null && it.ad_player_voice.isNotEmpty()) {
                        val position = Random.nextInt(it.ad_player_voice.size)
                        result.add(
                            BaseAudioInfo(
                                audioPath = it.ad_player_voice[position].audio_url,
                                isAd = true
                            )
                        )
                        PlayGlobalData.playAdIsPlaying.set(true)
                        PlayGlobalData.playVoiceAdClose.set(false)
                        PlayGlobalData.playVoiceImgAd.set(it.ad_player_voice[position])
                    } else {
                        PlayGlobalData.playAdIsPlaying.set(false)
                        PlayGlobalData.playVoiceAdClose.set(true)
                        PlayGlobalData.playVoiceImgAd.set(null)
                    }
                    musicPlayerManger.setAdPath(result)
                    actionPlayAd()
                    it.ad_player_audio_cover?.let { audioImgAdList ->
                        if (audioImgAdList.isNotEmpty()) {
                            PlayGlobalData.playAudioImgAd.set(
                                audioImgAdList[Random.nextInt(
                                    audioImgAdList.size
                                )]
                            )
                        }
                    }
                }
            }
        }
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

    fun showNotification() {
//        getCurrentPlayerMusic()?.let {
//            notificationManger?.showNotification(this, it, "")
//        }
    }

    interface IPlayStatusListener {
        fun onMusicPlayerState(playerState: Int, message: String?)
    }

    override fun onAllActivityDestroy() {
        if (musicPlayerManger.isPlaying()) {
            musicPlayerManger.pause()
            PlayGlobalData.clearCountDownTimer()
        }
    }

}