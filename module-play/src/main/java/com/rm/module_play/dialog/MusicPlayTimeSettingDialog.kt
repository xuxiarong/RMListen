package com.rm.module_play.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.base.dialogfragment.SuperBottomSheetDialogFragment
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.model.PlayCountTimerModel
import com.rm.module_play.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.music_play_dialog_time_setting.*


/**
 *
 * @des:播放器定时器
 * @data: 8/26/20 6:46 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayTimeSettingDialog(viewModel: PlayViewModel) {
    MusicPlayTimeSettingDialog().apply {
        this.viewModel = viewModel
    }.show(supportFragmentManager, "MusicPlayTimeSettingDialog")
}

class MusicPlayTimeSettingDialog : SuperBottomSheetDialogFragment() {

    lateinit var viewModel: PlayViewModel
    lateinit var mDataBind: ViewDataBinding
    private val timerData = arrayListOf<PlayCountTimerModel>()
    private val timeAdapter by lazy {
        CommonBindVMAdapter(
                viewModel = viewModel,
                commonData = timerData,
                commonItemLayoutId = R.layout.rv_item_times_page,
                commonDataBrId = BR.item,
                commonViewModelId = BR.viewModel
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mDataBind = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        mDataBind.setVariable(BR.viewModel, viewModel)
        mDataBind.apply { lifecycleOwner = this@MusicPlayTimeSettingDialog }
        return mDataBind.root
    }


    override fun getLayoutResId(): Int = R.layout.music_play_dialog_time_setting
    override fun onInitialize() {
        for (i in 0 until PlayGlobalData.playCountTimerList.size){
            timerData.add(PlayCountTimerModel(i))
        }
        play_timer_count_down_rv.bindVerticalLayout(timeAdapter)
        timeAdapter.setOnItemClickListener  { _, _, position ->
            dismissAllowingStateLoss()
            PlayGlobalData.setCountDownTimer(position)
        }
        play_dialog_close_timer_fl.setOnClickListener {
            dismissAllowingStateLoss()
            PlayGlobalData.clearCountDownTimer()
        }
    }


}