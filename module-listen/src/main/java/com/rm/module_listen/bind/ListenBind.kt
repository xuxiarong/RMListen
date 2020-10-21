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
fun TextView.bindSubsText(number:Int?){
    text = when {
        number ==null -> {
            resources.getString(R.string.listen_my_listen_subs)
        }
        number <=0 -> {
            resources.getString(R.string.listen_my_listen_subs)
        }
        else -> {
            String.format(resources.getString((R.string.listen_my_listen_subs_number),number))
        }
    }
}

@BindingAdapter("listenBindChapterName")
fun TextView.listenBindChapterName(audio:ListenHistoryModel){
    text = ""
    try {
        if (audio.HistoryPlayBook.listBean.isNotEmpty()){
            text = audio.HistoryPlayBook.listBean[0].chapter_name
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}

@BindingAdapter("listenBindChapterTime")
fun TextView.listenBindChapterTime(audio:ListenHistoryModel){
    text = ""
    try {
        if (audio.HistoryPlayBook.listBean.isNotEmpty()){
            text = TimeUtils.getListenDuration(audio.HistoryPlayBook.listBean[0].duration)
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("listenBindChapterStatus")
fun TextView.listenBindChapterStatus(audio:ListenHistoryModel){
    text = ""
    try {
        val chapterList = audio.HistoryPlayBook.listBean[0]

        if (audio.HistoryPlayBook.listBean.isNotEmpty()){
            var result = ((chapterList.progress * 1.0f) / (chapterList.duration * 1000L) * 100).toInt()
            if(result <= 0){
                result =1
            }
            if(result == 100){
                text = context.getString(R.string.listen_finish)
                setTextColor(ContextCompat.getColor(context,R.color.business_color_b1b1b1))
            }else {
                setTextColor(ContextCompat.getColor(context,R.color.business_color_ffba56))
                text = "${String.format(context.getString(R.string.listen_progress),result)}%"
            }
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}