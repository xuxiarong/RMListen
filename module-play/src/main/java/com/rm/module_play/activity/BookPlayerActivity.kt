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
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.ktx.putAnyExtras
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.play.PlayState
import com.rm.business_lib.wedgit.swipleback.SwipeBackLayout
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.PlayConstance
import com.rm.module_play.R
import com.rm.module_play.R.anim.activity_top_open
import com.rm.module_play.cache.PlayBookState
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.dialog.*
import com.rm.module_play.enum.Jump
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


    var mChapterId: String? = null
    private var fromJumpType: String? = null

    //是否重置播放
    private var isResumePlay = false


    companion object {

        const val chapterListModel = "chapterListModel"
        const val fromJump = "fromJump"
        const val fromGlobalCache = "fromGlobalCache"

        //记录上次打开的时间，防止多次快速点击打开多次，影响体验
        var lastOpenTime: Long = SystemClock.currentThreadTimeMillis()
        var playAudioId: Long = 0L
        var playAudioInfo: DownloadAudio = DownloadAudio()
        var playChapterId: Long = 0L
        var playChapterList: List<ChapterList> = mutableListOf()
        var playSortType: String = "scs"

        fun startPlayActivity(
            context: Context,
            audioId: Long = 0L,
            audioInfo: DownloadAudio = DownloadAudio(),
            ChapterId: Long = 0L,
            chapterList: List<ChapterList> = mutableListOf(),
            sortType: String = "scs"
        ) {
            //防止连续打开多次
            if (SystemClock.currentThreadTimeMillis() - lastOpenTime < 100) {
                lastOpenTime = SystemClock.currentThreadTimeMillis()
                return
            }
            lastOpenTime = SystemClock.currentThreadTimeMillis()
            playAudioId = audioId
            playAudioInfo = audioInfo
            playChapterId = ChapterId
            playChapterList = chapterList
            playSortType = sortType
            val intent = Intent(context, BookPlayerActivity::class.java)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0)

        }


        //带书籍详情进入
        fun startActivity(
            context: Context,
            homeDetailBean: DetailBookBean?,
            from: String,
            sortType: String
        ) {
            homeDetailBean?.let {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putAnyExtras(chapterListModel, it)
                intent.putAnyExtras(fromJump, from)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0)
            }
        }

        //某一个章节进来
        fun startActivity(
            context: Context,
            chapter: ChapterList?,
            from: String,
            sortType: String
        ) {
            chapter?.let {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putAnyExtras(chapterListModel, it)
                intent.putAnyExtras(fromJump, from)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0)
            }
        }

        //全局原点
        fun startActivity(context: Context, fromGlobal: String) {

            if (TextUtils.isEmpty(PlayConstance.getLastListenAudioUrl())) {
                (context as BaseActivity).tipView.showTipView(
                    context,
                    context.getString(com.rm.business_lib.R.string.business_no_content)
                )
                return
            }
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putAnyExtras(fromJump, fromGlobal)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0)

        }

        //从最近播放进入
        const val paramChapterId = "chapterId"
        const val paramAudioId = "audioId"
        fun startActivity(context: Context, chapterId: String, audioId: String, from: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putAnyExtras(paramChapterId, chapterId)
            intent.putAnyExtras(fromJump, from)
            intent.putAnyExtras(paramAudioId, audioId)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0)

        }

        /**
         * 从订阅新增进来
         */
        fun startActivity(
            context: Context,
            book: List<ChapterList>,
            chapterId: String, from: String
        ) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putAnyExtras(paramChapterId, chapterId)
            intent.putAnyExtras(fromJump, from)
            intent.putAnyExtras(chapterListModel, book)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0)

        }


        const val downloadAudio = "download_audio"

        /**
         * 从本地列表过来
         */
        fun startActivity(
            context: Context,
            audio: DownloadAudio,
            chapterId: String,
            from: String
        ) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putAnyExtras(paramChapterId, chapterId)
            intent.putAnyExtras(fromJump, from)
            intent.putAnyExtras(paramAudioId, audio.audio_id)
            intent.putAnyExtras(downloadAudio, audio)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0)

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
            playPath?.let { it1 ->
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                isResumePlay = true
                if (playPath.size <= 1) {
                    mViewModel.hasNextChapter.set(false)
                    mViewModel.hasPreChapter.set(false)
                }
                when (fromJumpType) {
                    Jump.DOTS.from -> {
                        if (musicPlayerManger.getCurrentPlayerMusic() != null) {
                            val currentPlayerMusic = musicPlayerManger.getCurrentPlayerMusic()!!
                            mChapterId = currentPlayerMusic.chapterId

                        }
                        mChapterId?.let {
                            if (!musicPlayerManger.isPlaying()) {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)
                            }
                        }
                    }
                    Jump.DETAILSBOOK.from -> {
                        mChapterId = playPath.getOrNull(0)?.chapterId
                        mChapterId?.let {
                            if (musicPlayerManger.isPlaying()) {
                                if (musicPlayerManger.getCurrentPlayerMusic()?.chapterId != mChapterId) {
                                    musicPlayerManger.updateMusicPlayerData(it1, it)
                                    musicPlayerManger.startPlayMusic(it)
                                }
                            } else {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)

                            }
                        }


                    }
                    Jump.CHAPTER.from -> {
                        mChapterId?.let {
                            if (musicPlayerManger.isPlaying()) {
                                if (musicPlayerManger.getCurrentPlayerMusic()?.chapterId != mChapterId) {
                                    musicPlayerManger.updateMusicPlayerData(it1, it)
                                    musicPlayerManger.startPlayMusic(it)
                                }
                            } else {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)

                            }
                        }
                    }
                    Jump.RECENTPLAY.from -> {
                        mChapterId?.let {
                            if (musicPlayerManger.isPlaying()) {
                                if (musicPlayerManger.getCurrentPlayerMusic()?.chapterId != mChapterId) {
                                    musicPlayerManger.updateMusicPlayerData(it1, it)
                                    musicPlayerManger.startPlayMusic(it)
                                }
                            } else {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)
                            }
                        }
                    }
                    Jump.SUBBOOKS.from -> {

                    }
                    Jump.DOWNLOAD.from -> {
                        mChapterId?.let {
                            if (musicPlayerManger.isPlaying()) {
                                if (musicPlayerManger.getCurrentPlayerMusic()?.chapterId != mChapterId) {
                                    musicPlayerManger.updateMusicPlayerData(it1, it)
                                    musicPlayerManger.startPlayMusic(it)
                                }
                            } else {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)
                            }
                        }
                    }
                    else -> {

                    }
                }

            }
            musicPlayerManger.getCurrentPlayerMusic()?.let { audio ->
                mChapterId?.let {
                    getRecentPlayBook(audio)

                }
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
                        mChapterId?.let {
                            RouterHelper.createRouter(ListenService::class.java)
                                .showMySheetListDialog(
                                    mViewModel,
                                    this@BookPlayerActivity,
                                    mViewModel.audioId.get()!!
                                )
                        }

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

    /**
     * 获取参数
     */
    private fun getIntentParams() {
        //正在播放的对象
        fromJumpType = intent.getStringExtra(fromJump)
        when (fromJumpType) {
            //从圆圈进来
            Jump.DOTS.from -> {
                val playBook = fromGlobalCache.getObjectMMKV(PlayBookState::class.java)
                mViewModel.initPlayBookSate(playBook)

            }
            //从最近播放进来
            Jump.RECENTPLAY.from -> {
                val chapterId = intent.getStringExtra(paramChapterId)
                val audioId = intent.getStringExtra(paramAudioId)
                mViewModel.audioId.set(audioId)
                mChapterId = chapterId
                mViewModel.getDetailInfo(audioId)
                mViewModel.chapterPageList(
                    audioId,
                    chapterId, "asc"
                )
            }
            //从详情全部进来
            Jump.DETAILSBOOK.from -> {
                (intent.getSerializableExtra(chapterListModel) as? DetailBookBean)?.let {
                    mViewModel.chapterList(
                        it.audio_id,
                        1,
                        20,
                        "asc"
                    )
                }
            }
            //从详情某章节进来
            Jump.CHAPTER.from -> {
                (intent.getSerializableExtra(chapterListModel) as? ChapterList)?.let {
                    mChapterId = it.chapter_id
                    mViewModel.audioId.set(it.audio_id)
                    mViewModel.chapterList(
                        it.audio_id,
                        1,
                        20,
                        "asc"
                    )
                    mViewModel.getDetailInfo(it.audio_id)
                }
            }
            //从订阅进来
            Jump.SUBBOOKS.from -> {
                mChapterId = intent.getStringExtra(paramChapterId)
                (intent.getSerializableExtra(chapterListModel) as? List<ChapterList>)?.let {
                    mViewModel.setPlayPath(it)
                }
            }

            //从下载进来
            Jump.DOWNLOAD.from -> {
                mChapterId = intent.getStringExtra(paramChapterId)
                val audio: DownloadAudio =
                    intent.getSerializableExtra(downloadAudio) as DownloadAudio

                val chapterList = arrayListOf<ChapterList>()
                audio.chapterList.forEach {
                    chapterList.add(ChapterList.copyFromDownload(it))
                }
                mViewModel.setPlayPath(chapterList)

                mViewModel.audioChapterModel.set(
                    AudioChapterListModel(
                        list = chapterList,
                        total = chapterList.size,
                        Anthology_list = mutableListOf()
                    )
                )
            }

            else -> {

            }

        }


    }

    override fun onPause() {
        super.onPause()
        fromGlobalCache.putMMKV(mViewModel.playBookSate.get())
    }

    override fun initData() {
        mViewModel.countdown()
        getIntentParams()
        mViewModel.getCommentList()
    }

    private fun checkData() {

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
            mViewModel.audioChapterModel.get()?.list?.find { it.chapter_id == mChapterId }
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
                it.list?.find { it.chapter_id == mChapterId },
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