package com.rm.business_lib.binding

import android.animation.Animator
import android.text.TextUtils
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
    "bindingLottieDefaultJson",
    "bindingLottieSelectJson",
    "bindingLottieIsSelect",
    "bindingLottieIsPlay",
    "bindingLottieEndBlock",
    requireAll = false
)
fun LottieAnimationView.bindingLottieAnimation(
    bindingLottieDefaultJson: String,
    bindingLottieSelectJson: String,
    bindingLottieIsSelect: Boolean? = false,
    bindingLottieIsPlay: Boolean? = false,
    bindingLottieEndBlock: ()->Unit? = {}
) {
    if (TextUtils.isEmpty(bindingLottieDefaultJson) || TextUtils.isEmpty(bindingLottieSelectJson)) {
        return
    }
    if (bindingLottieIsSelect == true) {
        setAnimation("business_like.json")
    } else {
        setAnimation("business_cancel_like.json")
    }
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            bindingLottieEndBlock()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    })
//    if (bindingLottieIsPlay == true) {
//        playAnimation()
//    }
}