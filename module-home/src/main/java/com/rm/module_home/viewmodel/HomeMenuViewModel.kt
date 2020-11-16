package com.rm.module_home.viewmodel

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.SheetListBean
import com.rm.business_lib.bean.SheetMenuInfoBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity.Companion.SHEET_ID
import com.rm.module_home.adapter.MenuListAdapter
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.repository.HomeRepository
import kotlin.properties.Delegates

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeMenuViewModel(private val repository: HomeRepository) : BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    // 听单详情数据
    var menuList = MutableLiveData<MenuSheetBean>()

    //当前pageId
    private var mPageId by Delegates.notNull<Int>()

    //每次加载的条数
    private val pageSize = 5

    //当前的页码
    private var mPage = 1

    //懒加载adapter
    val menuAdapter = MenuListAdapter(this)

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStatusModel.setNoHasMore(false)
        getSheetList()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getSheetList()
    }

    /**
     * 获取听单详情
     */
    fun getMenuListInfo() {
        launchOnIO {
            repository.sheet().checkResult(
                onSuccess = {
                    showContentView()
                    menuList.value = it
                    mPageId = it.page_id
                },
                onError = {
                    showServiceError()
                }
            )
        }
    }

    /**
     * 获取听单列表
     */
    fun getSheetList() {
        launchOnIO {
            repository.getSheetList(mPage, pageSize)
                .checkResult(onSuccess = {
                    processSuccessData(it)
                }, onError = {
                    processFailData()
                })
        }
    }

    /**
     * 处理成功的数据
     */
    private fun processSuccessData(bean: SheetListBean) {
        showContentView()
        if (mPage == 1) {
            //刷新完成
            menuAdapter.setList(processList(bean.list))
            processList(bean.list)
            refreshStatusModel.finishRefresh(true)
        } else {
            //加载完成
            bean.list?.let {
                menuAdapter.addData(processList(it)!!)
            }
            refreshStatusModel.finishLoadMore(true)
        }

        if (menuAdapter.data.size >= bean.total) {
            //没用更多数据
            refreshStatusModel.setNoHasMore(true)
        } else {
            mPage++
        }

    }

    /**
     * 处理数据，如果书单音频书不足三本则移除
     */
    private fun processList(list: MutableList<SheetMenuInfoBean>?): MutableList<SheetMenuInfoBean>? {
        val iterator = list?.iterator()
        while (iterator?.hasNext() == true) {
            val next = iterator.next()
            if (next.audio_list.size < 3) {
                iterator.remove()
            }
        }
        return list
    }

    /**
     * 处理失败的数据
     */
    private fun processFailData() {
        if (mPage == 1) {
            showContentView()
            refreshStatusModel.finishRefresh(false)
        } else {
            mPage--
            refreshStatusModel.finishLoadMore(false)
        }
    }


    /**
     * item点击事件
     */
    fun itemClickFun(bean: SheetMenuInfoBean) {
        val map = getHasMap()
        map[SHEET_ID] = bean.sheet_id
        startActivityForResult(HomeMenuDetailActivity::class.java, map, 100)
    }

    /**
     * item中子item点击事件
     */
    fun itemChildClickFun(context: Context, bean: DownloadAudio) {
        HomeDetailActivity.startActivity(context, bean.audio_id.toString())
    }

}