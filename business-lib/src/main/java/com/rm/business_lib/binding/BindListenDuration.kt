package com.rm.business_lib.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayInfoModel
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.business_lib.R
import com.rm.business_lib.db.download.DownloadChapter

/**
 * desc   :
 * date   : 2020/12/17
 * version: 1.0
 */

@SuppressLint("SetTextI18n")
@BindingAdapter(
    "downloadBindChapterStatus",
    "downloadBindPlayAudio",
    "downloadBindPlayProgress",
    requireAll = true
)
fun TextView.downloadBindChapterStatus(
    downloadChapter: DownloadChapter,
    playAudioModel: BasePlayInfoModel?,
    progressModel: BasePlayProgressModel?
) {
    text = ""
    try {
        if (progressModel != null && playAudioModel != null) {
            val result =
                if (downloadChapter.chapter_id.toString() == playAudioModel.playChapterId) {
                    progressModel.currentDuration
                } else {
                    downloadChapter.listen_duration
                }
            if (result <= 0) {
                return
            }
            if (result >= downloadChapter.realDuration) {
                text = context.getString(R.string.business_listen_finish)
                setTextColor(ContextCompat.getColor(context, R.color.business_color_b1b1b1))
            } else {
                var percent = (result * 100 / downloadChapter.realDuration)
                if(percent<1){
                    percent = 1
                }
                setTextColor(ContextCompat.getColor(context, R.color.business_color_ffba56))
                text = "${String.format(context.getString(R.string.business_listen_progress),percent) }%"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}