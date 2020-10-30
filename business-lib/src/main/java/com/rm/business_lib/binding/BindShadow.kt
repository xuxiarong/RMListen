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
@BindingAdapter(
    "bindShapeType",
    "bindShadowType",
    "bindShapeColor",
    "bindShadowColor",
    "bindShapeRadius",
    "bindShadowRadius",
    requireAll = false
)
fun View.bindShadow(
    bindShapeType: Int?,
    bindShadowType: ShadowDrawableUtil.TypeEnum?,
    bindShapeColor: Int?,
    bindShadowColor: Int?,
    bindShapeRadius: Int?,
    bindShadowRadius: Int?
) {
    ShadowDrawableUtil.setShadowDrawable(
        this,
        bindShapeType ?: ShadowDrawableUtil.SHAPE_ROUND_PART,
        bindShadowType ?: ShadowDrawableUtil.TypeEnum.TOP,
        bindShapeColor ?: Color(R.color.business_text_color_ffffff),
        bindShapeRadius ?: 24,
        bindShadowColor ?: Color(R.color.business_text_color_999999),
        bindShadowRadius ?: 10
    )

}