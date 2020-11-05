package com.rm.module_play.binding

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.play.PlayState
import com.rm.baselisten.view.progressbar.CircularProgressView
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.module_play.view.PlayControlView
import com.rm.music_exoplayer_lib.constants.STATE_BUFFERING
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager


/**
 *
 * @des:
 * @data: 9/10/20 5:44 PM
 * @Version: 1.0.0
 */

@BindingAdapter("progressChangedListener")
fun BubbleSeekBar.progressChangedListener(action : (String)->Unit) {
    setOnProgressChangedListener(object :
        BubbleSeekBar.OnProgressChangedListener {
        override fun onProgressChanged(
            bubbleSeekBar: BubbleSeekBar?,
            progress: Float,
            thumbText: String,
            fromUser: Boolean
        ) {
            if(fromUser){
                action(thumbText)
            }
        }

        override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
        }

        override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?,progress : Float) {
            MusicPlayerManager.musicPlayerManger.seekTo(progress.toLong())
            action("")
        }
    })
}


@BindingAdapter("setProgress")
fun BubbleSeekBar.setProgress(pos: Float) {
    setNoListenerProgress(pos)
}

@BindingAdapter("setProgressMax")
fun BubbleSeekBar.setProgressMax(max: Float) {
    setMax(max)
}


@BindingAdapter("updateThumbText")
fun BubbleSeekBar.updateThumbText(str: String) {
    updateThumbText(str)
}


@BindingAdapter("bindPlayOrPause")
fun LottieAnimationView.bindPlayOrPause(state: PlayState) {
    var playAnimName = ""
    var playFinish = true

    if(isAnimating ){
        DLog.d("suolongg","z正在动画中，退出")
        return
    }

    if (!state.read && (state.state==4 || state.state == 3) && playFinish && (playAnimName == "play_start.json"||playAnimName == "")) {
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

    } else if(state.read && state.state == 3 && playFinish && playAnimName == "play_pause.json"||playAnimName == "") {
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

@BindingAdapter("bindPlayControl")
fun PlayControlView.bindPlayControl(playState: PlayState){
    startOrPause(playState = playState)
}

@BindingAdapter("bindPlayControlClick")
fun PlayControlView.bindPlayControlClick(action: (() -> Unit)?) {
    if (action != null) {
        setOnClickListener {
            if(playFinish){
                action()
            }
        }
    }
}


@BindingAdapter("bindPlayPreSrc")
fun ImageView.bindPlayPreSrc(hasPre: Boolean) {
    setImageResource(
        if (hasPre) {
            R.drawable.play_ic_has_pre
        } else {
            R.drawable.play_ic_none_pre
        }
    )
}

@BindingAdapter("bindPlayNextSrc")
fun ImageView.bindPlayNextSrc(hasNext: Boolean) {
    setImageResource(
        if (hasNext) {
            R.drawable.play_ic_has_next
        } else {
            R.drawable.play_ic_none_next
        }
    )
}


@BindingAdapter("bindPlayPrepareProgress")
fun CircularProgressView.bindPlayPrepareProgress(playStatus: PlayState) {
    if (playStatus.state == STATE_BUFFERING) {
        startAutoProgress()
    } else {
        stopAutoProgress()
    }
}

@BindingAdapter("bindPlayTimeCountText")
fun TextView.bindPlayTimeCountText(timeCount: Long) {
    if (timeCount <= 0) {
        visibility = View.GONE
        return
    } else {
        visibility = View.VISIBLE
        text = TimeUtils.getListenDuration(timeCount)
    }
}

@BindingAdapter("bindPlaySpeedText")
fun TextView.bindPlaySpeedText(float: Float?) {
    if (float == null) {
        visibility = View.GONE
        return
    }
    if (float <= 0) {
        visibility = View.GONE
        return
    }

    text = (
        if (0F < float && float <= 0.5F) {
            "0.5x"
        } else if (0.5F < float && float <= 0.75F) {
            "0.75x"
        } else if (0.75F < float && float <= 1.0F) {
            "1.0x"
        } else if (1.0F < float && float <= 1.25F) {
            "1.25x"
        } else if (1.25F < float && float <= 1.5F) {
            "1.5x"
        } else {
            "2.0x"
        }
    )

}

@BindingAdapter("bindPlaySpeedSrc")
fun ImageView.bindPlaySpeedSrc(float: Float?) {
    if (float == null) {
        setImageResource(R.drawable.play_ic_speed_1x)
        return
    }
    if (float <= 0) {
        setImageResource(R.drawable.play_ic_speed_1x)
        return
    }

    setImageResource(
        if (0F < float && float <= 0.5F) {
            R.drawable.play_ic_speed_0_5x
        } else if (0.5F < float && float <= 0.75F) {
            R.drawable.play_ic_speed_0_7_5x
        } else if (0.75F < float && float <= 1.0F) {
            R.drawable.play_ic_speed_1x
        } else if (1.0F < float && float <= 1.25F) {
            R.drawable.play_ic_speed_1_2_5x
        } else if (1.25F < float && float <= 1.5F) {
            R.drawable.play_ic_speed_1_5x
        } else {
            R.drawable.play_ic_speed_2x
        }
    )

}