package com.rm.module_play.dialog

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.R
import com.rm.music_exoplayer_lib.constants.MUSIC_ALARM_MODEL_0
import com.rm.music_exoplayer_lib.constants.SP_KEY_ALARM_MODEL
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.CacheUtils
import kotlinx.android.synthetic.main.music_play_dialog_time_setting.*

/**
 *
 * @des:播放器定时器
 * @data: 8/26/20 6:46 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayTimeSettingDialog() {
    MusicPlayTimeSettingDialog().show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayTimeSettingDialog : BottomDialogFragment() {
    private val timeSAdapter by lazy {
        TimeSAdapter(timeList).apply {
            setOnItemClickListener { _, _, position ->
                val itemPos = (data.getOrNull(position) ?: "0").toInt()
                notifyChange(itemPos)
                musicPlayerManger.setPlayerAlarmModel(itemPos)

            }
        }
    }
    private val itemDecoration by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if ((view.layoutParams as? RecyclerView.LayoutParams)?.viewLayoutPosition == 5) {
                    outRect.set(dip(0), dip(1), dip(0), dip(0))
                }
            }
        }

    }
    private val timeList by lazy {
        //占位 add
        mutableListOf("10", "20", "30", "40", "60", "1", "2", "3", "4", "5")
    }

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_time_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_time_setting.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_time_setting.adapter = timeSAdapter
        rv_music_play_time_setting.addItemDecoration(itemDecoration)

    }

    /**
     * 倒计时adpeter
     */
    internal class TimeSAdapter(list: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_times_page, list) {
        var isCheckPos = CacheUtils.instance.getInt(SP_KEY_ALARM_MODEL, MUSIC_ALARM_MODEL_0)
        override fun convert(holder: BaseViewHolder, item: String) {
            val str = if (item.toInt() >= 10) "${item}分钟" else "${item}集"
            holder.setText(R.id.tv_music_play_time_play, str)
            holder.getView<ImageView>(R.id.music_play_time_setting_check).background =
                if (item == isCheckPos.toString()) {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.music_play_item_time_selected_ic_icon_chosen_da
                    )
                } else {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.music_play_item_ic_icon_select_df
                    )
                }
            holder.setVisible(R.id.tv_music_play_time_down, isCheckPos.toString() == item)

        }

        /**
         * 刷新数据
         */
        fun notifyChange(isCheckPos: Int) {
            this.isCheckPos = isCheckPos
            notifyDataSetChanged()
        }
    }

}