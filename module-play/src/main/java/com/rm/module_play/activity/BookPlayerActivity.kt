package com.rm.module_play.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.navigateToForResult
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.adapter.BookPlayerAdapter
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.dialog.showMusicPlayMoreDialog
import com.rm.module_play.dialog.showMusicPlaySpeedDialog
import com.rm.module_play.dialog.showMusicPlayTimeSettingDialog
import com.rm.module_play.dialog.showPlayBookListDialog
import com.rm.module_play.model.PlayControlModel
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
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
    MusicPlayerEventListener {
    private val mBookPlayerAdapter: BookPlayerAdapter by lazy {
        BookPlayerAdapter(mViewModel, BR.viewModel, BR.itemModel)

    }
    var homeDetailBean: HomeDetailModel? = null
    var indexSong = 0
    var fromGlobalValue: String? = ""

    companion object {
        val homeDetailModel = "homeDetailModel"
        val audioChapterListModel = "AudioChapterListModel"
        val songindex = "songIndex";
        val fromGlobal = "fromGlobal"
        fun startActivity(
            context: Context,
            homeDetailBean: HomeDetailModel?,
            index: Int = 0
        ) {
            if (homeDetailBean != null) {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putExtra(homeDetailModel, homeDetailBean)
                intent.putExtra(songindex, index)
                context.startActivity(intent)
            }
        }

        fun startActivity(context: Context, fromGlobal: String) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            intent.putExtra(BookPlayerActivity.fromGlobal, fromGlobal)
            context.startActivity(intent)
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

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        setStatusBar(R.color.businessWhite)
        mViewModel.addBubbleFLViewModel(bubbleFl)
        mViewModel.initPlayerAdapterModel()
        mDataBind.rvMusicPlay.bindVerticalLayout(mBookPlayerAdapter)
    }

    override fun startObserve() {
        mViewModel.playPath.observe(this, Observer {
            it?.let {
                musicPlayerManger.updateMusicPlayerData(it, indexSong)
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                if (fromGlobalValue.orEmpty().isEmpty()) {
                    musicPlayerManger.startPlayMusic(indexSong)
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
                                showMusicPlaySpeedDialog {
                                    musicPlayerManger.setPlayerMultiple(it)
                                }
                            }
                        }
                    }
                    ACTION_GET_PLAYINFO_LIST -> {
                        navigateToForResult(ARouterPath.testPath, 100)
                    }
                    ACTION_JOIN_LISTEN -> {
                        homeDetailBean?.let {
                            RouterHelper.createRouter(ListenService::class.java)
                                .showMySheetListDialog(
                                    mViewModel,
                                    this@BookPlayerActivity,
                                    it.detaillist.audio_id.toString()
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
    }


    private fun getIntentParams() {
        //正在播放的对象
        homeDetailBean = intent.getSerializableExtra(homeDetailModel) as? HomeDetailModel
        //来自全局小圆圈点击进来
        fromGlobalValue = intent.getStringExtra(fromGlobal)

        if (fromGlobalValue.orEmpty().isNotEmpty()) {
            homeDetailBean = homeDetailModel.getObjectMMKV(HomeDetailModel::class.java)
        }
        homeDetailBean?.let {
            homeDetailModel.putMMKV(it)
            val listValue = mViewModel.mutableList.value
            listValue?.set(0, PlayControlModel(homeDetailModel = it))
            mViewModel.audioID.set(it.detaillist.audio_id)
            mViewModel.chapterList(
                it.detaillist.audio_id,
                1,
                20,
                "asc",
                homeDetailBean?.detaillist?.anchor?.anchor_avatar ?: ""
            )
            mViewModel.commentAudioComments(it.detaillist.audio_id)
            mViewModel.mutableList.postValue(listValue)
            mBookPlayerAdapter.notifyDataSetChanged()
            mViewModel.setHistoryPlayBook(it)
        }
        indexSong = intent.getIntExtra(songindex, 0)

    }

    override fun initData() {
        if (isServiceWork(musicPlayerManger.getServiceName())) {
        }
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
        val playerControl = mViewModel.playControlModel.get()
        playerControl?.baseAudioInfo = musicInfo
        mViewModel.playControlModel.set(playerControl)
        mViewModel.playControlModel.notifyChange()
        mViewModel.playReport(
            playerControl?.baseAudioInfo?.audioId ?: "",
            playerControl?.baseAudioInfo?.chapterId ?: ""
        )
        mViewModel.lastState.set(mViewModel.pathList.size>0&&position>0)
        mViewModel.updatePlayBook(mViewModel.audioChapterModel.get()?.chapter_list?.getOrNull(position))
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        val str =
            "${formatTimeInMillisToString(currentDurtion)}/${formatTimeInMillisToString(totalDurtion)}"
        mViewModel.updateThumbText.set(str)
        bubbleFl.text = str
        mViewModel.process.set(currentDurtion.toFloat())
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        mViewModel.playSate.set(playbackState)

    }

    /**
     * 判断服务是否正在运行
     */
    fun isServiceWork(
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