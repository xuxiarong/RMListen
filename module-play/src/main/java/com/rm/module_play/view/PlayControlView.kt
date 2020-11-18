package com.rm.module_play.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.model.BasePlayStatusModel
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
    var action: (() -> Unit)? = {}
    private val pauseListener by lazy {
        PauseAnimatorListener()
    }
    private val startListener by lazy {
        StartAnimatorListener()
    }

    fun startOrPause(playState: BasePlayStatusModel,action: (() -> Unit)?){
        this.action = action
        //代表播放列表已经播放完
        if(playState.playStatus == STATE_ENDED){
            setAnimation("play_pause.json")
            playAnimation()
            return
        }

        if(isAnimating ){
            DLog.d("suolongg","z正在动画中，退出")
            return
        }

        if (!playState.playReady &&  playState.playStatus == 3 && playFinish && (playAnimName == "play_start.json"||playAnimName == "")) {
            setAnimation("play_pause.json")
            addAnimatorListener(pauseListener)
            playAnimName = "play_pause.json"
            playAnimation()
            DLog.d("suolongg","播放暂停动画")

        } else if(playState.playReady && playState.playStatus == 3 && playFinish && playAnimName == "play_pause.json"||playAnimName == "") {
            setAnimation("play_start.json")
            addAnimatorListener(startListener)
            playAnimName = "play_start.json"
            DLog.d("suolongg","播放开始动画")
            playAnimation()
        }
    }

    private fun setClickFun(action: (() -> Unit)?){
        postDelayed({
            playFinish = true
            if(action!=null){
                setOnClickListener {
                    action()
                }
            }
        },50)
    }

    fun clearClick(){
        setOnClickListener {
            DLog.d("suolong","正在动画中，不执行点击事件")
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

    inner class PauseAnimatorListener : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
            clearClick()
            DLog.d("suolong","play_pause onAnimationRepeat")
        }

        override fun onAnimationEnd(animation: Animator?) {
            setClickFun(action = action)
            DLog.d("suolong","play_pause onAnimationEnd")
        }

        override fun onAnimationCancel(animation: Animator?) {
            setClickFun(action = action)
            DLog.d("suolong","play_pause onAnimationCancel")

        }

        override fun onAnimationStart(animation: Animator?) {
            clearClick()
            DLog.d("suolong","play_pause onAnimationStart")
        }
    }

    inner class StartAnimatorListener : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
            clearClick()
        }

        override fun onAnimationEnd(animation: Animator?) {
            setClickFun(action = action)
            DLog.d("suolong","play_start onAnimationEnd")
        }

        override fun onAnimationCancel(animation: Animator?) {
            setClickFun(action = action)
        }

        override fun onAnimationStart(animation: Animator?) {
            clearClick()
            DLog.d("suolong","play_start onAnimationStart")
        }
    }
}