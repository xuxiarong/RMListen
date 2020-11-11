package com.rm.module_listen.viewmodel

import android.text.TextUtils
import android.view.View
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.adapter.ListenSheetCollectedListAdapter
import com.rm.module_listen.bean.SheetFavorBean
import com.rm.module_listen.bean.SheetFavorDataBean
import com.rm.module_listen.repository.ListenRepository

class ListenSheetCollectedListViewModel(private val repository: ListenRepository) :
    BaseVMViewModel() {

    /**
     * 懒加载创建adapter对象
     */
    val mAdapter by lazy {
        ListenSheetCollectedListAdapter(this)
    }

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    //每页加载的数据
    private val pageSize = 12

    //当前加载的页码
    private var mPage = 1

    //记录点击的bean对象
    private var clickBean: SheetFavorDataBean? = null

    var memberId = ""

    /**
     * 请求加载数据
     */
    fun getData(memberId: String) {
        if (TextUtils.isEmpty(memberId)) {
            getFavorList()
        } else {
            getFavorList(memberId)
        }
    }

    private fun getFavorList(memberId: String) {
        launchOnIO {
            repository.getCollectedList(mPage, pageSize, memberId).checkResult(
                onSuccess = {
                    successData(it)
                },
                onError = {
                    failData()
                }
            )
        }
    }

    private fun getFavorList() {
        launchOnIO {
            repository.getCollectedList(mPage, pageSize).checkResult(
                onSuccess = {
                    successData(it)
                },
                onError = {
                    failData()
                }
            )
        }
    }

    /**
     * 处理成功数据
     */
    private fun successData(bean: SheetFavorBean) {
        showContentView()
        if (mPage == 1) {
            //刷新完成
            refreshStateModel.finishRefresh(true)
            if (bean.list.isNotEmpty()) {
                showContentView()
                mAdapter.setList(bean.list)
            } else {
                showDataEmpty()
            }
        } else {
            //加载更多完成
            refreshStateModel.finishLoadMore(true)
            mAdapter.addData(bean.list)
        }

        //是否有更多数据
        refreshStateModel.setHasMore(bean.list.size > pageSize)
    }

    /**
     * 处理请求失败
     */
    private fun failData() {
        showContentView()
        if (mPage == 1) {
            refreshStateModel.finishRefresh(false)
        } else {
            refreshStateModel.finishLoadMore(false)
        }
    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        getData(memberId)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        getData(memberId)
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: SheetFavorDataBean) {
        getActivity(view.context)?.let {
            clickBean = bean
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id.toString())
        }
    }

    /**
     * 子view item点击事件
     */
    fun itemChildClickFun(view: View, bean: DownloadAudio) {
        getActivity(view.context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .toDetailActivity(it, bean.audio_id.toString())
        }
    }

    /**
     * 通过听单id 移除
     */
    fun remove(sheetId: String) {
        if (clickBean != null) {
            if (sheetId == clickBean!!.sheet_id.toString()) {
                mAdapter.remove(clickBean!!)
            } else {
                removeIndex(sheetId)
            }
        } else {
            removeIndex(sheetId)
        }
    }

    /**
     * 通过下标移除
     */
    private fun removeIndex(sheetId: String) {
        val index = getIndex(sheetId)
        if (index != -1) {
            mAdapter.removeAt(index)
        }
    }

    /**
     * 通过听单id 获取下标
     */
    private fun getIndex(sheetId: String): Int {
        mAdapter.data.forEachIndexed { index, bean ->
            if (bean.sheet_id.toString() == sheetId) {
                return index
            }
        }
        return -1
    }

}