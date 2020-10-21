package com.rm.module_download.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.DownloadMemoryCache
import com.rm.business_lib.bean.download.DownloadAudioStatusModel
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {

    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }
    private val homeService by lazy { RouterHelper.createRouter(HomeService::class.java) }

    val downloadingAdapter by lazy {
        CommonBindVMAdapter<DownloadChapterStatusModel>(
            this,
            mutableListOf(),
            R.layout.download_item_in_progress,
            BR.viewModel,
            BR.itemBean
        )
    }

    val downloadFinishAdapter by lazy {
        CommonBindVMAdapter<DownloadAudioStatusModel>(
            this,
            mutableListOf(),
            R.layout.download_item_download_completed,
            BR.viewModel,
            BR.itemBean
        )
    }

    var downloadingSelected = ObservableBoolean(false)
    var downloadingEdit = ObservableBoolean(false)
    var downloadingSelectAll = ObservableBoolean(false)
    var downloadingSelectNum = ObservableInt(0)

    var downloadFinishSelected = ObservableBoolean(false)
    var downloadFinishEdit = ObservableBoolean(false)
    var downloadFinishSelectAll = ObservableBoolean(false)
    var downloadFinishSelectNum = ObservableInt(0)
    var downloadFinishDeleteListenFinish = ObservableBoolean(false)


    var downloadAudioList = DownloadMemoryCache.downloadingAudioList


    fun getDownloadFromDao() {
        launchOnIO {
            val allData = DaoUtil(DownloadAudio::class.java, "").queryAll()
            DLog.d("suolong", "${allData?.size}")
            allData?.let {
                var tempAudioStatusList = mutableListOf<DownloadAudioStatusModel>()
                it.forEach {
                    tempAudioStatusList.add(DownloadAudioStatusModel(audio = it))
                }
                if (tempAudioStatusList.size > 0) {
                    downloadAudioList.postValue(tempAudioStatusList)
                }
            }
        }
    }

    fun editDownloading() {
        downloadingEdit.set(downloadingEdit.get().not())
    }

    fun editDownloadFinish() {
        downloadFinishEdit.set(downloadFinishEdit.get().not())
    }

    fun changeDownloadingAll(){
        val selectAll = downloadingSelectAll.get()
        downloadingAdapter.data.forEach {
            if(!selectAll && it.select == DownloadConstant.CHAPTER_UN_SELECT){
                it.select = DownloadConstant.CHAPTER_SELECTED
                downloadingSelectNum.set(downloadingSelectNum.get().plus(1))
            }else if(selectAll && it.select == DownloadConstant.CHAPTER_SELECTED){
                it.select = DownloadConstant.CHAPTER_UN_SELECT
                downloadingSelectNum.set(downloadingSelectNum.get().plus(-1))
            }
        }
        downloadingSelectAll.set(selectAll.not())
        downloadingAdapter.notifyDataSetChanged()
    }

    fun changeDownloadChapterSelect(model:DownloadChapterStatusModel){
        if(model.select == DownloadConstant.CHAPTER_UN_SELECT){
            model.select = DownloadConstant.CHAPTER_SELECTED
            downloadingSelectNum.set(downloadingSelectNum.get().plus(1))
        }else if(model.select == DownloadConstant.CHAPTER_SELECTED){
            model.select = DownloadConstant.CHAPTER_UN_SELECT
            downloadingSelectNum.set(downloadingSelectNum.get().plus(-1))
        }
    }

    fun chapterClick(context: Context,model:DownloadChapterStatusModel) {
        if (downloadingEdit.get()) {
            changeDownloadChapterSelect(model = model)
            downloadingAdapter.notifyItemChanged(downloadingAdapter.data.indexOf(model))
        }else{

        }
    }


    fun changeDownloadFinishAll(){
        val selectAll = downloadFinishSelectAll.get()
        downloadFinishAdapter.data.forEach {
            if(!selectAll && it.select == DownloadConstant.AUDIO_UN_SELECT){
                it.select = DownloadConstant.AUDIO_SELECTED
                downloadFinishSelectNum.set(downloadFinishSelectNum.get().plus(1))
            }else if(selectAll && it.select == DownloadConstant.AUDIO_SELECTED){
                it.select = DownloadConstant.AUDIO_UN_SELECT
                downloadFinishSelectNum.set(downloadFinishSelectNum.get().plus(-1))
            }
        }
        downloadFinishSelectAll.set(selectAll.not())
        downloadFinishAdapter.notifyDataSetChanged()
    }

    fun changeDownloadFinishSelect(model:DownloadAudioStatusModel){
        if(model.select == DownloadConstant.AUDIO_UN_SELECT){
            model.select = DownloadConstant.AUDIO_SELECTED
            downloadFinishSelectNum.set(downloadFinishSelectNum.get().plus(1))
        }else if(model.select == DownloadConstant.AUDIO_SELECTED){
            model.select = DownloadConstant.AUDIO_UN_SELECT
            downloadFinishSelectNum.set(downloadFinishSelectNum.get().plus(-1))
        }
    }

    fun changeSelectListenFinish(){
        downloadFinishDeleteListenFinish.set(downloadFinishDeleteListenFinish.get().not())
    }

    fun audioClick(context: Context, model: DownloadAudioStatusModel) {
        if (downloadFinishEdit.get()) {
            changeDownloadFinishSelect(model = model)
            downloadFinishAdapter.notifyItemChanged(downloadFinishAdapter.data.indexOf(model))
        } else {
            homeService.toDetailActivity(context, model.audio.audio_id.toString())
        }
    }

    fun deleteAudio(){

    }

}