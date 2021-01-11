package com.rm.module_play.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.ktx.reverse
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_ORDER
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_SINGLE
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.music_play_dialog_book_list.*

/**
 *
 * @des:书单
 * @data: 8/27/20 11:19 AM
 * @Version: 1.0.0
 */
fun FragmentActivity.showPlayBookListDialog(
    viewModel: PlayViewModel
) {
    MusicPlayBookListDialog().apply {
        this.viewModel = viewModel
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayBookListDialog : BottomDialogFragment() {
    lateinit var viewModel: PlayViewModel
    private lateinit var mDataBind: ViewDataBinding
    private val chapterAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
                viewModel,
                mutableListOf(),
                R.layout.play_dialog_item_chapter,
                BR.viewModel,
                BR.item
        )
    }

    override fun getBackgroundAlpha() = 0f

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_book_list

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBind = DataBindingUtil.inflate(inflater, onSetInflaterLayout(), container, false)
        mDataBind.setVariable(BR.viewModel, viewModel)
        mDataBind.apply {
            lifecycleOwner = this@MusicPlayBookListDialog
        }
        return mDataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.isLocalChapterList.get()){
            rv_music_play_book_local_list.adapter = chapterAdapter
            rv_music_play_book_local_list.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }else{
            rv_music_play_book_list.adapter = chapterAdapter
            rv_music_play_book_list.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        //设置拖拽关闭的回调
        play_drag_chapter_list.seDragCloseListener {
            dismiss()
        }
        //设置下载按钮的点击事件
        play_iv_download.setOnClickListener {
            activity?.let { activity ->
                PlayGlobalData.playAudioModel.get()?.let { audio ->
                    RouterHelper.createRouter(DownloadService::class.java)
                        .startDownloadChapterSelectionActivity(activity, audio)
                }
            }

        }
        //设置播放模式
        setPlayModel()
        play_iv_play_mode.setOnClickListener {
            if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_SINGLE) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_ORDER)
            } else if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_ORDER) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_SINGLE)
            }
            setPlayModel()
        }
        if(PlayGlobalData.isSortAsc()){
            play_cb_chapter_sort.setImageResource(R.drawable.business_icon_sort_de)
        }else{
            play_cb_chapter_sort.setImageResource(R.drawable.business_icon_ortinverted_de)
        }
        play_cb_chapter_sort.setOnClickListener {
            if(PlayGlobalData.isSortAsc()){
                PlayGlobalData.playChapterListSort.set(AudioSortType.SORT_DESC)
                BaseConstance.updateBaseAudioSort(AudioSortType.SORT_DESC)
                play_cb_chapter_sort.setImageResource(R.drawable.business_icon_ortinverted_de)
            }else{
                PlayGlobalData.playChapterListSort.set(AudioSortType.SORT_ASC)
                BaseConstance.updateBaseAudioSort(AudioSortType.SORT_ASC)
                play_cb_chapter_sort.setImageResource(R.drawable.business_icon_sort_de)
            }
            PlayGlobalData.chapterRefreshModel.setCanRefresh(PlayGlobalData.chapterRefreshModel.canRefresh.get()?:true)
            PlayGlobalData.chapterRefreshModel.setResetNoMoreData(PlayGlobalData.chapterRefreshModel.noMoreData.get()?:false)
            PlayGlobalData.playChapterList.reverse()
        }
        //开启数据变化的监听
        startObserve()
    }

    private fun startObserve() {
        DownloadMemoryCache.downloadingChapterList.observe(viewLifecycleOwner, Observer {
            chapterAdapter.setList(getChapterStatus(chapterAdapter.data))
            chapterAdapter.notifyDataSetChanged()
        })
        DownloadMemoryCache.downloadingAudioList.observe(viewLifecycleOwner, Observer {
            chapterAdapter.setList(getChapterStatus(chapterAdapter.data))
            chapterAdapter.notifyDataSetChanged()
        })
        PlayGlobalData.playChapterList.observe(viewLifecycleOwner, Observer {
            chapterAdapter.setList(getChapterStatus(it))
        })
    }

    private fun getChapterStatus(chapterList: List<DownloadChapter>): MutableList<DownloadChapter> {
        val audioName = PlayGlobalData.playAudioModel.get()?.audio_name?:""
        val audioId = PlayGlobalData.playAudioModel.get()?.audio_id?:0L
        chapterList.forEach {
            it.audio_name = audioName
            it.audio_id = audioId
            DownLoadFileUtils.checkChapterIsDownload(chapter = it)
        }
        return chapterList.toMutableList()
    }

    /**
     * 设置直播模式
     */
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun setPlayModel() {
        var res = R.drawable.business_play_mode_order
        if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_SINGLE) {
            res = R.drawable.music_play_ic_icon_single_de
            play_iv_play_mode.text = "单集播放"
        } else if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_ORDER) {
            res = R.drawable.business_play_mode_order
            play_iv_play_mode.text = "顺序播放"
        }
        val resDrawable = resources.getDrawable(res, null)
        resDrawable.setBounds(0, 0, resDrawable.minimumWidth, resDrawable.minimumHeight)
        play_iv_play_mode.setCompoundDrawables(resDrawable, null, null, null)

    }
}