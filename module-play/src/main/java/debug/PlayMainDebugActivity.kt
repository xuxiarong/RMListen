package debug

import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_play.R
import kotlinx.android.synthetic.main.play_activity_main.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class PlayMainDebugActivity : BaseDebugActivity(), MusicPlayerEventListener {
    override fun getLayoutResId(): Int = R.layout.play_activity_main

    override fun initView() {
    }

    override fun initData() {



    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onPrepared(totalDurtion: Long, isAd: Boolean) {
        seek_bar.max = totalDurtion.toInt()
    }

    override fun onBufferingUpdate(percent: Int) {
        TODO("Not yet implemented")
    }

    override fun onInfo(event: Int, extra: Int) {
        TODO("Not yet implemented")
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int, isAd: Boolean) {
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {

        ExoplayerLogger.exoLog(
            "currentDurtion=${currentDurtion.toInt()} duration=${totalDurtion.toInt()}"
        )
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

    }

    override fun onStartPlayAd() {
        TODO("Not yet implemented")
    }

    override fun onStopPlayAd() {
        TODO("Not yet implemented")
    }
}