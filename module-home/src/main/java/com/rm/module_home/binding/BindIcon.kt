package com.rm.module_home.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.module_home.R

@BindingAdapter("isSrc")
fun ImageView.isSrc(isLiked: Boolean){
    if(isLiked){
        setImageResource(R.drawable.home_icon_play_ed)
    }else{
        setImageResource(R.drawable.business_icon_unlike)
    }
}

@BindingAdapter("bindMenu")
fun ImageView.bindMenu(menuName : String){
    when (menuName) {
        context.getString(R.string.home_boutique) -> {
            setImageResource(R.drawable.home_selector_menu_boutique)
        }
        context.getString(R.string.home_rank) -> {
            setImageResource(R.drawable.home_selector_menu_rank)
        }
        context.getString(R.string.home_listen_list) -> {
            setImageResource(R.drawable.home_selector_menu_listen)
        }
    }
}