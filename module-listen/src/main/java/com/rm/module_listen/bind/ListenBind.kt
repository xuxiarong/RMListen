package com.rm.module_listen.bind

import android.widget.TextView
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
            text = TimeUtils.getListenHistoryTime(audio.HistoryPlayBook.listBean[0].duration * 1000L,5)
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}

@BindingAdapter("listenBindChapterStatus")
fun TextView.listenBindChapterStatus(audio:ListenHistoryModel){
    text = ""
    try {
        val chapterList = audio.HistoryPlayBook.listBean[0]

        if (audio.HistoryPlayBook.listBean.isNotEmpty()){
            val result = (chapterList.progress * 1.0f) / (chapterList.duration * 1000L)
            String.format("%.2f",(result))
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}