package com.rm.module_play.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.R
import kotlinx.android.synthetic.main.music_play_dialog_more_setting.*

/**
 *
 * @des:
 * @data: 8/26/20 5:51 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayMoreDialog(back: (type: Int) -> Unit) {
    MusicPlayMoreDialogFragment().apply {
        mBack = back
    }.show(supportFragmentManager, "MusicPlayMoreDialogFragment")
}

class MusicPlayMoreDialogFragment : BottomDialogFragment() {
    var mBack: (type: Int) -> Unit = {}
    override fun onSetInflaterLayout(): Int = R.layout.music_play_dialog_more_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        music_play_times.setOnClickListener {
            mBack(0)
            dismiss()
        }
        music_play_speed.setOnClickListener {
            mBack(1)
            dismiss()
        }
    }

}