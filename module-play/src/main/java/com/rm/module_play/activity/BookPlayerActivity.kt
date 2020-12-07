package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.wedgit.swipleback.SwipeBackLayout
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.R.anim.activity_top_open
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.databinding.PlayPlayHeadBinding
import com.rm.module_play.playview.GlobalPlayHelper
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger


@SuppressLint("InflateParams")
@Suppress(
    "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
/**
 * 播放器主要界面
 */
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
        GlobalPlayHelper.IPlayStatusListener, SwipeBackLayout.SwipeBackListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalPlayHelper.INSTANCE.registerPlayStatusListener(this@BookPlayerActivity)
        GlobalPlayHelper.INSTANCE.addOnPlayerEventListener()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.playManger.resumePlayState(true)
    }

    override fun finish() {
        super.finish()
        GlobalPlayHelper.INSTANCE.unRegisterPlayStatusListener()
        overridePendingTransition(0, R.anim.activity_bottom_close)
    }

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setTransparentStatusBar()
        mDataBind.swipeBackLayout.apply {
            setDragEdge(SwipeBackLayout.DragEdge.TOP)
            setOnSwipeBackListener(this@BookPlayerActivity)
            setScrollChild(mDataBind.playCommentRv)
        }
        setTitleTpoMargin()
        recycleScrollListener()

        mDataBind.playCommentRv.apply {
            bindVerticalLayout(mViewModel.mCommentAdapter)
            createHead(this)
        }
    }

    private fun createHead(rv: RecyclerView) {
        DataBindingUtil.inflate<PlayPlayHeadBinding>(
            LayoutInflater.from(rv.context),
            R.layout.play_play_head,
            rv,
            false
        ).apply {
            mViewModel.mCommentAdapter.addHeaderView(this.root)
            setVariable(BR.viewModel, mViewModel)
        }
    }

    /**
     * 设置顶部的margin
     */
    private fun setTitleTpoMargin() {
        (mDataBind.reportWhyArrow.layoutParams as ViewGroup.MarginLayoutParams).apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin =
                getStateHeight(this@BookPlayerActivity) + resources.getDimensionPixelOffset(R.dimen.dp_10)
        }
    }

    override fun startObserve() {

        mViewModel.playPath.observe(this, Observer { playPath ->
            if (playPath.size <= 1) {
                PlayGlobalData.hasNextChapter.set(false)
                PlayGlobalData.hasPreChapter.set(false)
            }
            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
            val chapterId = PlayGlobalData.playChapterId.get()
            if (chapterId != null && !TextUtils.isEmpty(chapterId)) {
                if (currentPlayerMusic != null) {
                    //传入的章节id与正在播放的章节id进行对比，如果不一致，则播放传入的章节，一致则不用处理，继续播放该章节即可
                    if (currentPlayerMusic.chapterId != chapterId) {
                        startPlayChapter(playPath, chapterId, currentPlayerMusic)
                    } else {
                        GlobalPlayHelper.INSTANCE.onPlayMusiconInfo(
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
    }

    private fun startPlayChapter(
        playPath: List<BaseAudioInfo>,
        chapterId: String,
        currentPlayerMusic: BaseAudioInfo
    ) {
        musicPlayerManger.updateMusicPlayerData(audios = playPath, chapterId = chapterId)
        musicPlayerManger.startPlayMusic(chapterId = chapterId)
//        mViewModel.maxProcess.set(currentPlayerMusic.duration.toFloat())
        when {
            playCurrentDuration <= 0 -> {
                PlayGlobalData.process.set(0F)
            }
            else -> {
                if (playCurrentDuration > currentPlayerMusic.duration) {
                    playCurrentDuration = 0
                }
                musicPlayerManger.seekTo(playCurrentDuration)
            }
        }
    }

    override fun initData() {
        PlayGlobalData.playAudioId.set(playAudioId)
        PlayGlobalData.playChapterListSort.set(playSortType)
        //书籍信息未传入，获取书籍详情信息,有则直接使用
        if (TextUtils.isEmpty(playAudioModel.audio_cover_url)) {
            mViewModel.getDetailInfo(playAudioId)
        } else {
            PlayGlobalData.initPlayAudio(playAudioModel)
        }
        //如果传入的章节id为空，说明不是通过章节列表跳转的，直接访问书籍章节列表的第一页数据即可

        if (playChapterList.isNotEmpty()) {
            PlayGlobalData.playChapterList.value = playChapterList
        } else {
            if (TextUtils.isEmpty(playChapterId)) {
                mViewModel.getNextPageChapterList()
            } else {
                val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
                if (currentPlayerMusic != null && currentPlayerMusic.chapterId == playChapterId) {
                    PlayGlobalData.maxProcess.set(currentPlayerMusic.duration.toFloat())
                    if(musicPlayerManger.getCurDurtion()>=currentPlayerMusic.duration){
                        PlayGlobalData.process.set(0F)
                        musicPlayerManger.seekTo(0L)
                    }else{
                        PlayGlobalData.process.set(musicPlayerManger.getCurDurtion().toFloat())
                    }
                }
                PlayGlobalData.playChapterId.set(playChapterId)
                mViewModel.getChapterListWithId(audioId = playAudioId, chapterId = playChapterId)
            }
        }
        playChapterId = ""
        mViewModel.getCommentList()

    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        if (playerState == -1) {
            tipView.showTipView(this, this.getString(R.string.business_play_error))
            mViewModel.playManger.pause()
        }

    }

//    //播放完成
//    override fun onCompletionPlay() {
//        if (mViewModel.playManger.getPlayerAlarmModel() < 10) {
//            if (mViewModel.playManger.getRemainingSetInt() > 0) {
//                mViewModel.countdownTime.set("${mViewModel.playManger.getRemainingSetInt()}集")
//            } else {
//                mViewModel.countdownTime.set("")
//            }
//        }
//    }

    override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
        getBaseContainer().background.mutate().alpha = 1
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind.playCommentRv.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val height = mDataBind.bg.height
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / height
                    }
                    mDataBind.bg.alpha = alpha
                }
            }
        })
    }
}