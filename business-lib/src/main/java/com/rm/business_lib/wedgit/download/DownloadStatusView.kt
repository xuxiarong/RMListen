package com.rm.business_lib.wedgit.download

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.R
import com.rm.business_lib.db.download.DownloadChapter
import kotlinx.android.synthetic.main.layout_download_status_view.view.*

/**
 * desc   :
 * date   : 2020/10/29
 * version: 1.0
 */
class DownloadStatusView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_download_status_view, this)
    }

    fun setDownloadStatus(chapter: DownloadChapter) {
        when (chapter.down_status) {
            DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_start)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()

            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.VISIBLE
                businessDownWaitLv.playAnimation()
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.VISIBLE
                businessDownProgress.progress = (chapter.current_offset / (chapter.size / 100)).toInt()
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_pause)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_finish)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
            }
            else -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
            }
        }
    }

}