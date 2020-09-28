package com.rm.module_play.binding

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.module_play.model.PlayControlModel
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger


/**
 *
 * @des:
 * @data: 9/10/20 5:44 PM
 * @Version: 1.0.0
 */

@BindingAdapter("progressChangedListener")
fun BubbleSeekBar.progressChangedListener(pos: Int) {
    setOnProgressChangedListener(object :
        BubbleSeekBar.OnProgressChangedListener {
        override fun onProgressChanged(
            bubbleSeekBar: BubbleSeekBar?,
            progress: Float,
            fromUser: Boolean
        ) {
            val str =
                "${formatTimeInMillisToString(progress.toLong())}/${formatTimeInMillisToString(
                    bubbleSeekBar?.getMax()?.toLong() ?: 0
                )}"
//            music_play_bubbleSeekBar.updateThumbText(str)
//            bubbleFl.text = str
            MusicPlayerManager.musicPlayerManger.seekTo(progress.toLong())
        }

        override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
        }

        override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
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


@BindingAdapter("playState")
fun LottieAnimationView.startLottieAnimation(state: Int) {
    if (state == 2 || state == 3) {
        setAnimation("play_stop.json")
    } else if (state == 1) {
        setAnimation("stop_play.json")
    }
    playAnimation()

}

@BindingAdapter("bindLastSrc")
fun ImageView.bindLastSrc(state: Boolean=true) {
    var resourceId = 0
    if (state) {
        resourceId= R.drawable.ic_icon_previouspiece_bc
    } else {
        resourceId= R.drawable.music_play_ic_icon_bf

    }
    setImageResource(resourceId)

}