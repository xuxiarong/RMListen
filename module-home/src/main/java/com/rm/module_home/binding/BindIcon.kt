package com.rm.module_home.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.module_home.R

@BindingAdapter("isSrc")
fun ImageView.isSrc(isLiked: Boolean){
    if(isLiked){
        setImageResource(R.drawable.home_icon_play_ed)
    }else{
        setImageResource(R.drawable.icon_detail_comment_like)
    }
}