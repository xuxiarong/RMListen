package com.rm.module_play.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.util.DLog
import com.rm.business_lib.play.PlayState
import com.rm.music_exoplayer_lib.constants.STATE_ENDED

/**
 * desc   :
 * date   : 2020/11/03
 * version: 1.0
 */
class PlayControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LottieAnimationView(context, attrs, defStyleAttr){

    var playAnimName = ""
    var playFinish = true

    fun startOrPause(playState: PlayState){
        //代表播放列表已经播放完
        if(playState.state == STATE_ENDED){
            setAnimation("play_pause.json")
            playAnimation()
            return
        }

        if(isAnimating ){
            DLog.d("suolongg","z正在动画中，退出")
            return
        }

        if (!playState.read &&  playState.state == 3 && playFinish && (playAnimName == "play_start.json"||playAnimName == "")) {
            setAnimation("play_pause.json")
            addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                    playAnimName = "play_pause.json"
                    playFinish = false
                    DLog.d("suolong","play_pause onAnimationRepeat")
                }

                override fun onAnimationEnd(animation: Animator?) {
                    playFinish = true
                    playAnimName = "play_pause.json"
                    DLog.d("suolong","play_pause onAnimationEnd")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    playAnimName = "play_pause.json"
                    playFinish = true
                    DLog.d("suolong","play_pause onAnimationCancel")

                }

                override fun onAnimationStart(animation: Animator?) {
                    playFinish = false
                    playAnimName = "play_pause.json"
                    DLog.d("suolong","play_pause onAnimationStart")
                }
            })
            playAnimation()

        } else if(playState.read && playState.state == 3 && playFinish && playAnimName == "play_pause.json"||playAnimName == "") {
            setAnimation("play_start.json")
            addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                    playAnimName = "play_start.json"
                    playFinish = false
                    DLog.d("suolong","play_start onAnimationRepeat")
                }

                override fun onAnimationEnd(animation: Animator?) {
                    playAnimName = "play_start.json"
                    playFinish = true
                    DLog.d("suolong","play_start onAnimationEnd")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    playAnimName = "play_start.json"
                    playFinish = true
                    DLog.d("suolong","play_start onAnimationCancel")
                }

                override fun onAnimationStart(animation: Animator?) {
                    playFinish = false
                    DLog.d("suolong","play_start onAnimationStart")
                }
            })
            playAnimation()
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playAnimName = ""
        playFinish = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playAnimName = ""
        playFinish = true
    }

}