package com.rm.module_play.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
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


@BindingAdapter("addBubbleFL")
fun BubbleSeekBar.addBubbleFL(text: TextView) {
    addBubbleFL(text)
}

@BindingAdapter("updateThumbText")
fun BubbleSeekBar.updateThumbText(str:String) {
    updateThumbText(str)
}