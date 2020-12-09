package com.rm.business_lib.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.business_lib.bean.BusinessAdModel

/**
 * desc   :
 * date   : 2020/12/08
 * version: 1.0
 */
@BindingAdapter("bindBusinessAdModel")
fun ImageView.bindBusinessAdModel(model: BusinessAdModel?){
    if(model == null){
        visibility = View.GONE
    }else{
        visibility = View.VISIBLE
        loadRoundCornersImage(8.0f,this,model.image_path)
    }
}