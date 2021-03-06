package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.share.ShareManage
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
        var isContinuePlay : Boolean = false

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
                if (playAudioId.isEmpty() || playChapterId.isEmpty()) {
                    isContinuePlay = false
                    ToastUtil.showTopToast(context, context.getString(R.string.play_audio_not_be_empty))
                    return
                }else{
                    BaseConstance.basePlayInfoModel.get()?.let {
                        //如果播放的书籍或者章节不一致，则先把播放器的数据清除掉
                        if(playAudioId!=it.playAudioId || playChapterId!=it.playChapterId){
                            isContinuePlay = false
                            PlayGlobalData.playChapterList.value = mutableListOf()
                            BaseConstance.updateBaseChapterId(playAudioId, playChapterId)
                        }else{
                            isContinuePlay = true
                            if(sortType!=PlayGlobalData.playChapterListSort.get()){
                                PlayGlobalData.playChapterList.value?.let { playList ->
                                    playList.reverse()
                                    PlayGlobalData.playChapterList.value = playList
                                }
                            }
                            BaseConstance.basePlayProgressModel.get()?.let { progressModel ->
                                playCurrentDuration = if(progressModel.currentDuration >= progressModel.totalDuration){
                                    1L
                                }else{
                                    progressModel.currentDuration
                                }
                            }
                        }
                    }
                    PlayGlobalData.playAudioId.set(playAudioId)
                    PlayGlobalData.playChapterId.set(playChapterId)
                    PlayGlobalData.playChapterListSort.set(playSortType)
                }
                val intent = Intent(context, BookPlayerActivity::class.java)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0)
            } catch (e: Exception) {
                ToastUtil.showTopToast(context, "${e.message}")
            }
        }
    }

    private var needShowNetErrorChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var rvOnScrollListener: RecyclerView.OnScrollListener? = null


    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalPlayHelper.INSTANCE.registerPlayStatusListener(this@BookPlayerActivity)
        GlobalPlayHelper.INSTANCE.addPlayEventListener()
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

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setTransparentStatusBar()

        mDataBind?.let {
            it.swipeBackLayout.apply {
                setDragEdge(SwipeBackLayout.DragEdge.TOP)
                setOnSwipeBackListener(this@BookPlayerActivity)
                setScrollChild(it.playCommentRv)
            }
            setTitleTpoMargin()
            recycleScrollListener()

            it.playCommentRv.apply {
                bindVerticalLayout(mViewModel.mCommentAdapter)
                createHead(this)
            }
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
        (mDataBind?.reportWhyArrow?.layoutParams as ViewGroup.MarginLayoutParams).apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin =
                getStateHeight(this@BookPlayerActivity) + resources.getDimensionPixelOffset(R.dimen.dp_10)
        }
    }

    override fun startObserve() {

        PlayGlobalData.playChapterList.observe(this, Observer { playPath ->

            DLog.d("music-exoplayer-lib","playPath = ${playPath.size}")

            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
            val chapterId = PlayGlobalData.playChapterId.get()
            if (chapterId != null && !TextUtils.isEmpty(chapterId) && playPath.isNotEmpty()) {
                updateMusicPlayerData(playPath, chapterId)
                if (currentPlayerMusic != null) {
                    //传入的章节id与正在播放的章节id进行对比，如果不一致，则播放传入的章节，一致则不用处理，继续播放该章节即可
                    if (currentPlayerMusic.chapterId != chapterId) {
                        startPlayChapter(chapterId, currentPlayerMusic.duration)
                    } else {
                        GlobalPlayHelper.INSTANCE.onPlayMusiconInfo(
                            currentPlayerMusic,
                            musicPlayerManger.getCurrentPlayIndex(), false
                        )
                    }
                } else {
                    if (playPath != null && playPath.isNotEmpty()) {
                        val predicate: (DownloadChapter) -> Boolean =
                            { chapterId == it.chapter_id.toString() }
                        val firstIndex = playPath.indexOfFirst(predicate)
                        if (firstIndex != -1) {
                            startPlayChapter(chapterId, playPath[firstIndex].realDuration)
                            PlayGlobalData.setPlayHasNextAndPre(playPath, firstIndex)
                        } else {
                            startPlayChapter(
                                playPath[0].chapter_id.toString(),
                                playPath[0].duration
                            )
                            PlayGlobalData.setPlayHasNextAndPre(playPath, 0)
                        }
                    }
                }
            } else {
                if (playPath != null && playPath.isNotEmpty()) {
                    updateMusicPlayerData(playPath, playPath[0].chapter_id.toString())
                    startPlayChapter(playPath[0].chapter_id.toString(), playPath[0].duration)
                    PlayGlobalData.setPlayHasNextAndPre(playPath, 0)
                }
            }
        })

        needShowNetErrorChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                AriaDownloadManager.needShowNetError.get().let {
                    if(it){
                       ToastUtil.showTopNetErrorToast(this@BookPlayerActivity)
                    }
                }
            }
        }
        needShowNetErrorChangedCallback?.let {
            AriaDownloadManager.needShowNetError.addOnPropertyChangedCallback(it)
        }
    }

    /**
     * 更新播放器数据
     */
    private fun updateMusicPlayerData(playPath: List<DownloadChapter>, chapterId: String) {
        val baseAudioList = mutableListOf<BaseAudioInfo>()
        playPath.forEach { chapter ->
            val playUrl =
                if (TextUtils.isEmpty(DownLoadFileUtils.getPlayChapterFilePath(chapter))) {
                    chapter.path_url
                } else {
                    DownLoadFileUtils.getPlayChapterFilePath(chapter)
                }
            baseAudioList.add(
                BaseAudioInfo(
                    audioPath = playUrl,
                    audioName = chapter.chapter_name,
                    filename = chapter.chapter_name,
                    audioId = chapter.audio_id.toString(),
                    chapterId = chapter.chapter_id.toString(),
                    duration = chapter.realDuration,
                    playCount = chapter.play_count.toString()
                )
            )
        }
        musicPlayerManger.updateMusicPlayerData(audios = baseAudioList, chapterId = chapterId)
    }

    /**
     * 判断
     */
    private fun startPlayChapter(
        chapterId: String,
        chapterDuration: Long
    ) {
        //如果播放进度大于章节进度，则重新从0开始
        if (playCurrentDuration >= chapterDuration) {
            playCurrentDuration = 0
        }
        //如果播放的是上一次播放的
        if (isContinuePlay) {
            musicPlayerManger.setAdPath(arrayListOf())
            musicPlayerManger.startPlayMusic(chapterId = chapterId)
            return
        }

        mViewModel.getChapterAd { musicPlayerManger.startPlayMusic(chapterId = chapterId) }


    }

    override fun initData() {
        //书籍信息未传入，获取书籍详情信息,有则直接使用
        if (TextUtils.isEmpty(playAudioModel.audio_cover_url)) {
            mViewModel.getAudioInfo(playAudioId)
        } else {
            PlayGlobalData.playAudioModel.set(playAudioModel)
            mViewModel.getAudioInfo(playAudioId)
        }
        //首次进入获取一次广告数据
        mViewModel.getAudioFloorAd()
        mViewModel.getAudioRecommend(playAudioId)
        //如果传入的章节id为空，说明不是通过章节列表跳转的，直接访问书籍章节列表的第一页数据即可

        if (playChapterList.isNotEmpty()) {
            mViewModel.isLocalChapterList.set(true)
            PlayGlobalData.playChapterId.set(playChapterId)
            PlayGlobalData.playChapterList.value = playChapterList
            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
            if (currentPlayerMusic != null && currentPlayerMusic.chapterId == playChapterId) {
                PlayGlobalData.maxProcess.set(currentPlayerMusic.duration.toFloat())
                if (playCurrentDuration == 1L) {
                    PlayGlobalData.process.set(0F)
                    PlayGlobalData.playNeedQueryChapterProgress.set(false)
                    musicPlayerManger.seekTo(0L)
                } else {
                    PlayGlobalData.process.set(musicPlayerManger.getCurDurtion().toFloat())
                }
            }
        } else {
            mViewModel.isLocalChapterList.set(false)
            PlayGlobalData.playChapterId.set(playChapterId)
            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()
            if (currentPlayerMusic != null && currentPlayerMusic.chapterId == playChapterId) {
                PlayGlobalData.maxProcess.set(currentPlayerMusic.duration.toFloat())
                if (playCurrentDuration == 1L) {
                    PlayGlobalData.process.set(0F)
                    PlayGlobalData.playNeedQueryChapterProgress.set(false)
                    musicPlayerManger.seekTo(0L)
                } else {
                    PlayGlobalData.process.set(musicPlayerManger.getCurDurtion().toFloat())
                }
            } else {
                DLog.d("music-exoplayer-lib","initData  playAudioId = ${playAudioId} -- playChapterId = $playChapterId")
                PlayGlobalData.process.set(0F)
                mViewModel.getChapterListWithId(audioId = playAudioId, chapterId = playChapterId)
            }
        }
        playChapterId = ""
        mViewModel.getCommentList()
    }

    /**
     * 分享
     */
    fun clickShare(view: View) {
        ShareManage.shareChapter(
            this,
            PlayGlobalData.playAudioId.get() ?: "",
            PlayGlobalData.playChapterId.get() ?: "",
            PlayGlobalData.playChapter.get()?.chapter_name ?: ""
        )
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
        if (playerState == -1) {
            ToastUtil.showTopToast(this, this.getString(R.string.business_play_error))
            mViewModel.playManger.pause()
        }

    }

    override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
        getBaseContainer().background.mutate().alpha = 1
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        rvOnScrollListener = object : RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val height = mDataBind?.bg?.height ?: 0
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / height
                    }
                    mDataBind?.bg?.alpha = alpha
                }
            }
        }
        rvOnScrollListener?.let {
            mDataBind?.playCommentRv?.addOnScrollListener(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        needShowNetErrorChangedCallback?.let {
            AriaDownloadManager.needShowNetError.removeOnPropertyChangedCallback(it)
            needShowNetErrorChangedCallback = null
        }
        rvOnScrollListener?.let {
            mDataBind?.playCommentRv?.removeOnScrollListener(it)
            rvOnScrollListener = null
        }
    }
}