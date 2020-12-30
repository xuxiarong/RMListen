package com.rm.module_play.binding

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.view.progressbar.CircularProgressView
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadChapter
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
fun BubbleSeekBar.progressChangedListener(action: ((String) -> Unit)?) {
    if (action == null) {
        return
    }
    setOnProgressChangedListener(object :
            BubbleSeekBar.OnProgressChangedListener {
        override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Float,
                thumbText: String,
                fromUser: Boolean
        ) {
            if (fromUser) {
                action(thumbText)
            }
        }

        override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
        }

        override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?, progress: Float) {
            MusicPlayerManager.musicPlayerManger.seekTo(progress.toLong())
            action("")
        }
    })
}

@BindingAdapter("bindPlayStatusModel","bindItemChapter")
fun ImageView.bindPlayStatusModel(statusModel: BasePlayStatusModel?,item:DownloadChapter?) {
    if (statusModel == null || item == null) {
        visibility = View.GONE
        return
    }
    if (item.chapter_id == MusicPlayerManager.musicPlayerManger.getCurrentPlayerMusic()?.chapterId?.toLong()) {
            visibility = View.VISIBLE
            if (statusModel.isPause()) {
                setImageResource(R.drawable.business_ic_play_pause)
            } else {
                setImageResource(R.drawable.business_ic_playing)
            }
    } else {
        visibility = View.GONE
    }
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
fun BubbleSeekBar.updateThumbText(str: String?) {
    if (str == null) {
        return
    }
    updateThumbText(str)
}

@BindingAdapter("bindPlayControl")
fun PlayControlView.bindPlayControl(playState: BasePlayStatusModel?) {
    playState?.let {
        processPlayStatus(it)
    }
}

@BindingAdapter("bindPlayError")
fun PlayControlView.bindPlayError(isError: Boolean?) {
    isError?.let {
        showError(it)
    }
}

@BindingAdapter("bindStartPlayClick")
fun PlayControlView.bindStartPlayClick(startPlay: (() -> Unit)?) {
    startPlayVar = startPlay
}

@BindingAdapter("bindPausePlayClick")
fun PlayControlView.bindPausePlayClick(pausePlay: (() -> Unit)?) {
    pausePlayVar = pausePlay
}

@BindingAdapter("bindResetPlay")
fun PlayControlView.bindResetPlay(resetPlay: (() -> Unit)?) {
    resetPlayVar = resetPlay
}

@BindingAdapter("bindPlayCountDownSecond")
fun PlayControlView.bindPlayCountDownSecond(second: Long) {
    if (second == 0L) {
        pausePlayVar?.let {
            BaseConstance.basePlayStatusModel.get()?.let {
                if (it.isStart()) {
                    pauseAnim()
                    it()
                }
            }
        }
    }
}


@BindingAdapter("bindPlayCountDownSize")
fun PlayControlView.bindPlayCountDownSize(chapterSize: Int) {
    if (chapterSize == 0) {
        pausePlayVar?.let {
            pauseAnim()
            it()
        }
    }
}

@BindingAdapter("bindPlayTimerItemText")
fun TextView.bindPlayTimerItemText(position: Int) {
    if (position in 0..9) {
        text = if (PlayGlobalData.playCountTimerList[position] in 1..5) {
            String.format(context.getString(R.string.play_timer_chapter), PlayGlobalData.playCountTimerList[position])
        } else {
            String.format(context.getString(R.string.play_timer_second), PlayGlobalData.playCountTimerList[position])
        }
    }
}


@BindingAdapter("bindPlayCountDownSecondText")
fun TextView.bindPlayCountDownSecondText(second: Long) {
    if (second > 0L) {
        text = TimeUtils.getListenDuration(second)
        visibility = View.VISIBLE
    } else {
        text = ""
        visibility = View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindPlayCountDownSizeText", "bindPlayCountDownSecondText", "bindPlayCountPosition", "bindItemPosition", requireAll = false)
fun TextView.bindPlayCountDownSizeText(chapterSize: Int, second: Long, playPosition: Int = 0, itemPosition: Int = 0) {
    if (playPosition == itemPosition) {
        if (second > 0L) {
            text = TimeUtils.getPlayDuration(second)
            visibility = View.VISIBLE
        } else if (chapterSize > 0) {
            visibility = View.VISIBLE
            text = String.format(context.getString(R.string.play_timer_count_chapter), chapterSize)

        } else {
            text = ""
            visibility = View.GONE
        }
    } else {
        text = ""
        visibility = View.GONE
    }
}


@BindingAdapter("bindPlayPrepareProgress")
fun CircularProgressView.bindPlayPrepareProgress(playStatus: BasePlayStatusModel?) {
    if (playStatus == null) {
        return
    }
    if (playStatus.playStatus == STATE_BUFFERING) {
        startAutoProgress()
    } else {
        stopAutoProgress()
    }
}

@BindingAdapter("bindPlayPrepareProgress")
fun ImageView.bindPlayPrepareProgress(playStatus: BasePlayStatusModel?) {
    if (playStatus == null) {
        visibility = View.GONE
        return
    }
    val animationDrawable = background as AnimationDrawable
    if (background is AnimationDrawable) {
        if (playStatus.playStatus == STATE_BUFFERING) {
            visibility = View.VISIBLE
            animationDrawable.start()
        } else {
            visibility = View.GONE
            animationDrawable.stop()
        }
    }
}


@BindingAdapter("bindPlaySpeedText")
fun TextView.bindPlaySpeedText(float: Float?) {
    if (float == null) {
        visibility = View.INVISIBLE
        return
    }
    if (float <= 0 || (0.75 < float && float <= 1.0F)) {
        visibility = View.INVISIBLE
        return
    }
    visibility = View.VISIBLE

    text = if (0F < float && float <= 0.5F) {
        "0.5x"
    } else if (0.5F < float && float <= 0.75F) {
        "0.75x"
    } else if (1.0F < float && float <= 1.25F) {
        "1.25x"
    } else if (1.25F < float && float <= 1.5F) {
        "1.5x"
    } else {
        "2.0x"
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