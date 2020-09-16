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
import com.rm.module_listen.repository.ListenSubsUpdateRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenSubsUpdateViewModel : BaseVMViewModel() {

    private val repository by lazy {
        ListenSubsUpdateRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }


    var subsDateVisible = ObservableBoolean(true)
    var subsDateListDate = MutableLiveData<MutableList<ListenAudioDateModel>>()
    var subsData = MutableLiveData<ListenSubsModel>()

    var todayUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var yesterdayUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var earlyUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()
    var allUpdateList = MutableLiveData<MutableList<MultiItemEntity>>()

    fun getSubsDataFromService(){

        launchOnIO {
            repository.getListenSubsUpgradeList().checkResult(
                onSuccess = {
                    dealServiceData(it)
                },onError = {
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
            tempAllData.add(ListenAudioDateModel("更早",false))
            tempAllData.add(ListenAudioEarlyRvModel())
            allUpdateList.value = tempAllData
            it.early.forEach { subsEarly ->
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

//    fun testData(){
//        var todayData = ArrayList<ListenSubsToday>()
//        for (i in 0..5){
//            todayData.add(ListenSubsToday(
//                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
//                "today_id_$i",
//                "今天第{$i}本",
//                testTodayChapter()
//            ))
//        }
//        var yesterdayData = ArrayList<ListenSubsYesterday>()
//        for (i in 0..5){
//            todayData.add(ListenSubsToday(
//                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
//                "today_id_$i",
//                "今天第{$i}本",
//                testYesChapter()
//            ))
//        }
//
//        var early = ArrayList<ListenSubsEarly>()
//        for (i in 0..5){
//            todayData.add(ListenSubsToday(
//                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
//                "today_id_$i",
//                "今天第{$i}本",
//                testEarlyChapter()
//            ))
//        }
//        subsData.value = ListenSubsModel(
//            today = todayData,
//            yesterday = yesterdayData,
//            early =  early,
//            last_unread = 0,
//            total_unread = 1
//        )
//    }

    fun testTodayChapter() : MutableList<ListenSubsChapter>{
        return mutableListOf(
            ListenSubsChapter(
                "chapter_1",
                chapter_name =  "第1章",
                duration = "10:10",
                path = "d://test",
                play_count = 1000L,
                sequence = "100",
                size = "100M",
                upgrade_time = "今天"
            ),
            ListenSubsChapter(
                "chapter_2",
                chapter_name =  "第2章",
                duration = "20:20",
                path = "d://test",
                play_count = 2000L,
                sequence = "200",
                size = "200M",
                upgrade_time = "今天"
            )
        )
    }

    fun testYesChapter() : MutableList<ListenSubsChapter>{
        return mutableListOf(
            ListenSubsChapter(
                "chapter_1",
                chapter_name =  "第1章",
                duration = "10:10",
                path = "d://test",
                play_count = 1000L,
                sequence = "100",
                size = "100M",
                upgrade_time = "昨天"
            ),
            ListenSubsChapter(
                "chapter_2",
                chapter_name =  "第2章",
                duration = "20:20",
                path = "d://test",
                play_count = 2000L,
                sequence = "200",
                size = "200M",
                upgrade_time = "昨天"
            )
        )
    }
    fun testEarlyChapter() : MutableList<ListenSubsChapter>{
        return mutableListOf(
            ListenSubsChapter(
                "chapter_1",
                chapter_name =  "第1章",
                duration = "10:10",
                path = "d://test",
                play_count = 1000L,
                sequence = "100",
                size = "100M",
                upgrade_time = "2020/09/01"
            ),
            ListenSubsChapter(
                "chapter_2",
                chapter_name =  "第2章",
                duration = "20:20",
                path = "d://test",
                play_count = 2000L,
                sequence = "200",
                size = "200M",
                upgrade_time = "2020/09/02"
            )
        )
    }

}