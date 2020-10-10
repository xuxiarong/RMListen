package debug

import android.widget.SeekBar
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.baselisten.util.ToastUtil
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


        //播放
        start_play.setOnClickListener {

//            musicPlayerManger.startPlayMusic(0)
        }
        //暂停
        start_pause.setOnClickListener {
            musicPlayerManger.pause()
        }
        //一倍速播放
        tv_one.setOnClickListener {
            musicPlayerManger.setPlayerMultiple(1f)

        }
        //1.5倍速播放
        tv_one_b.setOnClickListener {
            musicPlayerManger.setPlayerMultiple(1.5f)
        }
        //3倍速播放
        tv_three.setOnClickListener {
            musicPlayerManger.setPlayerMultiple(3f)
        }
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                musicPlayerManger.seekTo(seekBar?.progress?.toLong() ?: 0)
            }
        })
        //快进
        tv_forward.setOnClickListener {
            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() + 1000 * 30)
        }
        //后退
        tv_back.setOnClickListener {
            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() - 1000 * 30)
        }
        //定时关闭播放器
        tv_commit.setOnClickListener {
            if (et_times.text.isNotEmpty()) {
                musicPlayerManger.setAlarm(et_times.text.toString().toInt()*1000)
            } else {
                ToastUtil.show(this, "请输入时间")

            }
        }
    }

    override fun initData() {



    }


    override fun onMusicPlayerState(playerState: Int, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onPrepared(totalDurtion: Long) {
        seek_bar.max = totalDurtion.toInt()
        tv_end.text = formatTimeInMillisToString(totalDurtion)
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
        seek_bar.progress = currentDurtion.toInt()
        tv_process.text = formatTimeInMillisToString(currentDurtion)
        ExoplayerLogger.exoLog(
            "currentDurtion=${currentDurtion.toInt()} duration=${totalDurtion.toInt()}"
        )
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

    }

}