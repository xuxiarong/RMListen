package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.databinding.ActivityPlayBinding
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.view.PlayButtonView
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.music_paly_control_view.*
import kotlinx.android.synthetic.main.music_play_process_time.*
import kotlinx.coroutines.*


class PlayActivity :
    BaseVMActivity<ActivityPlayBinding, PlayViewModel>(),
    MusicPlayerEventListener {

    override fun getLayoutId(): Int = R.layout.activity_play

    override fun initModelBrId() = BR.viewModel


    val bubbleFl by lazy {
        TextView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundResource(R.drawable.bubble_bg)
            setTextColor(-0x1)
            gravity = Gravity.CENTER

        }
    }

    @SuppressLint("ResourceType")
    override fun initView() {
        setStatusBar(R.color.businessWhite)
        music_play_bubbleSeekBar.addBubbleFL(bubbleFl)
        music_play_bubbleSeekBar.setOnProgressChangedListener(object :
            BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                val str =
                    "${formatTimeInMillisToString(progress.toLong())}/${formatTimeInMillisToString(
                        bubbleSeekBar?.getMax()?.toLong() ?: 0
                    )}"
                music_play_bubbleSeekBar.updateThumbText(str)
                bubbleFl.text = str
                musicPlayerManger.seekTo(progress.toLong())
            }

            override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

            override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

        })
        music_play_button.setOnClickListener {
            toggleState()
            musicPlayerManger.playOrPause()
        }
        tv_music_left_next.setOnClickListener {
            //向后退15s
//            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() - 1000 * 15)
            startActivity(Intent(this,TestActivity::class.java))
//            GlobalplayHelp.instance.globalView.show()
        }
        tv_music_right_next.setOnClickListener {
            //向前进15s
            GlobalplayHelp.instance.globalView.hide()
//            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() + 1000 * 15)
        }
        music_play_point.setOnClickListener {
            showMusicPlayMoreDialog { it1 ->
                if (it1 == 0) {
                    showMusicPlayTimeSettingDialog()
                } else {
                    showMusicPlaySpeedDialog {
                        musicPlayerManger.setPlayerMultiple(it)
                    }
                }
            }
        }
        image_music_play_book_list.setOnClickListener {
            //调整播放列表
            showPlayBookListDialog()
        }


    }

    override fun onResume() {
        super.onResume()
        GlobalplayHelp.instance.addGlobalPlayHelp(getRootView(),layoutParams)
        GlobalplayHelp.instance.globalView.play(R.drawable.business_defualt_img)
        android.os.Handler().postDelayed({
            GlobalplayHelp.instance.globalView.show()
        },1500)
    }

    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
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

    val RADIO_URL = "https://webfs.yun.kugou.com/202008271626/08eb2d767ed3c0009cd8c706a769a9bc/G111/M03/12/01/D4cBAFmhufWAdEyfADTbmVX-PgI993.mp3"

    override fun initData() {

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                musicPlayerManger.initialize(this@PlayActivity,
                    MusicInitializeCallBack {})
                delay(300)
            }
            musicPlayerManger.addOnPlayerEventListener(this@PlayActivity)
            val musicData = arrayListOf<BaseAudioInfo>()
            musicData.add(BaseAudioInfo(RADIO_URL))
            musicPlayerManger.updateMusicPlayerData(musicData, 0)
        }
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
    }

    override fun onPrepared(totalDurtion: Long) {
        music_play_bubbleSeekBar.setMax(totalDurtion.toFloat())
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
        music_play_bubbleSeekBar.setProgress(currentDurtion.toFloat())
        val str =
            "${formatTimeInMillisToString(currentDurtion)}/${formatTimeInMillisToString(totalDurtion)}"
        music_play_bubbleSeekBar.updateThumbText(str)
        bubbleFl.text = str
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
    }


}