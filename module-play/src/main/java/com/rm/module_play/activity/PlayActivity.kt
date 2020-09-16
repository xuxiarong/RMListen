package com.rm.module_play.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.baselisten.util.Cxt
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.coroutinepermissions.InlineRequestPermissionException
import com.rm.business_lib.coroutinepermissions.requestPermissionsForResult
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.component_comm.navigateToForResult
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.common.ARouterPath.Companion.testPath
import com.rm.module_play.databinding.ActivityPlayBinding
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.test.SearchResultInfo
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
    private val permsSd = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PlayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            })
        }
    }

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

    @InternalCoroutinesApi
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
            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() - 1000 * 15)
        }
        tv_music_right_next.setOnClickListener {
            //向前进15s
            musicPlayerManger.seekTo(musicPlayerManger.getCurDurtion() + 1000 * 15)
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
        bt_get_book_list.setOnClickListener {
            navigateToForResult(testPath, 100)
        }
        //上一首
        iv_music_play_left.setOnClickListener {
            musicPlayerManger.playLastMusic()
        }
        //下一首
        iv_music_play_right.setOnClickListener {
            musicPlayerManger.playNextMusic()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                requestPermissionsForResult(*permsSd, rationale = "为了更好的提供服务，需要获取存储空间权限")

            } catch (e: InlineRequestPermissionException) {
                ToastUtil.show(this@PlayActivity,"获取权限失败")
            }
        }

    }

    private var searchResultInfo: List<SearchResultInfo>? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100) {
            searchResultInfo = data?.getParcelableArrayListExtra("books")
            searchResultInfo?.let { mViewModel.zipPlayPath(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        rootViewAddView(GlobalplayHelp.instance.globalView)
        GlobalplayHelp.instance.globalView.show()

    }

    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
    }

    override fun startObserve() {
        mViewModel.playPath.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (mViewModel.playPath.get()?.size == 20) {
                    mViewModel.playPath.get()?.let {
                        musicPlayerManger.updateMusicPlayerData(it, 2)
                        musicPlayerManger.addOnPlayerEventListener(this@PlayActivity)
                        GlobalplayHelp.instance.addOnPlayerEventListener()
                    }

                }
            }
        })
    }

    private fun toggleState() {
        when (music_play_button.buttonState) {
            PlayButtonView.STATE_PLAY -> music_play_button.setButtonState(
                PlayButtonView.STATE_PAUSE, true
            )
            PlayButtonView.STATE_PAUSE -> music_play_button.setButtonState(
                PlayButtonView.STATE_PLAY, true
            )
        }
    }


    override fun initData() {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                musicPlayerManger.initialize(
                    Cxt.context,
                    MusicInitializeCallBack {})
                delay(300)
            }
        }

    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        loadRoundCornersImage(dip(8).toFloat(),iv_img_layout, musicPlayerManger.getCurrentPlayerMusic()?.audioCover)

    }

    override fun onPrepared(totalDurtion: Long) {
        music_play_bubbleSeekBar.setMax(totalDurtion.toFloat())
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
//        iv_img_layout
        loadRoundCornersImage(dip(8).toFloat(),iv_img_layout,musicInfo.audioCover)
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        music_play_bubbleSeekBar.setNoListenerProgress(currentDurtion.toFloat())
        val str = "${formatTimeInMillisToString(currentDurtion)}/${formatTimeInMillisToString(totalDurtion)}"
        music_play_bubbleSeekBar.updateThumbText(str)
        bubbleFl.text = str
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

    }


}