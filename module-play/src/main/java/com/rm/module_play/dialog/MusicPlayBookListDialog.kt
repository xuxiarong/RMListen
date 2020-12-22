package com.rm.module_play.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.ktx.reverse
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.business_lib.AudioSortType
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.business_lib.binding.bindChapterList
import com.rm.business_lib.binding.bindDateString
import com.rm.business_lib.binding.bindDuration
import com.rm.business_lib.binding.bindPlayCountString
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.wedgit.LivingView
import com.rm.business_lib.wedgit.download.DownloadStatusView
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_ORDER
import com.rm.music_exoplayer_lib.constants.MUSIC_MODEL_SINGLE
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.tencent.bugly.proguard.ac
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
        TimeSAdapter(activity!! ,viewModel).apply {
            setOnItemClickListener { _, _, position ->
                val chapterId = data[position].chapter_id
                if (chapterId == viewModel.playManger.getCurrentPlayerID()) {
                    if(viewModel.playManger.isPlaying()){
                        viewModel.playManger.pause()
                    }else{
                        viewModel.playManger.play()
                    }
                } else {
                    PlayGlobalData.playNeedQueryChapterProgress.set(true)
                    viewModel.getChapterAd {
                        viewModel.playManger.startPlayMusic(data[position].chapter_id.toString())
                    }
                }
            }
        }
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
            play_cb_chapter_sort.setImageResource(R.drawable.home_detail_chapter_ort_ce)
        }else{
            play_cb_chapter_sort.setImageResource(R.drawable.home_detail_chapter_inverted_ce)
        }
        play_cb_chapter_sort.setOnClickListener {
            if(PlayGlobalData.isSortAsc()){
                PlayGlobalData.playChapterListSort.set(AudioSortType.SORT_DESC)
                viewModel.chapterRefreshModel.setResetNoMoreData(viewModel.chapterRefreshModel.canRefresh.get()?:false)
                PlayGlobalData.playChapterList.reverse()
                play_cb_chapter_sort.setImageResource(R.drawable.home_detail_chapter_inverted_ce)

            }else{
                PlayGlobalData.playChapterListSort.set(AudioSortType.SORT_ASC)
                viewModel.chapterRefreshModel.setResetNoMoreData(viewModel.chapterRefreshModel.noMoreData.get()?:false)
                PlayGlobalData.playChapterList.reverse()
                play_cb_chapter_sort.setImageResource(R.drawable.home_detail_chapter_ort_ce)
            }
        }
        //开启数据变化的监听
        startObserve()
    }

    private fun startObserve() {
        DownloadMemoryCache.downloadingChapter.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                chapterAdapter.notifyDataSetChanged()
            }
        })
        PlayGlobalData.playChapterList.observe(viewLifecycleOwner, Observer {
            chapterAdapter.setList(it)
        })
        BaseConstance.basePlayStatusModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                BaseConstance.basePlayStatusModel.get()?.let {
                    if(!it.isBuffering()){
                        chapterAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }
    internal class TimeSAdapter(val activity: FragmentActivity?,val viewModel: PlayViewModel) :
        BaseQuickAdapter<DownloadChapter, BaseViewHolder>(
            R.layout.music_play_item_book_list,
                PlayGlobalData.playChapterList.value
        ) {
        override fun convert(holder: BaseViewHolder, item: DownloadChapter) {
            val playChapter =
                item.chapter_id == musicPlayerManger.getCurrentPlayerMusic()?.chapterId?.toLong()
            val playIcon = holder.getView<AppCompatImageView>(R.id.living_img)
            if (playChapter) {
                BaseConstance.basePlayStatusModel.get()?.let {
                    playIcon.visibility = View.VISIBLE
                    if (it.isStart()) {
                        playIcon.setImageResource(R.drawable.business_ic_playing)
                    } else {
                        playIcon.setImageResource(R.drawable.business_ic_play_pause)
                    }
                }
            } else {
                playIcon.visibility = View.GONE
            }
            holder.setText(R.id.tv_music_play_chapter_title, item.chapter_name)
            holder.getView<TextView>(R.id.tv_music_play_count).bindPlayCountString(item.play_count)
            holder.getView<TextView>(R.id.tv_music_play_time_count).bindDuration(item.realDuration)
            holder.getView<TextView>(R.id.tv_music_play_up_time).bindDateString(item.created_at)
            val downloadStatusView = holder.getView<DownloadStatusView>(R.id.image_music_play_down)
            downloadStatusView.bindChapterList(
                PlayGlobalData.playAudioModel.get(),
                item,
                DownloadMemoryCache.downloadingChapter.get()
            )
        }
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

    override fun onDestroy() {
        super.onDestroy()
        mDataBind.apply {
            lifecycleOwner = this@MusicPlayBookListDialog
        }
    }

}