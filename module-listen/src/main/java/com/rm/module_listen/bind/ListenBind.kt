package com.rm.module_listen.bind

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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
fun TextView.listenBindChapterName(audio: ListenHistoryModel) {
    text = ""
    try {
        if (audio.audio.listenChapterList.isNotEmpty()) {
            text = audio.audio.listenChapterList[0].chapter_name
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("listenBindChapterTime")
fun TextView.listenBindChapterTime(audio: ListenHistoryModel) {
    text = ""
    try {
        if (audio.audio.listenChapterList.isNotEmpty()) {
            text = TimeUtils.getPlayDuration(audio.audio.listenChapterList.first().realDuration)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("listenBindChapterStatus")
fun TextView.listenBindChapterStatus(audio: ListenHistoryModel) {
    text = ""
    try {
        val chapterList = audio.audio.listenChapterList
        if (chapterList.isNotEmpty()) {
            val lastChapter = chapterList.first()
            var result = lastChapter.listen_duration
            if (result <= 0) {
                result = 0
            }
            if (result >= lastChapter.realDuration) {
                text = context.getString(R.string.listen_finish)
                setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
                text = "${String.format(context.getString(R.string.listen_progress), (result * 100 /lastChapter.duration))}%"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}