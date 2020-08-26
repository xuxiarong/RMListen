package com.rm.module_play.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.rm.business_lib.base.dialogfragment.BottomDialogFragment
import com.rm.module_play.R

/**
 *
 * @des:
 * @data: 8/26/20 5:51 PM
 * @Version: 1.0.0
 */
fun FragmentActivity.showMusicPlayMoreDialog(){
    MusicPlayMoreDialogFragment().apply {

    }.show(supportFragmentManager,"MusicPlayMoreDialogFragment")
}

class MusicPlayMoreDialogFragment: BottomDialogFragment() {

    override fun onSetInflaterLayout(): Int= R.layout.music_play_dialog_more_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}