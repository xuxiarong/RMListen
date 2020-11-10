package com.rm.module_play.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.BaseAudioModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.binding.bindChapterList
import com.rm.business_lib.binding.bindDateString
import com.rm.business_lib.binding.bindDuration
import com.rm.business_lib.binding.bindPlayCount
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.wedgit.LivingView
import com.rm.business_lib.wedgit.download.DownloadStatusView
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.R
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
    downloadAudio : BaseAudioModel?,
    audioListModel: AudioChapterListModel,
    back: (chapterId: String) -> Unit,
    mLoad: (types: Int) -> Unit,
    isPlay : Boolean
) {
    MusicPlayBookListDialog().apply {
        this.audioChapterListModel = audioListModel
        this.mBack = back
        this.mLoad = mLoad
        this.downloadAudio = downloadAudio
        this.isPlay = isPlay
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayBookListDialog : BottomDialogFragment() {
    var mBack: (chapterId: String) -> Unit = {}
    var mLoad: (types: Int) -> Unit = {}
    var audioChapterListModel: AudioChapterListModel? = null
    var downloadAudio: BaseAudioModel? = null
    var isPlay : Boolean =false

    private val timeSAdapter by lazy {
        TimeSAdapter(downloadAudio,audioChapterListModel?.list as MutableList<ChapterList>,isPlay).apply {
            setOnItemClickListener { adapter, view, position ->
                mBack(data[position].chapter_id)
                dismissAllowingStateLoss()
            }
        }
    }

    companion object{
        var musicDialog : MusicPlayBookListDialog? = null
        var isShowing = false
    }

    fun notifyDialog(){
        if(musicDialog!=null){
            timeSAdapter.notifyDataSetChanged()
        }
    }

    override fun getBackgroundAlpha() = 0f

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_book_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isShowing = true
        musicDialog = this@MusicPlayBookListDialog
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_book_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_book_list.adapter = timeSAdapter
        setPlayModel()
        music_play_order_play.setOnClickListener {
            if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_SINGLE) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_ORDER)
            } else if (musicPlayerManger.getPlayerModel() == MUSIC_MODEL_ORDER) {
                musicPlayerManger.setPlayerModel(MUSIC_MODEL_SINGLE)
            }
            setPlayModel()
        }
        music_play_download_de.setOnClickListener {
            val downloadService = RouterHelper.createRouter(DownloadService::class.java)
            if(activity!=null && !activity!!.isFinishing && downloadAudio!=null){
                val audio = DownloadAudio()
                audio.audio_name = downloadAudio!!.audio_name
                audio.audio_id = downloadAudio!!.audio_id.toLong()
                audio.author = downloadAudio!!.author
                audio.audio_cover_url = downloadAudio!!.audio_cover_url
                audio.last_sequence = downloadAudio!!.last_sequence.toInt()
                downloadService.startDownloadChapterSelectionActivity(context = context!!,downloadAudio = audio)
            }
        }
        play_drag_chapter_list.seDragCloseListener {
            dismiss()
            isShowing = false
            musicDialog = null
        }

        cb_sort_songs_list.setOnClickListener {
            timeSAdapter.data.reverse()
            timeSAdapter.notifyDataSetChanged()
        }


        smart_refresh_layout_play.setOnRefreshListener {
            smart_refresh_layout_play.finishRefresh(1500)
            mLoad(0)
        }
        smart_refresh_layout_play.setOnLoadMoreListener {
            smart_refresh_layout_play.finishLoadMore(1500)
            mLoad(1)
        }
    }


    internal class TimeSAdapter(var downloadAudio : BaseAudioModel?, list: MutableList<ChapterList>, var isPlay: Boolean) :
        BaseQuickAdapter<ChapterList, BaseViewHolder>(R.layout.music_play_item_book_list, list) {
        override fun convert(holder: BaseViewHolder, item: ChapterList) {
            val playChapter=item.chapter_id== musicPlayerManger.getCurrentPlayerMusic()?.chapterId
            holder.setVisible(R.id.music_play_book_list_position,!playChapter)
            if(playChapter){
                if(isPlay){
                    holder.getView<LivingView>(R.id.living_img).startAnim()
                }else{
                    holder.getView<LivingView>(R.id.living_img).pauseAnim()
                }
            }else{
                holder.getView<LivingView>(R.id.living_img).visibility = View.GONE
            }
            holder.setText(R.id.music_play_book_list_position, "${holder.layoutPosition + 1}")
            holder.setText(R.id.tv_music_play_chapter_title, item.chapter_name+"sadadasdasdasdaskjdskljdfjkldfjkldfsjkladfsjkldsjkl")
            holder.getView<TextView>(R.id.tv_music_play_count).bindPlayCount(item.play_count)
            holder.getView<TextView>(R.id.tv_music_play_time_count).bindDuration(item.duration)
            holder.getView<TextView>(R.id.tv_music_play_up_time).bindDateString(item.created_at)
            val downloadStatusView = holder.getView<DownloadStatusView>(R.id.image_music_play_down)
            downloadStatusView.bindChapterList(
                downloadAudio,
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