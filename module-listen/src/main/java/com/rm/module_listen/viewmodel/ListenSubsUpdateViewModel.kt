package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
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
    var subsDateListDate = MutableLiveData<MutableList<ListenSubsDateModel>>()
    var subsData = MutableLiveData<ListenSubsModel>()


    fun getSubsDataFromService(){

//        launchOnIO {
//            repository.getListenSubsUpgradeList().checkResult(
//                onSuccess = {
//                    subsTodayData.value = it
//                },onError = {
//                    DLog.d("suolong","on error")
//                }
//            )
//        }



        subsDateListDate.value = mutableListOf(
            ListenSubsDateModel("今天",false),
            ListenSubsDateModel("昨天",false),
            ListenSubsDateModel("8-29",false),
            ListenSubsDateModel("8-28",true),
            ListenSubsDateModel("8-27",false),
            ListenSubsDateModel("8-26",false),
            ListenSubsDateModel("8-25",false),
            ListenSubsDateModel("8-24",false),
            ListenSubsDateModel("8-23",false),
            ListenSubsDateModel("8-22",false)
        )
    }

    fun dealServiceData(){

    }

    fun testData(){
        var todayData = ArrayList<ListenSubsToday>()
        for (i in 0..5){
            todayData.add(ListenSubsToday(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "today_id_$i",
                "今天第{$i}本",
                testTodayChapter()
            ))
        }
        var yesterdayData = ArrayList<ListenSubsYesterday>()
        for (i in 0..5){
            todayData.add(ListenSubsToday(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "today_id_$i",
                "今天第{$i}本",
                testYesChapter()
            ))
        }

        var early = ArrayList<ListenSubsEarly>()
        for (i in 0..5){
            todayData.add(ListenSubsToday(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "today_id_$i",
                "今天第{$i}本",
                testEarlyChapter()
            ))
        }
        subsData.value = ListenSubsModel(
            today = todayData,
            yesterday = yesterdayData,
            early =  early,
            last_unread = 0,
            total_unread = 1
        )
    }

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