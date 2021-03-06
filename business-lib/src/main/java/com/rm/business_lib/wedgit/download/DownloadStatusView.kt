package com.rm.business_lib.wedgit.download

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.FrameLayout
import com.hjq.permissions.Permission
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.constant.PermissionConstants
import com.rm.business_lib.R
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.download.DownloadMemoryCache
import com.tencent.bugly.proguard.ad
import kotlinx.android.synthetic.main.layout_download_status_view.view.*

/**
 * desc   :
 * date   : 2020/10/29
 * version: 1.0
 */
class DownloadStatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_download_status_view, this)
    }

    var audio: DownloadAudio? = null


    fun setDownloadStatus(chapter: DownloadChapter) {
        when (chapter.down_status) {
            DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_start)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
                setOnClickListener {
                    checkContext {
                        startDownloadChapter(chapter)
                    }
                }
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.VISIBLE
                businessDownWaitLv.playAnimation()
                setOnClickListener {
                    checkContext { startDownloadChapter(chapter) }
                }
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOADING -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.VISIBLE
                if((chapter.size / 100) == 0L){
                    businessDownProgress.progress  = 0
                }else{
                    businessDownProgress.progress = (chapter.current_offset / (chapter.size / 100)).toInt()
                }
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
                setOnClickListener {
                    DownloadMemoryCache.downloadingChapterClick(chapter)
                }
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_pause)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
                setOnClickListener {
                    DownloadMemoryCache.downloadingChapterClick(chapter)
                }
            }
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> {
                businessDownIv.visibility = View.VISIBLE
                businessDownIv.setImageResource(R.drawable.business_download_finish)
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
                setOnClickListener {

                }
            }
            else -> {
                businessDownIv.visibility = View.GONE
                businessDownProgress.visibility = View.GONE
                businessDownWaitLv.visibility = View.GONE
                businessDownWaitLv.clearAnimation()
                setOnClickListener {

                }
            }
        }
    }

    private fun startDownloadChapter(chapter: DownloadChapter) {
        if (audio != null) {
            DownloadMemoryCache.addAudioToDownloadMemoryCache(audio!!)
        }
        DownloadMemoryCache.addDownloadingChapter(context, mutableListOf(chapter))
    }

    private fun checkContext(actionGranted: () -> Unit) {
        if(context is BaseActivity){
            requestPermission(context as BaseActivity,actionGranted)

        }else{
            //???Dialog???????????????context?????????ContextThemeWrapper???
            if(context is ContextThemeWrapper){
                if((context as ContextThemeWrapper).baseContext is BaseActivity){
                    requestPermission((context as ContextThemeWrapper).baseContext as BaseActivity,actionGranted)
                }
            }
            else if(context is androidx.appcompat.view.ContextThemeWrapper){
                if((context as androidx.appcompat.view.ContextThemeWrapper).baseContext is BaseActivity){
                    requestPermission((context as androidx.appcompat.view.ContextThemeWrapper).baseContext as BaseActivity,actionGranted)
                }
            }
        }
    }

    private fun requestPermission(baseActivity : BaseActivity?, actionGranted: () -> Unit) {
        baseActivity?.let {
            baseActivity.requestPermissionForResult(permission = mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                actionDenied = {
                    ToastUtil.showTopToast(baseActivity, baseActivity.getString(R.string.business_listen_storage_permission_refuse))
                },
                actionGranted = {
                    actionGranted()
                },
                actionPermanentlyDenied = {
                    ToastUtil.showTopToast(baseActivity, baseActivity.getString(R.string.business_listen_to_set_storage_permission))
                })
        }
    }


}