package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
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
import com.rm.module_play.dialog.MusicPlayBookListDialog
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.playview.GlobalPlayHelper
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_GET_PLAYINFO_LIST
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_JOIN_LISTEN
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_MORE_COMMENT
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_MORE_FINSH
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_OPERATING
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_QUEUE
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
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

        //记录上次打开的时间，防止多次快速点击打开多次，影响体验
        private var lastOpenTime: Long = 0L
        var playAudioId: String = ""
        var playAudioModel: DownloadAudio = DownloadAudio()
        var playChapterId: String = ""
        var playChapterList: MutableList<DownloadChapter> = mutableListOf()
        var playCurrentDuration: Long = 0L
        var playSortType: String = AudioSortType.SORT_ASC

        fun startPlayActivity(
            context: Context,
            audioId: String = "",
            audioModel: DownloadAudio = DownloadAudio(),
            chapterId: String = "",
            chapterList: MutableList<DownloadChapter> = mutableListOf(),
            currentDuration: Long = 0L,
            sortType: String = AudioSortType.SORT_ASC
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
                playCurrentDuration = currentDuration
                playSortType = sortType

                //音频ID不能为空
                if (playAudioId.isEmpty()) {
                    val baseActivity = context as BaseActivity
                    baseActivity.tipView.showTipView(baseActivity, "书籍ID不能为空")
                    return
                }
                val intent = Intent(context, BookPlayerActivity::class.java)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0)
            } catch (e: Exception) {
                ToastUtil.show(context, "${e.message}")
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

    private val footView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setStatusBar(R.color.businessWhite)
        swipe_back_layout.setDragEdge(SwipeBackLayout.DragEdge.TOP)
        swipe_back_layout.setOnSwipeBackListener(this)
    }

    override fun startObserve() {
        mViewModel.commentRefreshModel.isHasMore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.commentRefreshModel.isHasMore.get()
                if (hasMore==true){
                    mViewModel.mCommentAdapter.removeAllFooterView()
                    mViewModel.mCommentAdapter.addFooterView(footView)
                }else{
                    mViewModel.mCommentAdapter.removeAllFooterView()
                }
            }
        })


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
            GlobalPlayHelper.INSTANCE.addOnPlayerEventListener()
            if (playPath.size <= 1) {
                mViewModel.hasNextChapter.set(false)
                mViewModel.hasPreChapter.set(false)
            }
            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
            val chapterId = mViewModel.playChapterId.get()
            if (chapterId != null && !TextUtils.isEmpty(chapterId)) {
                if (currentPlayerMusic != null) {
                    //传入的章节id与正在播放的章节id进行对比，如果不一致，则播放传入的章节，一致则不用处理，继续播放该章节即可
                    if (currentPlayerMusic.chapterId != chapterId) {
                        startPlayChapter(playPath, chapterId, currentPlayerMusic)
                    } else {
                        onPlayMusiconInfo(
                            currentPlayerMusic,
                            musicPlayerManger.getCurrentPlayIndex()
                        )
                    }
                } else {
                    if (playPath != null && playPath.isNotEmpty()) {
                        val predicate: (BaseAudioInfo) -> Boolean = { chapterId == it.chapterId }
                        val firstIndex = playPath.indexOfFirst(predicate)
                        if (firstIndex != -1) {
                            startPlayChapter(playPath, chapterId, playPath[firstIndex])
                        } else {
                            startPlayChapter(playPath, playPath[0].chapterId, playPath[0])
                        }
                    }
                }
            } else {
                if (playPath != null && playPath.isNotEmpty()) {
                    startPlayChapter(playPath, playPath[0].chapterId, playPath[0])
                }
            }
        })

        mViewModel.playControlAction.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val controlTime = mViewModel.playControlAction.get()
                when {
                    controlTime?.contains(ACTION_PLAY_QUEUE) == true -> {

//                        showPlayBookListDialog(
//                            downloadAudio = mViewModel.playAudioModel.get(),
//                            audioListModel = it,
//                            back = {
//                                musicPlayerManger.startPlayMusic(it.toString())
//                            },
//                            mLoad = mViewModel.playChapterListSort,
//                            isPlay = mViewModel.playStatusBean.get()?.read == true
//                        )
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
                                mViewModel.playAudioId.get()!!
                            )
                    }
                    controlTime?.contains(ACTION_MORE_COMMENT) == true -> {
                        mViewModel.playAudioId.get()?.let {

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

    private fun startPlayChapter(
        playPath: List<BaseAudioInfo>,
        chapterId: String,
        currentPlayerMusic: BaseAudioInfo
    ) {
        musicPlayerManger.updateMusicPlayerData(audios = playPath, chapterId = chapterId)
        musicPlayerManger.startPlayMusic(chapterId = chapterId)
        mViewModel.maxProcess.set(currentPlayerMusic.duration.toFloat())
        when {
            playCurrentDuration <= 0 -> {
                mViewModel.process.set(0F)
            }
            else -> {
                if (playCurrentDuration > currentPlayerMusic.duration * 1000) {
                    playCurrentDuration = currentPlayerMusic.duration * 1000
                }
                musicPlayerManger.seekTo(playCurrentDuration)
            }
        }
    }

    override fun initData() {
        mViewModel.playAudioId.set(playAudioId)
        mViewModel.playChapterListSort.set(playSortType)
        //书籍信息未传入，获取书籍详情信息,有则直接使用
        if (TextUtils.isEmpty(playAudioModel.audio_cover_url)) {
            mViewModel.getDetailInfo(playAudioId)
        } else {
            mViewModel.initPlayAudio(playAudioModel)
        }
        //如果传入的章节id为空，说明不是通过章节列表跳转的，直接访问书籍章节列表的第一页数据即可

        if (playChapterList.isNotEmpty()) {
            mViewModel.playChapterList.value = playChapterList
        } else {
            if (TextUtils.isEmpty(playChapterId)) {
                mViewModel.getChapterList(playAudioId)
            } else {
                val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
                if (currentPlayerMusic != null && currentPlayerMusic.chapterId == playChapterId) {
                    mViewModel.maxProcess.set(currentPlayerMusic.duration * 1000F)
                    mViewModel.process.set(musicPlayerManger.getCurDurtion().toFloat())
                }
                mViewModel.playChapterId.set(playChapterId)
                mViewModel.getChapterListWithId(audioId = playAudioId, chapterId = playChapterId)
            }
        }
        playChapterId = ""
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
        mViewModel.savePlayChapter(musicInfo, position)

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

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {

    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        mViewModel.updatePlayChapterProgress(currentDurtion, totalDurtion)
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == PlayGlobalData.STATE_ENDED) {
            mViewModel.updatePlayChapterProgress(isPlayFinish = true)
        }
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
            " playWhenReady = $playWhenReady --- status = $playbackState --- time = ${System.currentTimeMillis()}"
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