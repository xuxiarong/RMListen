package com.rm.module_listen.bind

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.TimeUtils
import com.rm.module_listen.R
import com.rm.module_listen.model.ListenHistoryModel

/**
 * desc   :
 * date   : 2020/09/23
 * version: 1.0
 */

@BindingAdapter("bindSubsText")
fun TextView.bindSubsText(number: Int?) {
    text = when {
        number == null -> {
            resources.getString(R.string.listen_my_listen_subs)
        }
        number <= 0 -> {
            resources.getString(R.string.listen_my_listen_subs)
        }
        else -> {
            String.format(resources.getString((R.string.listen_my_listen_subs_number), number))
        }
    }
}

@BindingAdapter("listenBindChapterName")
fun TextView.listenBindChapterName(historyModel: ListenHistoryModel) {
    text = ""
    try {
        text = historyModel.chapter.chapter_name
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("listenBindChapterTime")
fun TextView.listenBindChapterTime(historyModel: ListenHistoryModel) {
    text = ""
    try {
        text = TimeUtils.getPlayDuration(historyModel.chapter.realDuration)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter(
    "listenBindChapterStatus",
    "listenBindPlayAudio",
    "listenBindPlayProgress",
    requireAll = true
)
fun TextView.listenBindChapterStatus(
    historyModel: ListenHistoryModel,
    playAudioModel: BasePlayInfoModel?,
    progressModel: BasePlayProgressModel?
) {
    text = ""
    try {
        val lastChapter = historyModel.chapter
        if (progressModel != null && playAudioModel != null) {
            var result = if (lastChapter.chapter_id.toString() == playAudioModel.playChapterId) {
                progressModel.currentDuration
            } else {
                lastChapter.listen_duration
            }
            if (result <= 0) {
                result = 0
            }
            if (result >= lastChapter.realDuration) {
                text = context.getString(R.string.listen_finish)
                setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
                text = "${String.format(
                    context.getString(R.string.listen_progress),
                    (result * 100 / lastChapter.duration)
                )}%"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}