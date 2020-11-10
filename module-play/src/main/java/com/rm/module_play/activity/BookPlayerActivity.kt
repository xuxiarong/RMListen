package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.BaseAudioModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.play.PlayState
import com.rm.business_lib.wedgit.swipleback.SwipeBackLayout
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.R.anim.activity_top_open
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.dialog.*
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_GET_PLAYINFO_LIST
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_JOIN_LISTEN
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_MORE_COMMENT
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_MORE_FINSH
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_OPERATING
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_QUEUE
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.activity_book_player.*


@Suppress(
    "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
/**
 * 播放器主要界面
 */
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
    MusicPlayerEventListener, SwipeBackLayout.SwipeBackListener {
    companion object {
        const val fromGlobalCache = "fromGlobalCache"

        //记录上次打开的时间，防止多次快速点击打开多次，影响体验
        var lastOpenTime: Long = SystemClock.currentThreadTimeMillis()
        var playAudioId: String = ""
        var playAudioModel: BaseAudioModel = BaseAudioModel()
        var playChapterId: String = ""
        var playChapterList: List<ChapterList> = mutableListOf()
        var playSortType: String = "scs"
        //是否重置播放
        var isResumePlay = false
        fun startPlayActivity(
            context: Context,
            audioId: String = "",
            audioModel: BaseAudioModel = BaseAudioModel(),
            chapterId: String = "",
            chapterList: List<ChapterList> = mutableListOf(),
            sortType: String = "scs"
        ) {
            try {
                //防止连续打开多次
                if (SystemClock.currentThreadTimeMillis() - lastOpenTime < 100) {
                    lastOpenTime = SystemClock.currentThreadTimeMillis()
                    return
                }
                lastOpenTime = SystemClock.currentThreadTimeMillis()
                playAudioId = audioId
                playAudioModel = audioModel
                playChapterId = chapterId
                playChapterList = chapterList
                playSortType = sortType
                //音频ID不能为空
                if(playAudioId.isEmpty()){
                    val baseActivity = context as BaseActivity
                    baseActivity.tipView.showTipView(baseActivity,"书籍ID不能为空")
                    return
                }
                val intent = Intent(context, BookPlayerActivity::class.java)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0)
            }catch (e : Exception){
                ToastUtil.show(context,"${e.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.playManger.resumePlayState(true)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.activity_bottom_close)
    }

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setStatusBar(R.color.businessWhite)

        mViewModel.initPlayerAdapterModel()
        swipe_back_layout.setDragEdge(SwipeBackLayout.DragEdge.TOP)
        swipe_back_layout.setOnSwipeBackListener(this)

    }

    override fun startObserve() {
        DownloadMemoryCache.downloadingChapter.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (MusicPlayBookListDialog.isShowing && MusicPlayBookListDialog.musicDialog != null) {
                    MusicPlayBookListDialog.musicDialog!!.notifyDialog()
                }
            }
        })

        mViewModel.playPath.observe(this, Observer { playPath ->
            musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
            GlobalplayHelp.instance.addOnPlayerEventListener()
            isResumePlay = true
            if (playPath.size <= 1) {
                mViewModel.hasNextChapter.set(false)
                mViewModel.hasPreChapter.set(false)
            }
            if (musicPlayerManger.getCurrentPlayerMusic() != null) {
                val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()!!
                playChapterId = currentPlayerMusic.chapterId
                getRecentPlayBook(currentPlayerMusic)
            }

            if (!musicPlayerManger.isPlaying()) {
                musicPlayerManger.updateMusicPlayerData(
                    audios = playPath,
                    chapterId = playChapterId
                )
                musicPlayerManger.startPlayMusic(chapterId = playChapterId)
            }
        })

        mViewModel.playControlAction.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val controlTime = mViewModel.playControlAction.get()
                when {
                    controlTime?.contains(ACTION_PLAY_QUEUE) == true -> {
                        //调整播放列表
                        mViewModel.audioChapterModel.get()?.let { it ->
                            showPlayBookListDialog(
                                downloadAudio = mViewModel.getHomeDetailListModel(),
                                audioListModel = it,
                                back = {
                                    musicPlayerManger.startPlayMusic(it.toString())
                                },
                                mLoad = { type ->
                                    if (type == 0) {
                                        //                                    ToastUtil.show(this@BookPlayerActivity, "刷新")
                                    } else {
                                        //                                    ToastUtil.show(this@BookPlayerActivity, "加载更多")
                                    }
                                }, isPlay = mViewModel.playStatusBean.get()?.read == true
                            )
                        }
                    }
                    controlTime?.contains(ACTION_PLAY_OPERATING) == true -> {
                        showMusicPlayMoreDialog { it1 ->
                            if (it1 == 0) {
                                showMusicPlayTimeSettingDialog {
                                    if (it) {
                                        mViewModel.countdown()
                                        if (mViewModel.playManger.getRemainingSetInt() > 0) {
                                            mViewModel.countdownTime.set("${mViewModel.playManger.getRemainingSetInt()}集")
                                        }
                                    }
                                }
                            } else {
                                showMusicPlaySpeedDialog()
                            }
                        }
                    }
                    controlTime?.contains(ACTION_GET_PLAYINFO_LIST) == true -> {
                        navigateToForResult(ARouterPath.testPath, 100)

                    }
                    controlTime?.contains(ACTION_JOIN_LISTEN) == true -> {
                    RouterHelper.createRouter(ListenService::class.java)
                        .showMySheetListDialog(
                            mViewModel,
                            this@BookPlayerActivity,
                            mViewModel.audioId.get()!!
                        )
                    }
                    controlTime?.contains(ACTION_MORE_COMMENT) == true -> {
                        mViewModel.audioId.get()?.let {

                        }
                    }
                    controlTime?.contains(ACTION_MORE_FINSH) == true -> {
                        finish()
                    }
                    else -> {

                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        fromGlobalCache.putMMKV(mViewModel.playBookSate.get())
    }

    override fun initData() {
        mViewModel.audioId.set(playAudioId)
        //书籍信息未传入，获取书籍详情信息
        if(TextUtils.isEmpty(playAudioModel.anchor_id)){
            mViewModel.getDetailInfo(playAudioId)
        }
        mViewModel.countdown()
        mViewModel.getCommentList()
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        if (playerState == -1) {
            tipView.showTipView(this, this.getString(R.string.business_play_error))
            mViewModel.playManger.pause()
        }

    }

    override fun onPrepared(totalDurtion: Long) {
        DLog.d("suolong", "'${System.currentTimeMillis()}")
        mViewModel.maxProcess.set(totalDurtion.toFloat())
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
        getRecentPlayBook(musicInfo)
        val playList = mViewModel.playManger.getCurrentPlayList()
        if (playList != null && playList.isNotEmpty()) {
            val size = playList.size
            if (position == 0) {
                mViewModel.hasPreChapter.set(false)
            } else {
                mViewModel.hasPreChapter.set(true)
            }
            if (position == size - 1) {
                mViewModel.hasNextChapter.set(false)
            } else {
                mViewModel.hasNextChapter.set(true)
            }
        } else {
            mViewModel.hasNextChapter.set(false)
            mViewModel.hasNextChapter.set(false)
        }
    }

    /**
     * 设置当前播放页面
     */
    private fun getRecentPlayBook(
        musicInfo: BaseAudioInfo
    ) {
        mViewModel.updatePlayBook(
            mViewModel.audioChapterModel.get()?.list?.find { it.chapter_id == playChapterId }
        )
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {

    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {

        mViewModel.updateThumbText.set(
            "${formatTimeInMillisToString(currentDurtion)}/${formatTimeInMillisToString(
                totalDurtion
            )}"
        )
        mViewModel.playBookSate.get()?.process = currentDurtion.toFloat()
        mViewModel.process.set(currentDurtion.toFloat())
        mViewModel.audioChapterModel.get()?.let {
            mViewModel.updatePlayBookProcess(
                it.list?.find { it.chapter_id == playChapterId },
                currentDurtion
            )
        }

        if (isResumePlay) {
            mViewModel.maxProcess.set(totalDurtion.toFloat())
            isResumePlay = false
        }
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val currentStatus = mViewModel.playStatusBean.get()
        if (currentStatus != null) {
            if (currentStatus.read == playWhenReady && currentStatus.state == playbackState) {
                return
            } else {
                mViewModel.playStatusBean.set(
                    PlayState(
                        state = playbackState,
                        read = playWhenReady
                    )
                )
                BaseConstance.basePlayStatusModel.set(
                    BasePlayStatusModel(
                        playReady = playWhenReady,
                        playStatus = playbackState
                    )
                )
            }
        } else {
            mViewModel.playStatusBean.set(
                PlayState(
                    state = playbackState,
                    read = playWhenReady
                )
            )
            BaseConstance.basePlayStatusModel.set(
                BasePlayStatusModel(
                    playReady = playWhenReady,
                    playStatus = playbackState
                )
            )
        }
        DLog.d(
            "suolong",
            " playWhenReady = $playWhenReady --- status = ${playbackState} --- time = ${System.currentTimeMillis()}"
        )
    }

    //播放完成
    override fun onCompletionPlay() {
        if (mViewModel.playManger.getPlayerAlarmModel() < 10) {
            if (mViewModel.playManger.getRemainingSetInt() > 0) {
                mViewModel.countdownTime.set("${mViewModel.playManger.getRemainingSetInt()}集")
            } else {
                mViewModel.countdownTime.set("")
            }
        }
    }

    override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
        getBaseContainer().background.mutate().alpha = (255 * (1 - fractionScreen)).toInt()
    }


}