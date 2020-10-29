package com.rm.business_lib.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.rm.baselisten.utilExt.Color
import com.rm.business_lib.R
import com.rm.business_lib.wedgit.ShadowDrawableUtil

/**
 *
 * @author yuanfang
 * @date 10/29/20
 * @description
 *
 */
@BindingAdapter("bindShadowColor","","","")
fun View.bindShadow(boolean: Boolean) {
    ShadowDrawableUtil.setShadowDrawable(
        this,
        ShadowDrawableUtil.SHAPE_ROUND_PART,
        ShadowDrawableUtil.TypeEnum.TOP,
        Color(R.color.business_text_color_ffffff),
        24,
        Color(R.color.business_text_color_999999),
        10
    )

}