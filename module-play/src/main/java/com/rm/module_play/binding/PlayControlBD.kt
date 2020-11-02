package com.rm.module_play.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.wedgit.progressbar.CircularProgressView
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.module_play.cache.PlayState
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
        }

        override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
        }

        override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?,progress : Float) {
            MusicPlayerManager.musicPlayerManger.seekTo(progress.toLong())
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
fun LottieAnimationView.startLottieAnimation(state: PlayState) {
    if (!state.read || state.state==4) {
        setAnimation("stop_play.json")
    } else {
        setAnimation("play_stop.json")
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
fun CircularProgressView.bindPlayPrepareProgress(playStatus: PlayState) {
    if (playStatus.state == 2) {
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