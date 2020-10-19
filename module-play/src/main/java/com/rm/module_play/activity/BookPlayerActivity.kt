package com.rm.module_play.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.ktx.putAnyExtras
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.wedgit.swipleback.SwipeBackLayout
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.R.anim.activity_top_open
import com.rm.module_play.cache.PlayBookState
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
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
    var fromJumpType: String? = null

    //是否重置播放
    var isResumePlay = false

    companion object {
        const val chapterListModel = "chapterListModel"
        const val fromJump = "fromJump"
        const val fromGlobalCache = "fromGlobalCache"

        //带书籍详情进入
        fun startActivity(
            context: Context,
            homeDetailBean: DetailBookBean?,
            from: String
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
            from: String
        ) {
            chapter?.let {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putAnyExtras(chapterListModel, it)
                intent.putAnyExtras(fromJump, from)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(activity_top_open, 0);
            }
        }

        //全局原点
        fun startActivity(context: Context, fromGlobal: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putAnyExtras(fromJump, fromGlobal)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(activity_top_open, 0);

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
            (context as Activity).overridePendingTransition(activity_top_open, 0);

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
        mViewModel.playPath.observe(this, Observer { it ->
            it?.let { it1 ->
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                isResumePlay = true
                when (fromJumpType) {
                    Jump.DOTS.from -> {
                        mChapterId?.let {
                            if (!musicPlayerManger.isPlaying()) {
                                musicPlayerManger.updateMusicPlayerData(it1, it)
                                musicPlayerManger.startPlayMusic(it)
                            }

                        }

                    }
                    Jump.DETAILSBOOK.from -> {
                        mChapterId = it.getOrNull(0)?.chapterId
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
                            showPlayBookListDialog(it, {
                                mChapterId?.let {
                                    musicPlayerManger.startPlayMusic(it)
                                }
                            }, { type ->
                                if (type == 0) {
                                    //                                    ToastUtil.show(this@BookPlayerActivity, "刷新")
                                } else {
                                    //                                    ToastUtil.show(this@BookPlayerActivity, "加载更多")
                                }
                            })
                        }
                    }
                    controlTime?.contains(ACTION_PLAY_OPERATING) == true -> {
                        showMusicPlayMoreDialog { it1 ->
                            if (it1 == 0) {
                                showMusicPlayTimeSettingDialog()
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
                                    it
                                )
                        }

                    }
                    controlTime?.contains(ACTION_MORE_COMMENT) == true -> {
                        mViewModel.audioID.get()?.let {
                            CommentCenterActivity.toCommentCenterActivity(
                                this@BookPlayerActivity, it
                            )
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
        mViewModel.mutableList.observe(this, Observer {
            mViewModel.mBookPlayerAdapter.setList(it)
        })
        mViewModel.playBookSate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

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
                mViewModel.initPlayBookSate(fromGlobalCache.getObjectMMKV(PlayBookState::class.java))

            }
            //从最近播放进来
            Jump.RECENTPLAY.from -> {
                val chapterId = intent.getStringExtra(paramChapterId)
                val audioId = intent.getStringExtra(paramAudioId)
                mViewModel.audioID.set(audioId)
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
                    mViewModel.setBookDetailBean(it)
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
                    mViewModel.audioID.set(it.audio_id)
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

            else -> {

            }

        }


    }

    override fun onPause() {
        super.onPause()
        fromGlobalCache.putMMKV(mViewModel.playBookSate.get())


    }

    override fun initData() {
        getIntentParams()

    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {

    }

    override fun onPrepared(totalDurtion: Long) {
        mViewModel.maxProcess.set(totalDurtion.toFloat())
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
        getRecentPlayBook(musicInfo)
    }

    /**
     * 设置当前播放页面
     */
    private fun getRecentPlayBook(
        musicInfo: BaseAudioInfo
    ) {
        val playerControl = mViewModel.playControlModel.get()
        playerControl?.baseAudioInfo = musicInfo
        mViewModel.playControlModel.set(playerControl)
        mViewModel.playControlModel.notifyChange()
        mViewModel.playReport(
            playerControl?.baseAudioInfo?.audioId ?: "",
            playerControl?.baseAudioInfo?.chapterId ?: ""
        )
        mViewModel.lastState.set(mViewModel.pathList.size > 0 && mChapterId?.isNotEmpty() == true)
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
        mViewModel.playSate.set(playWhenReady)

    }

    override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
        getBaseContainer().background.mutate().alpha = (255 * (1 - fractionScreen)).toInt()
    }


}