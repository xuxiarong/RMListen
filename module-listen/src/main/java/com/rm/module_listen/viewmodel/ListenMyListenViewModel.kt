package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.model.ListenRecentDateModel
import com.rm.module_listen.model.ListenRecentListenModel

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenViewModel : BaseVMViewModel() {

    var recentListenData = MutableLiveData<MutableList<MultiItemEntity>>()
    var todayListenData = MutableLiveData<MutableList<MultiItemEntity>>()
    var lastMonthListenData = MutableLiveData<MutableList<MultiItemEntity>>()
    var earlyListenData = MutableLiveData<MutableList<MultiItemEntity>>()

    fun getTodayListenDataFromLocal(){
        todayListenData.value = mutableListOf(
            ListenRecentDateModel("今天",true),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "心要大，脚要实",
                "第一集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听完"
            ),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "不要害怕把自己的弱项暴露给他人",
                "第八集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听90%"
            )
        )
    }

    fun getRecentMonthListenDataFromLocal(){
        lastMonthListenData.value = mutableListOf(
            ListenRecentDateModel("最近一月",true),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "心要大，脚要实",
                "第一集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听完"
            ),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "不要害怕把自己的弱项暴露给他人",
                "第八集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听90%"
            )
        )
    }

    fun getEarlyListenDataFromLocal(){
        earlyListenData.value = mutableListOf(
            ListenRecentDateModel("更早",true),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "心要大，脚要实",
                "第一集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听完"
            ),
            ListenRecentListenModel(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                8.0f,
                "不要害怕把自己的弱项暴露给他人",
                "第八集 我喜欢的那个太空",
                "VIP",
                "34:12",
                "已听90%"
            )
        )
    }

}