package com.rm.module_home.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/11/24
 * version: 1.0
 */

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("bindPlayStatusModel", "bindPlayAudioModel", "bindPlayListenModel", requireAll = true)
fun TextView.bindPlayStatusText(statusModel: BasePlayStatusModel?, playAudioModel: BasePlayInfoModel?, listenModel: ListenAudioEntity?) {
    try {
        var resID = R.drawable.home_detail_icon_all_play
        if (null == statusModel || null == playAudioModel || null == listenModel) {
            text = context.getString(R.string.home_play_all)
        } else {
            if (listenModel.audio_id.toString() == playAudioModel.playAudioId) {
                if (statusModel.isStart()) {
                    text = context.getString(R.string.home_pause_play)
                    resID = R.drawable.home_detail_icon_all_stop
                } else {
                    text = context.getString(R.string.home_resume_pay)
                }
            }else{
                text = context.getString(R.string.home_resume_pay)
            }
        }
        val resDrawable = context.resources.getDrawable(resID, null)
        resDrawable.setBounds(0, 0, resDrawable.minimumWidth, resDrawable.minimumHeight)
        setCompoundDrawables(resDrawable, null, null, null)
    } catch (e: Exception) {
        text = context.getString(R.string.home_play_all)
        val resDrawable = context.resources.getDrawable(R.drawable.home_detail_icon_all_play, null)
        resDrawable.setBounds(0, 0, resDrawable.minimumWidth, resDrawable.minimumHeight)
        setCompoundDrawables(resDrawable, null, null, null)
    }
}

@BindingAdapter("bindPlayStatusModel", "bindPlayAudioModel", "bindDetailChapter","bindDetailListen", requireAll = true)
fun ImageView.bindPlayChapter(statusModel: BasePlayStatusModel?, playAudioModel: BasePlayInfoModel?, chapter: DownloadChapter,listenModel: ListenAudioEntity?){
    if(statusModel != null && playAudioModel != null && playAudioModel.playChapterId == chapter.chapter_id.toString()){
        visibility = View.VISIBLE
        if(statusModel.isStart()){
           setImageResource(R.drawable.business_ic_playing)
       }else{
           setImageResource(R.drawable.business_ic_play_pause)
       }
    }else{
        if(listenModel!=null){
            if(listenModel.listenChapterId == chapter.chapter_id.toString()){
                if(chapter.audio_id.toString() != playAudioModel?.playAudioId){
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.business_ic_played)
                }else{
                    visibility = View.GONE
                }
            }else{
                visibility = View.GONE
            }
        }else{
            visibility = View.GONE
        }
    }
}

