package com.rm.module_play.dialog

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.R
import kotlinx.android.synthetic.main.music_play_dialog_time_setting.*

/**
 *
 * @des:播放器定时器
 * @data: 8/26/20 6:46 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayTimeSettingDialog() {
    MusicPlayTimeSettingDialog().apply {

    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayTimeSettingDialog : BottomDialogFragment() {
    private val timeSAdapter by lazy {
        TimeSAdapter(timeList).apply {

            setOnItemClickListener { _, _, position ->
                notifyChange(position)
            }
        }
    }
    private val timeList by lazy {
        //占位 add


        arrayListOf<String>().apply {
            for (index in 30 downTo 1){
                add("${index}分钟")
            }
        }
    }

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_time_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_time_setting.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_time_setting.adapter = timeSAdapter

    }


    internal class TimeSAdapter(list: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_times_page, list) {
        var isCheckPos = -1

        override fun convert(holder: BaseViewHolder, item: String) {
            holder.getView<ImageView>(R.id.music_play_time_setting_check).background =
                if (isCheckPos == holder.layoutPosition) ContextCompat.getDrawable(
                    context,
                    R.drawable.music_play_item_time_selected_ic_icon_chosen_da
                ) else ContextCompat.getDrawable(
                    context,
                    R.drawable.music_play_item_ic_icon_select_df
                )
        }

        /**
         * 刷新数据
         */
        fun notifyChange(isCheckPos: Int) {
            notifyItemChanged(this.isCheckPos)
            notifyItemChanged(isCheckPos)
            this.isCheckPos = isCheckPos


        }
    }

}