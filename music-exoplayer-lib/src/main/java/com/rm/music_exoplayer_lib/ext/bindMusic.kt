package com.rm.music_exoplayer_lib.ext

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.music_exoplayer_lib.R

/**
 * desc   :
 * date   : 2020/12/31
 * version: 1.0
 */

@BindingAdapter("bindMusicStatus")
fun ImageView.bindMusicStatus(statusModel: BasePlayStatusModel?){
    if(statusModel == null){
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE
    if(statusModel.isPause()){
        setImageResource(R.drawable.music_ic_pause_white)
    }else{
        setImageResource(R.drawable.music_ic_playing_white)
    }

}