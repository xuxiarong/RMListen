package com.rm.component_comm.bind

import android.view.View
import androidx.databinding.BindingAdapter
import com.rm.component_comm.utils.BannerJumpUtils

/**
 * desc   :
 * date   : 2020/12/08
 * version: 1.0
 */

@BindingAdapter("bindJumpUrl")
fun View.bindJumpUrl(jumpUrl : String?){
    jumpUrl?.let {
        setOnClickListener {
            BannerJumpUtils.onBannerClick(context = context,url = jumpUrl)
        }
    }
}