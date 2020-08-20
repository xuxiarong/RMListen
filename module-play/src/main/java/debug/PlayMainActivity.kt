package debug

import android.content.Context
import com.example.music_exoplayer_lib.bean.BaseAudioInfo
import com.example.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.example.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.example.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.example.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.example.music_exoplayer_lib.utils.ExoplayerLogger
import com.rm.baselisten.activity.BaseActivity
import com.rm.module_play.R
import kotlinx.android.synthetic.main.play_activity_main.*
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.koin.core.scope.Scope

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class PlayMainActivity : BaseActivity(), MusicPlayerEventListener {
    override fun getLayoutResId(): Int = R.layout.play_activity_main

    override fun initView() {


        //播放
        start_play.setOnClickListener {

            musicPlayerManger.startPlayerMusic(0)
        }
        //暂停
        start_pause.setOnClickListener {
            musicPlayerManger.pause()
        }

    }

    override fun initData() {
        GlobalScope.launch {
            async {
                musicPlayerManger.initialize(this@PlayMainActivity,
                    MusicInitializeCallBack {})
                delay(300)
            }.await()
            musicPlayerManger.addOnPlayerEventListener(this@PlayMainActivity)
        }


    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onPrepared(totalDurtion: Long) {
        TODO("Not yet implemented")
    }

    override fun onBufferingUpdate(percent: Int) {
        TODO("Not yet implemented")
    }

    override fun onInfo(event: Int, extra: Int) {
        TODO("Not yet implemented")
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
        TODO("Not yet implemented")
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
            "position=${currentDurtion.toInt()} duration=${formatTimeInMillisToString(
                currentDurtion
            )}"
        )
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
        TODO("Not yet implemented")
    }

}