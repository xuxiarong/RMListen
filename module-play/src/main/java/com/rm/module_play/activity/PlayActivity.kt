package com.rm.module_play.activity

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.module_play.view.PlayButtonView
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import kotlinx.android.synthetic.main.music_paly_control_view.*
import kotlinx.android.synthetic.main.music_play_process_time.*
import kotlinx.coroutines.*


class PlayActivity :
    BaseVMActivity<com.rm.module_play.databinding.ActivityPlayBinding, PlayViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_play

    override fun initView() {
        dataBind.viewModel = mViewModel
        val textView = TextView(this)
        textView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setBackgroundResource(R.drawable.bubble_bg)
        textView.text = ""
        textView.setTextColor(-0x1)
        textView.gravity = Gravity.CENTER
        music_play_bubbleSeekBar.addBubbleFL(textView)
        music_play_bubbleSeekBar.setOnProgressChangedListener(object :
            BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                val str = "${progress.toInt()}/${bubbleSeekBar?.getMax()?.toInt()}"
                music_play_bubbleSeekBar.updateThumbText(str)
                textView.text = str
            }

            override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

            override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

        })
        music_play_bubbleSeekBar.setProgress(60f)
        music_play_button.setOnClickListener {
            toggleState()
        }

    }

    override fun startObserve() {
    }

    private fun toggleState() {
        when (music_play_button.buttonState) {
            PlayButtonView.STATE_PLAY -> music_play_button.setButtonState(
                PlayButtonView.STATE_PAUSE, false
            )
            PlayButtonView.STATE_PAUSE -> music_play_button.setButtonState(
                PlayButtonView.STATE_PLAY, false
            )
        }
    }

    override fun initData() {

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                MusicPlayerManager.musicPlayerManger.initialize(this@PlayActivity,
                    MusicInitializeCallBack {})
                delay(300)
            }
            MusicPlayerManager.musicPlayerManger.addOnPlayerEventListener(PlayerListener())
        }
    }

    class PlayerListener : MusicPlayerEventListener {
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
            TODO("Not yet implemented")
        }

        override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
            TODO("Not yet implemented")
        }
    }

}