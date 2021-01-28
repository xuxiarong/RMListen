package com.rm.business_lib.binding

import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

/**
 *
 * @author yuanfang
 * @date 1/7/21
 * @description
 *
 */
@BindingAdapter(
    "bindingCommentLottieAnimation",
    requireAll = false
)
fun LottieAnimationView.bindingCommentLottieAnimation(
    bindingLottieIsSelect: Boolean? = false
) {
    clearAnimation()
    if (bindingLottieIsSelect == true) {
        setAnimation("business_like.json")
    } else {
        setAnimation("business_unlike.json")
    }
}