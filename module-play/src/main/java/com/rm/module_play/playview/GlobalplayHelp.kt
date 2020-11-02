package com.rm.module_play.playview

import com.rm.baselisten.util.Cxt
import com.rm.baselisten.utilExt.dip
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger

/**
 *
 * @des:播放状态栏
 * @data: 8/28/20 10:48 AM
 * @Version: 1.0.0
 */
class GlobalplayHelp private constructor() : MusicPlayerEventListener {
    companion object {
        val instance: GlobalplayHelp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalplayHelp()
        }
    }

    val globalView by lazy {
        GlobalPlay(Cxt.context).apply {
            setRadius(Cxt.context.dip(19).toFloat())
            setBarWidth(Cxt.context.dip(2).toFloat())
        }
    }

    fun addOnPlayerEventListener() {
        musicPlayerManger.addOnPlayerEventListener(this)
    }
    //设置书籍封面
    fun setBooKImage(bookUrl: String?){
        bookUrl?.let { globalView.setBooKImage(it) }
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        globalView.showPlayError()
        ExoplayerLogger.exoLog("playerState=${playerState},message=${message}")
    }

    override fun onPrepared(totalDurtion: Long) {
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        globalView.setProgress(currentDurtion.toFloat() / totalDurtion.toFloat())
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (!playWhenReady) {
            globalView.pause()
        } else {
            globalView.play()
        }
    }

    override fun onCompletionPlay() {
    }


}