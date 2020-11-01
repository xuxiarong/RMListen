package com.rm.module_play.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rm.business_lib.wedgit.progressbar.CircularProgressView
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.music_exoplayer_lib.ext.formatTimeInMillisToString
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager


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
fun LottieAnimationView.startLottieAnimation(state: Boolean) {
    if (state) {
        setAnimation("play_stop.json")
    } else {
        setAnimation("stop_play.json")
    }
    playAnimation()

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
fun CircularProgressView.bindPlayPrepareProgress(playStatus: Int) {
    if (playStatus == 2) {
        startAutoProgress()
    } else {
        stopAutoProgress()
    }
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