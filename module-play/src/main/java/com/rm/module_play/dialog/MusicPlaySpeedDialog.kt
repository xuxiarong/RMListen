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
import kotlinx.android.synthetic.main.music_play_dialog_speed_setting.*

/**
 *
 * @des:
 * @data: 8/27/20 11:03 AM
 * @Version: 1.0.0
 */

fun FragmentActivity.showMusicPlaySpeedDialog(back: (pos: Float) -> Unit) {
    MusicPlaySpeedDialog().apply {
        this.mBack = back
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlaySpeedDialog : BottomDialogFragment() {
    var mBack: (pos: Float) -> Unit = {}
    private val timeSAdapter by lazy {
        val timeList = mutableListOf<String>()
        tiemSet.keys.forEach {
            timeList.add(it)
        }
        TimeSAdapter(timeList).apply {
            setOnItemClickListener { _, _, position ->
                tiemSet[data[position]]?.let { mBack(it) }
                notifyChange(position)
            }
        }

    }
    private val tiemSet by lazy {
        //占位 add
        mapOf(
            "0.5X" to 0.5f,
            "0.75X" to 0.75f,
            "正常" to 1f,
            "1.25X" to 1.25f,
            "1.5X" to 1.5f,
            "2X" to 2f
        )
    }

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_speed_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_music_play_time_setting.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_time_setting.adapter = timeSAdapter

    }

    internal class TimeSAdapter(list: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_speed_setting, list) {
        var isCheckPos = -1
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.getView<ImageView>(R.id.music_play_speed_setting_check).background =
                if (isCheckPos == holder.layoutPosition) ContextCompat.getDrawable(
                    context,
                    R.drawable.music_play_item_time_selected_ic_icon_chosen_da
                ) else ContextCompat.getDrawable(
                    context,
                    R.drawable.music_play_item_ic_icon_select_df
                )
            holder.setText(R.id.tv_music_play_setting_time, item)
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