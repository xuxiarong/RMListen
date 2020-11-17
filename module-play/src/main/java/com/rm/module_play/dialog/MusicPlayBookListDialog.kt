package com.rm.module_play.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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
import kotlinx.android.synthetic.main.music_play_dialog_book_list.*

/**
 *
 * @des:书单
 * @data: 8/27/20 11:19 AM
 * @Version: 1.0.0
 */
fun FragmentActivity.showPlayBookListDialog(
    viewModel : PlayViewModel
) {
    MusicPlayBookListDialog().apply {
        this.viewModel = viewModel
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayBookListDialog : BottomDialogFragment() {
    lateinit var viewModel : PlayViewModel
    lateinit var mDataBind: ViewDataBinding
    private val chapterAdapter by lazy {
        TimeSAdapter(viewModel).apply {
            setOnItemClickListener { adapter, view, position ->
                val chapterId = data[position].chapter_id
                if(chapterId == viewModel.playManger.getCurrentPlayerID()){
                    viewModel.playManger.play()
                }else{
                    viewModel.playManger.startPlayMusic(data[position].chapter_id.toString())
                }
                dismissAllowingStateLoss()
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
        mDataBind.setVariable(BR.viewModel,viewModel)
        mDataBind.apply {
            lifecycleOwner = this@MusicPlayBookListDialog
        }
        return mDataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_book_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_book_list.adapter = chapterAdapter
        setPlayModel()
        music_play_order_play.setOnClickListener {
            if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_SINGLE) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_ORDER)
            } else if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_ORDER) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_SINGLE)
            }
            setPlayModel()
        }

        play_drag_chapter_list.seDragCloseListener {
            dismiss()
        }
        music_play_download_de.setOnClickListener {
            RouterHelper.createRouter(DownloadService::class.java).startDownloadChapterSelectionActivity(activity!!,viewModel.playAudioModel.get()!!)
        }

        cb_sort_songs_list.setOnClickListener {
            chapterAdapter.data.reverse()
            chapterAdapter.notifyDataSetChanged()
        }


//        smart_refresh_layout_play.setOnRefreshListener {
//            smart_refresh_layout_play.finishRefresh(1500)
//            mLoad(0)
//        }
//        smart_refresh_layout_play.setOnLoadMoreListener {
//            smart_refresh_layout_play.finishLoadMore(1500)
//            mLoad(1)
//        }
        startObserve()
    }

    fun startObserve(){
        DownloadMemoryCache.downloadingChapter.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                chapterAdapter.notifyDataSetChanged()
            }
        })
        viewModel.playChapterList.observe(viewLifecycleOwner, Observer {
            chapterAdapter.setList(it)
        })
    }

    internal class TimeSAdapter(val viewModel: PlayViewModel) :
        BaseQuickAdapter<DownloadChapter, BaseViewHolder>(R.layout.music_play_item_book_list, viewModel.playChapterList.value) {
        override fun convert(holder: BaseViewHolder, item: DownloadChapter) {
            val playChapter=item.chapter_id== musicPlayerManger.getCurrentPlayerMusic()?.chapterId?.toLong()
            holder.setVisible(R.id.music_play_book_list_position,!playChapter)
            if(playChapter){
                if(viewModel.playStatusBean.get()!!.isStart()){
                    holder.getView<LivingView>(R.id.living_img).startAnim()
                }else{
                    holder.getView<LivingView>(R.id.living_img).pauseAnim()
                }
            }else{
                holder.getView<LivingView>(R.id.living_img).visibility = View.GONE
            }
            holder.setText(R.id.music_play_book_list_position, "${holder.layoutPosition + 1}")
            holder.setText(R.id.tv_music_play_chapter_title, item.chapter_name)
            holder.getView<TextView>(R.id.tv_music_play_count).bindPlayCountString(item.play_count)
            holder.getView<TextView>(R.id.tv_music_play_time_count).bindDuration(item.duration)
            holder.getView<TextView>(R.id.tv_music_play_up_time).bindDateString(item.created_at)
            val downloadStatusView = holder.getView<DownloadStatusView>(R.id.image_music_play_down)
            downloadStatusView.bindChapterList(
                viewModel.playAudioModel.get(),
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
        var res = 0
        if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_SINGLE) {
            res = R.drawable.music_play_ic_icon_single_de
            music_play_order_play.text = "单集播放"
        }else if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_ORDER) {
            res = R.drawable.business_play_mode_order
            music_play_order_play.text = "顺序播放"
        }
        val resDrawable = resources.getDrawable(res, null)
        resDrawable.setBounds(0, 0, resDrawable.minimumWidth, resDrawable.minimumHeight)
        music_play_order_play.setCompoundDrawables(resDrawable, null, null, null)

    }

}