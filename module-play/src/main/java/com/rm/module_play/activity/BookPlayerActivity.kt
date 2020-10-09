package com.rm.module_play.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.bean.DetailBookBean
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
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
import com.yinglan.scrolllayout.ScrollLayout
import kotlinx.android.synthetic.main.activity_book_player.*


@Suppress(
    "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
/**
 * 播放器主要界面
 */
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
    MusicPlayerEventListener {


    private val mOnScrollChangedListener: ScrollLayout.OnScrollChangedListener =
        object : ScrollLayout.OnScrollChangedListener {
            override fun onScrollProgressChanged(currentProgress: Float) {
                if (currentProgress >= 0) {
                    var precent = 255 * currentProgress
                    if (precent > 255) {
                        precent = 255f
                    } else if (precent < 0) {
                        precent = 0f
                    }
                    val alpha = precent.toInt()
                    root_play.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
                }

            }

            override fun onScrollFinished(currentStatus: ScrollLayout.Status) {
                scrollStatus = currentStatus
                if (currentStatus == ScrollLayout.Status.EXIT || currentStatus == ScrollLayout.Status.OPENED) {
                    finish()
                }
            }

            override fun onChildScroll(top: Int) {

            }
        }
    var indexSong = 0
    var fromJumpType: String? = null
    var scrollStatus = ScrollLayout.Status.CLOSED

    //是否重置播放
    var isResumePlay = false

    companion object {
        const val homeDetailModel = "homeDetailModel"
        const val songsIndex = "songIndex"
        const val fromJump = "fromJump"
        const val fromGlobalCache = "fromGlobalCache"

        //网络信息加载
        fun startActivity(
            context: Context,
            homeDetailBean: DetailBookBean?,
            from: String,
            index: Int = 0
        ) {
            if (homeDetailBean != null) {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putExtra(homeDetailModel, homeDetailBean)
                intent.putExtra(songsIndex, index)
                intent.putExtra(fromJump, from)
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.activity_top_open, 0);

            }
        }

        //全局原点
        fun startActivity(context: Context, fromGlobal: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putExtra(fromJump, fromGlobal)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.activity_top_open, 0);

        }

        //从最近播放进入
        const val paramChapterId = "chapterId"
        const val paramAudioId = "audioId"
        fun startActivity(context: Context, chapterId: String, audioId: String, from: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putExtra(paramChapterId, chapterId)
            intent.putExtra(fromJump, from)
            intent.putExtra(paramAudioId, audioId)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.activity_top_open, 0);

        }


    }

    override fun finish() {
        super.finish()
        if (scrollStatus == ScrollLayout.Status.EXIT || scrollStatus == ScrollLayout.Status.OPENED) {
            overridePendingTransition(0, 0)
            scrollStatus = ScrollLayout.Status.CLOSED
        } else {
            overridePendingTransition(R.anim.activity_bottom_close, 0);

        }

    }

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        setStatusBar(R.color.businessWhite)
        /**设置 setting*/
        scroll_down_layout.setMinOffset(0)
        scroll_down_layout.setMaxOffset(((screenHeight * 0.5).toInt()))
        scroll_down_layout.setExitOffset(dip(50))
        scroll_down_layout.setIsSupportExit(true)
        scroll_down_layout.isAllowHorizontalScroll = true
        scroll_down_layout.setOnScrollChangedListener(mOnScrollChangedListener)
        scroll_down_layout.scrollToClose()
        scroll_down_layout.background?.mutate()?.alpha = 0
        mViewModel.initPlayerAdapterModel()


    }

    override fun startObserve() {
        mViewModel.playPath.observe(this, Observer { it ->
            it?.let { it1 ->
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                if ((fromJumpType == Jump.DOTS.from && musicPlayerManger.getCurrentPlayerMusic()?.chapterId != it1.getOrNull(
                        indexSong
                    )?.chapterId) ||
                    musicPlayerManger.getCurrentPlayerMusic() == null
                ) {
                    musicPlayerManger.updateMusicPlayerData(it1, indexSong)
                    musicPlayerManger.startPlayMusic(indexSong)
                } else {
                    //从圆点进入并且没有播放
                    if (!musicPlayerManger.isPlaying()) {
                        musicPlayerManger.updateMusicPlayerData(it1, indexSong)
                        musicPlayerManger.startPlayMusic(indexSong)
                    }
                    //相同书籍和章节进来
                    isResumePlay = true
                    musicPlayerManger.getCurrentPlayerMusic()?.let { audio ->
                        getRecentPlayBook(audio, indexSong)
                    }

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
                                musicPlayerManger.startPlayMusic(it)
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
                        mViewModel.homeDetailBean.get()?.let {
                            RouterHelper.createRouter(ListenService::class.java)
                                .showMySheetListDialog(
                                    mViewModel,
                                    this@BookPlayerActivity,
                                    it.audio_id
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
                indexSong = mViewModel.playBookSate.get()?.index ?: 0
            }
        })
    }

    /**
     * 获取参数
     */
    private fun getIntentParams() {
        //正在播放的对象
        //来自全局小圆圈点击进来
        fromJumpType = intent.getStringExtra(fromJump)
        when (fromJumpType) {
            Jump.DOTS.from -> {
                mViewModel.initPlayBookSate(fromGlobalCache.getObjectMMKV(PlayBookState::class.java))

            }
            Jump.RECENTPLAY.from -> {
                val chapterId = intent.getStringExtra(paramChapterId)
                val audioId = intent.getStringExtra(paramAudioId)


            }
            else -> {

            }
        }

        if (intent.getSerializableExtra(homeDetailModel) as? DetailBookBean != null) {
            val homeDetailBean = intent.getSerializableExtra(homeDetailModel) as? DetailBookBean
            mViewModel.seBookDetailBean(homeDetailBean)
            homeDetailBean?.let {
                mViewModel.chapterList(
                    it.audio_id,
                    1,
                    20,
                    "asc",
                    it.audio_cover_url
                )
            }
            indexSong = intent.getIntExtra(songsIndex, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        mViewModel.playBookSate.get()?.index = indexSong
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
        getRecentPlayBook(musicInfo, position)
    }

    /**
     * 设置当前播放页面
     */
    private fun getRecentPlayBook(
        musicInfo: BaseAudioInfo,
        position: Int
    ) {
        val playerControl = mViewModel.playControlModel.get()
        playerControl?.baseAudioInfo = musicInfo
        mViewModel.playControlModel.set(playerControl)
        mViewModel.playControlModel.notifyChange()
        this.indexSong = position
        mViewModel.playReport(
            playerControl?.baseAudioInfo?.audioId ?: "",
            playerControl?.baseAudioInfo?.chapterId ?: ""
        )
        mViewModel.lastState.set(mViewModel.pathList.size > 0 && position > 0)
        mViewModel.updatePlayBook(
            mViewModel.audioChapterModel.get()?.chapter_list?.getOrNull(position)
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
            mViewModel.updatePlayBookProcess(it.chapter_list[indexSong], currentDurtion)
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
        indexSong = mViewModel.playBookSate.get()?.index ?: 0
    }


}