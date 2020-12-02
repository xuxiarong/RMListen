package com.rm.module_play.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.base.dialogfragment.SuperBottomSheetDialogFragment
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.music_play_dialog_speed_setting.*

/**
 *
 * @des:倍速度设置
 * @data: 8/27/20 11:03 AM
 * @Version: 1.0.0
 */

fun FragmentActivity.showMusicPlaySpeedDialog(viewModel: PlayViewModel) {
    MusicPlaySpeedDialog().apply {
        this.viewModel = viewModel
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

//速度集合
internal val timeSet by lazy {
    mapOf(
        "0.5X" to 0.5f,
        "0.75X" to 0.75f,
        "正常" to 1f,
        "1.25X" to 1.25f,
        "1.5X" to 1.5f,
        "2X" to 2f
    )
}

class MusicPlaySpeedDialog : SuperBottomSheetDialogFragment() {

    lateinit var viewModel: PlayViewModel
    lateinit var mDataBind: ViewDataBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mDataBind = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        mDataBind.setVariable(BR.viewModel, viewModel)
        mDataBind.apply {
            lifecycleOwner = this@MusicPlaySpeedDialog
        }
        return mDataBind.root
    }

    private val timeSAdapter by lazy {
        val timeList = mutableListOf<String>()
        timeSet.keys.forEach {
            timeList.add(it)
        }
        TimeSAdapter(timeList).apply {
            setOnItemClickListener { _, _, position ->
                timeSet[data[position]]?.let {
                    musicPlayerManger.setPlayerMultiple(it)
                    SAVA_SPEED.putMMKV(it)
                    PlayGlobalData.playSpeed.set(it)
                    dismissAllowingStateLoss()

                }
                notifyDataSetChanged()

            }
        }

    }

    override fun getLayoutResId(): Int = R.layout.music_play_dialog_speed_setting

    override fun onInitialize() {
        rv_music_play_time_setting.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_play_time_setting.adapter = timeSAdapter
    }


    internal class TimeSAdapter(list: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item_speed_setting, list) {
        override fun convert(holder: BaseViewHolder, item: String) {
            val isCheckPos = PlayGlobalData.playSpeed.get()
            holder.getView<ImageView>(R.id.music_play_speed_setting_check).background =
                if (isCheckPos == timeSet[item]) ContextCompat.getDrawable(
                    context,
                    R.drawable.play_timer_item_position_select
                ) else ContextCompat.getDrawable(
                    context,
                    R.drawable.play_timer_item_position_unselect
                )
            holder.setText(R.id.tv_music_play_setting_time, item)
        }
    }

}