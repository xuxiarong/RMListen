package com.rm.module_play.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.util.getFloattMMKV
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.binding.bindPlaySpeedSrc
import com.rm.module_play.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.music_play_dialog_more_setting.*

/**
 *
 * @des:
 * @data: 8/26/20 5:51 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayMoreDialog(viewModel: PlayViewModel) {
    MusicPlayMoreDialogFragment().apply {
        this.viewModel = viewModel
    }.show(supportFragmentManager, "MusicPlayMoreDialogFragment")
}

class MusicPlayMoreDialogFragment : BottomDialogFragment() {
    lateinit var viewModel: PlayViewModel
    lateinit var mDataBind: ViewDataBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mDataBind = DataBindingUtil.inflate(inflater, onSetInflaterLayout(), container, false)
        mDataBind.setVariable(BR.viewModel, viewModel)
        mDataBind.apply {
            lifecycleOwner = this@MusicPlayMoreDialogFragment
        }
        return mDataBind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        music_play_times.setOnClickListener {
            dismiss()
            activity?.let {
                viewModel.showPlayTimerDialog(it)
            }
        }
        music_play_speed.setOnClickListener {
            dismiss()
            activity?.let {
                viewModel.showPlaySpeedDialog(it)
            }
        }
        playSpeedSettingIv.bindPlaySpeedSrc(SAVA_SPEED.getFloattMMKV())

    }

    override fun getBackgroundAlpha() = 0f

    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_more_setting

}