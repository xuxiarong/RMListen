package com.rm.module_listen.bind

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.swipe.EasySwipeMenuLayout
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenRecentListenAdapter.ListenRecentListenContentItemEntity
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
            val numberStr = if (number < 100) {
                "$number"
            } else {
                "99+"
            }
            String.format(resources.getString((R.string.listen_my_listen_subs_number), numberStr))
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

@BindingAdapter("listenBindChapterName")
fun TextView.listenBindChapterName(historyModel: ListenRecentListenContentItemEntity) {
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

@BindingAdapter("listenBindChapterTime")
fun TextView.listenBindChapterTime(historyModel: ListenRecentListenContentItemEntity) {
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
            val result = if (lastChapter.chapter_id.toString() == playAudioModel.playChapterId) {
                progressModel.currentDuration
            } else {
                lastChapter.listen_duration
            }
            if (result <= 0) {
                return
            }
            if (result >= lastChapter.realDuration) {
                text = context.getString(R.string.business_listen_finish)
                setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
            } else {
                var percent = (result * 100 / lastChapter.realDuration)
                if (percent < 1) {
                    percent = 1
                }
                setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
                text = "${
                    String.format(
                        context.getString(R.string.business_listen_progress), percent
                    )
                }%"
            }
        }
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
    historyModel: ListenRecentListenContentItemEntity,
    playAudioModel: BasePlayInfoModel?,
    progressModel: BasePlayProgressModel?
) {
    text = ""
    try {
        val lastChapter = historyModel.chapter
        if (progressModel != null && playAudioModel != null) {
            val result = if (lastChapter.chapter_id.toString() == playAudioModel.playChapterId) {
                progressModel.currentDuration
            } else {
                lastChapter.listen_duration
            }
            if (result <= 0) {
                return
            }
            if (result >= lastChapter.realDuration) {
                text = context.getString(R.string.business_listen_finish)
                setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
            } else {
                var percent = (result * 100 / lastChapter.realDuration)
                if (percent < 1) {
                    percent = 1
                }
                setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
                text = "${
                    String.format(
                        context.getString(R.string.business_listen_progress), percent
                    )
                }%"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("attachChildSwipeDeleteId")
fun EasySwipeMenuLayout.attachChildSwipeDeleteId(childId: Int = 0) {
    if (childId <= 0) {
        return
    }
    attachChildSwipeDeleteId(childId)
}