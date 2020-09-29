package com.rm.module_play.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.DetailBookBean
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.adapter.BookPlayerAdapter
import com.rm.module_play.cache.PlayBookState
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_GET_PLAYINFO_LIST
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_JOIN_LISTEN
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_MORE_COMMENT
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_OPERATING
import com.rm.module_play.viewmodel.PlayViewModel.Companion.ACTION_PLAY_QUEUE
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger


@Suppress(
    "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
/**
 * 播放器主要界面
 */
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
    MusicPlayerEventListener {

    private val mBookPlayerAdapter: BookPlayerAdapter by lazy {
        BookPlayerAdapter(mViewModel, BR.viewModel, BR.itemModel)

    }
    var indexSong = 0
    var fromGlobalValue: String? = ""
    //是否重置播放
    var isResumePlay = false

    companion object {
        const val homeDetailModel = "homeDetailModel"
        const val songsIndex = "songIndex"
        const val fromGlobal = "fromGlobal"
        const val fromGlobalCache = "fromGlobalCache"
        fun startActivity(
            context: Context,
            homeDetailBean: DetailBookBean?,
            index: Int = 0
        ) {
            if (homeDetailBean != null) {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putExtra(homeDetailModel, homeDetailBean)
                intent.putExtra(songsIndex, index)
                context.startActivity(intent)
            }
        }

        fun startActivity(context: Context, fromGlobal: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putExtra(BookPlayerActivity.fromGlobal, fromGlobal)
            context.startActivity(intent)
        }
    }


    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        setStatusBar(R.color.businessWhite)
        mViewModel.initPlayerAdapterModel()
        mDataBind.rvMusicPlay.bindVerticalLayout(mBookPlayerAdapter)
    }

    override fun startObserve() {
        mViewModel.playPath.observe(this, Observer {
            it?.let { it1 ->
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                if ((fromGlobalValue.orEmpty().isEmpty() &&
                            musicPlayerManger.getCurrentPlayerMusic()?.chapterId != it1.getOrNull(
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
                    musicPlayerManger.getCurrentPlayerMusic()?.let {
                        getRecentPlayBook(it, indexSong)
                    }

                }

            }
        })

        mViewModel.playControlAction.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when (mViewModel.playControlAction.get()) {
                    ACTION_PLAY_QUEUE -> {
                        //调整播放列表
                        mViewModel.audioChapterModel.get()?.let {
                            showPlayBookListDialog(it) { position ->
                                musicPlayerManger.startPlayMusic(position)
                            }
                        }

                    }
                    ACTION_PLAY_OPERATING -> {
                        showMusicPlayMoreDialog { it1 ->
                            if (it1 == 0) {
                                showMusicPlayTimeSettingDialog()
                            } else {
                                showMusicPlaySpeedDialog()
                            }
                        }
                    }
                    ACTION_GET_PLAYINFO_LIST -> {
                        navigateToForResult(ARouterPath.testPath, 100)
                    }
                    ACTION_JOIN_LISTEN -> {
                        mViewModel.homeDetailBean.get()?.let {
                            RouterHelper.createRouter(ListenService::class.java)
                                .showMySheetListDialog(
                                    mViewModel,
                                    this@BookPlayerActivity,
                                    it.audio_id
                                )
                        }

                    }
                    ACTION_MORE_COMMENT -> {
                        ToastUtil.show(this@BookPlayerActivity, "更多评论")
                    }
                    else -> {

                    }
                }
            }
        })
        mViewModel.mutableList.observe(this, Observer {
            mBookPlayerAdapter.setList(it)
        })
        mViewModel.playBookSate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                indexSong=mViewModel.playBookSate.get()?.index?:0

            }

        })
    }

    /**
     * 获取参数
     */
    private fun getIntentParams() {
        //正在播放的对象

        //来自全局小圆圈点击进来
        fromGlobalValue = intent.getStringExtra(fromGlobal)
        if (fromGlobalValue.orEmpty().isNotEmpty()) {
            mViewModel.initPlayBookSate(fromGlobalCache.getObjectMMKV(PlayBookState::class.java))

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
        mViewModel.playSate.set(playbackState)
        indexSong = mViewModel.playBookSate.get()?.index ?: 0
    }

    /**
     * 判断服务是否正在运行
     */
    private fun isServiceWork(
        serviceName: String
    ): Boolean {
        var isWork = false
        val myList: List<ActivityManager.RunningServiceInfo> =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(100)
        if (myList.isEmpty()) {
            return false
        }
        myList.forEach {
            val mName: String = it.service.className
            if (mName == serviceName) {
                isWork = true
                return isWork
            }
        }
        return isWork
    }

}