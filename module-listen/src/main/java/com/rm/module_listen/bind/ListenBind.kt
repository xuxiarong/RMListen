package com.rm.module_listen.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
            text = audio.HistoryPlayBook.listBean[0].duration.toString()
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}

@BindingAdapter("listenBindChapterPlayCount")
fun TextView.listenBindChapterPlayCount(audio:ListenHistoryModel){
    text = ""
    try {
        if (audio.HistoryPlayBook.listBean.isNotEmpty()){
            text = audio.HistoryPlayBook.listBean[0].play_count
        }
    }catch (e : Exception){
        e.printStackTrace()
    }
}