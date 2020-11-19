package com.rm.module_listen.viewmodel

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_ID
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenRepository

class ListenSheetMyListViewModel(private val repository: ListenRepository) :
    BaseVMViewModel() {

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    //懒加载构建adapter对象
    val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetBean>(
            this,
            mutableListOf(),
            R.layout.listen_adapter_sheet_my_list,
            BR.click,
            BR.item
        )
    }

    //记录点击item对应的实体对象
    private val clickBean = ObservableField<ListenSheetBean>()

    private val pageSize = 12

    private var page = 1

    var memberId = ""

    /**
     * 发起网络请求数据
     */
    fun getData(memberId: String) {
        if (TextUtils.isEmpty(memberId)) {
            getMySheetList()
        } else {
            getMySheetList(memberId)
        }

    }

    private fun getMySheetList(memberId: String) {
        launchOnIO {
            repository.getMyList(page, pageSize, memberId).checkResult(
                onSuccess = {
                    successData(it)
                },
                onError = {
                    failData()
                }
            )
        }
    }

    private fun getMySheetList() {
        launchOnIO {
            repository.getMyList(page, pageSize).checkResult(
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
     * 数据请求成功
     */
    private fun successData(bean: ListenSheetMyListBean) {
        if (page == 1) {
            //刷新完成
            if (bean.list?.size ?: 0 > 0) {
                showContentView()
                refreshStateModel.finishRefresh(true)
                mAdapter.setList(bean.list)
            } else {
                showDataEmpty()
            }

        } else {
            //加载更多完成
            refreshStateModel.finishLoadMore(true)
            bean.list?.let { mAdapter.addData(it) }
        }
        page++
        //是否有更多数据
        refreshStateModel.setNoHasMore(bean.list?.size ?: 0 < pageSize)
    }

    /**
     * 数据请求失败
     */
    private fun failData() {
        if (page == 1) {
            showServiceError()
            refreshStateModel.finishRefresh(false)
        } else {
            refreshStateModel.finishLoadMore(false)
        }
    }

    /**
     * 刷新
     */
    fun refreshData() {
        page = 1
        refreshStateModel.setNoHasMore(false)
        getData(memberId)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++page
        getData(memberId)
    }

    /**
     * item点击事件
     */
    fun itemClick(view: View, bean: ListenSheetBean) {
        getActivity(view.context)?.let {
            clickBean.set(bean)
            val hasMap = getHasMap()
            hasMap[SHEET_ID] = bean.sheet_id.toString()

            startActivityForResult(
                ListenMySheetDetailActivity::class.java, hasMap,
                ListenMySheetDetailActivity.LISTEN_SHEET_DETAIL_REQUEST_CODE
            )
        }
    }

    /**
     * 删除听单回调
     */
    fun remove(sheetId: String) {
        clickBean.get()?.let {
            if (it.sheet_id.toString() == sheetId) {
                mAdapter.remove(it)
                showTip("编辑成功")
            }
        }
    }

    /**
     * 编辑听单回调
     */
    fun changeData(sheetId: String, sheetName: String) {
        clickBean.get()?.let {
            if (it.sheet_id.toString() == sheetId) {
                val indexOf = mAdapter.data.indexOf(it)
                if (indexOf != -1) {
                    mAdapter.data[indexOf].sheet_name = sheetName
                    mAdapter.notifyItemChanged(indexOf)
                }
            }
        }
    }
}