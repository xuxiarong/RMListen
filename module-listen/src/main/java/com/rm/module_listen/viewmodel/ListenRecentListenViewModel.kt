package com.rm.module_listen.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.model.ListenHistoryModel
import com.rm.module_listen.model.ListenRecentDateModel
import com.rm.module_listen.model.ListenRecentListenModel

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenRecentListenViewModel : BaseVMViewModel() {

    var todayListenData = MutableLiveData<MutableList<MultiItemEntity>>()
    var lastMonthListenData = MutableLiveData<MutableList<MultiItemEntity>>()
    var earlyListenData = MutableLiveData<MutableList<MultiItemEntity>>()

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(
            this, mutableListOf(),
            R.layout.listen_item_recent_listen,
            R.id.listenRecentSl,
            BR.viewModel,
            BR.item
        )
    }


    fun getTodayListenDataFromLocal() {
        showLoading()
        launchOnIO {
            val queryPlayBookList = playService.queryPlayBookList()
            if (queryPlayBookList != null && queryPlayBookList.isNotEmpty()) {
                showContentView()
                mSwipeAdapter.addData(ListenRecentDateModel())
                queryPlayBookList.forEach {
                    mSwipeAdapter.addData(ListenHistoryModel(it))
                }
            } else {
                showDataEmpty()
            }
        }








        todayListenData.value = mutableListOf(
            ListenRecentDateModel(),
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

    fun getRecentMonthListenDataFromLocal() {
        lastMonthListenData.value = mutableListOf(
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

    fun getEarlyListenDataFromLocal() {
        earlyListenData.value = mutableListOf(
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

    fun startListenRecentDetail(context: Context) {

    }

    fun deleteItem(item: MultiItemEntity) {
        mSwipeAdapter.data.remove(item)
        mSwipeAdapter.notifyDataSetChanged()
    }

}