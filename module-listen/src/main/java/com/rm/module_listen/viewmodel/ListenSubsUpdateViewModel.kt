package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.model.*
import com.rm.module_listen.repository.ListenMyListenRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenSubsUpdateViewModel : BaseVMViewModel() {

    private val repository by lazy {
        ListenMyListenRepository(BusinessRetrofitClient().getListenService(ListenApiService::class.java))
    }


    var subsDateVisible = ObservableBoolean(true)
    var subsDateListDate = MutableLiveData<MutableList<ListenAudioDateModel>>()
    var subsData = MutableLiveData<ListenSubsModel>()

    var todayUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var yesterdayUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var earlyUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var allUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()

    var onAudioClick : (String)->Unit = {}

    fun onAudioClickFun(audio : ListenAudioModel){
        onAudioClick(audio.audio_id)
    }

    fun getSubsDataFromService(){
        showLoading()
        launchOnIO {
            repository.getListenSubsUpgradeList().checkResult(
                onSuccess = {
//                    dealServiceData(it)
                    var list = it.list
                },onError = {
                    showNetError()
                    DLog.d("suolong","on error")
                }
            )
        }

        subsDateListDate.value = mutableListOf(
            ListenAudioDateModel("今天",false),
            ListenAudioDateModel("昨天",false),
            ListenAudioDateModel("8-29",false),
            ListenAudioDateModel("8-28",true),
            ListenAudioDateModel("8-27",false),
            ListenAudioDateModel("8-26",false),
            ListenAudioDateModel("8-25",false),
            ListenAudioDateModel("8-24",false),
            ListenAudioDateModel("8-23",false),
            ListenAudioDateModel("8-22",false)
        )
    }

    fun dealServiceData(it: ListenSubsModel) {
        if(it.today.isEmpty() && it.yesterday.isEmpty() && it.early.isEmpty()){
            showDataEmpty()
            return
        }else{
            showContentView()
        }

        allUpdateList.value?.clear()
        todayUpdateList.value?.clear()
        yesterdayUpdateList.value?.clear()
        earlyUpdateList.value?.clear()

        val tempAllData = ArrayList<MultiItemEntity>()
        val serviceToday = ArrayList<MultiItemEntity>()
        val serviceYes = ArrayList<MultiItemEntity>()
        val serviceEarly = ArrayList<MultiItemEntity>()
        if(it.today.isNotEmpty()){
            tempAllData.add(ListenAudioDateModel("今天",false))
            tempAllData.add(ListenAudioTodayRvModel())
            allUpdateList.value = tempAllData
            it.today.forEach {
                it.itemType = R.layout.listen_item_subs_list_audio
                it.chapter_list.forEach {
                    it.itemType = R.layout.listen_item_subs_list_chapter
                }
                serviceToday.add(it)
                serviceToday.addAll(it.chapter_list)
            }
        }
        if(it.yesterday.isNotEmpty()){
            tempAllData.add(ListenAudioDateModel("昨天",false))
            tempAllData.add(ListenAudioYesterdayRvModel())
            allUpdateList.value = tempAllData
            it.yesterday.forEach {
                it.itemType = R.layout.listen_item_subs_list_audio
                it.chapter_list.forEach {
                    it.itemType = R.layout.listen_item_subs_list_chapter
                }
                serviceYes.add(it)
                serviceYes.addAll(it.chapter_list)
            }
        }
        if(it.early.isNotEmpty()){

            it.early.forEach { subsEarly ->
                tempAllData.add(ListenAudioDateModel(subsEarly.upgrade_time,false))
                tempAllData.add(ListenAudioEarlyRvModel())
                allUpdateList.value = tempAllData
                subsEarly.itemType = R.layout.listen_item_subs_list_audio
                subsEarly.chapter_list.forEach { chapter ->
                    chapter.itemType = R.layout.listen_item_subs_list_chapter
                }
                serviceEarly.add(subsEarly)
                serviceEarly.addAll(subsEarly.chapter_list)
            }
        }

        todayUpdateList.value = serviceToday
        yesterdayUpdateList.value = serviceYes
        earlyUpdateList.value = serviceEarly


    }

}