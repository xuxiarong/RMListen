package com.rm.module_play.activity

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
import com.rm.business_lib.bean.AudioChapterListModel
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
import com.scwang.smart.refresh.layout.SmartRefreshLayout

@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class BookPlayerActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>(),
    MusicPlayerEventListener {

    private val mBookPlayerAdapter: BookPlayerAdapter by lazy {
        BookPlayerAdapter(mViewModel, BR.viewModel, BR.itemModel)

    }
    var homeDetailBean: HomeDetailModel? = null
    var mAudioChapterListModel: AudioChapterListModel? = null
    companion object {
        val homeDetailModel = "homeDetailModel"
        val audioChapterListModel = "AudioChapterListModel"
        fun startActivity(
            context: Context,
            homeDetailBean: HomeDetailModel?,
            detailModel: AudioChapterListModel?
        ) {
            if (homeDetailBean != null && detailModel != null) {
                val intent = Intent(context, BookPlayerActivity::class.java)
                intent.putExtra(homeDetailModel, homeDetailBean)
                intent.putExtra(audioChapterListModel, detailModel)
                context.startActivity(intent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                })
            }

        }

        fun startActivity(context: Context) {
            val intent = Intent(context, BookPlayerActivity::class.java)
            context.startActivity(intent.apply {
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

    override fun getLayoutId(): Int = R.layout.activity_book_player

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        setStatusBar(R.color.businessWhite)
        mViewModel.addBubbleFLViewModel(bubbleFl)

    }


    override fun startObserve() {
        mViewModel.playPath.observe(this, Observer {
            it?.let {
                musicPlayerManger.updateMusicPlayerData(it, 2)
                musicPlayerManger.addOnPlayerEventListener(this@BookPlayerActivity)
                GlobalplayHelp.instance.addOnPlayerEventListener()
                musicPlayerManger.playOrPause()
            }
        })
        mViewModel.playControlAction.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when (mViewModel.playControlAction.get()) {
                    ACTION_PLAY_QUEUE -> {
                        //调整播放列表
                        mAudioChapterListModel?.let { it ->
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


    override fun initData() {
        mViewModel.initPlayerAdapterModel()
        mDataBind.rvMusicPlay.bindVerticalLayout(mBookPlayerAdapter)
        homeDetailBean = intent.getSerializableExtra(homeDetailModel) as? HomeDetailModel
        mAudioChapterListModel =
            intent.getSerializableExtra(audioChapterListModel) as? AudioChapterListModel
        mAudioChapterListModel?.let {
            mViewModel.zipPlayPath(it)
        }
        homeDetailBean?.let {
            val listValue = mViewModel.mutableList.value
            listValue?.set(0, PlayControlModel(homeDetailModel = it))
            mViewModel.audioID.set(it.detaillist.audio_id.toString())
            mViewModel.commentAudioComments("${it.detaillist.audio_id}")
            mViewModel.mutableList.postValue(listValue)
            mBookPlayerAdapter.notifyDataSetChanged()
        }

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
        mViewModel.playReport(playerControl?.baseAudioInfo?.audioId?:"",playerControl?.baseAudioInfo?.chapterId?:"")

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
    }
}