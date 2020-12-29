package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.repository.ListenRepository

class ListenSheetMyListViewModel(private val repository: ListenRepository) : BaseVMViewModel() {

    val refreshStateModel = SmartRefreshLayoutStatusModel()
    val contentRvId = R.id.listen_sheet_my_list_recycler_view

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
        showLoading()
        launchOnIO {
            repository.getMyList(page, pageSize, memberId).checkResult(
                onSuccess = {
                    showContentView()
                    successData(it)
                },
                onError = { it, code ->
                    showContentView()
                    failData("$it", code)
                }
            )
        }
    }

    private fun getMySheetList() {
        showLoading()
        launchOnIO {
            repository.getMyList(page, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    successData(it)
                },
                onError = { it, code ->
                    showContentView()
                    failData("$it", code)
                }
            )
        }
    }

    /**
     * 删除听单
     */
    private fun deleteSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.deleteSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    remove(sheetId)
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it",R.color.business_color_ff5e5e)
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
        //是否有更多数据
        refreshStateModel.setNoHasMore(bean.list?.size ?: 0 < pageSize)
        page++
    }

    /**
     * 数据请求失败
     */
    private fun failData(msg: String, code: Int?) {
        if (page == 1) {
            refreshStateModel.finishRefresh(false)
        } else {
            refreshStateModel.finishLoadMore(false)
        }
        showTip(msg, R.color.business_color_ff5e5e)
//        if (code == CODE_LOGIN_OUT || code == CODE_NOT_LOGIN) {
//            viewModelScope.launch {
//                delay(1000)
//                finish()
//            }
//
//        }
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
        getData(memberId)
    }

    /**
     * item点击事件
     */
    fun itemClick(context: Context, bean: ListenSheetBean) {

        when (bean.pre_deleted_from) {
            "1" -> {
                showTipDialog(context, "该听单因存在违规内容已被系统屏蔽，是否删除", bean.sheet_id.toString())
            }
            "2" -> {
                showTipDialog(context, "该听单已被作者删除，是否删除？", bean.sheet_id.toString())
            }
            else -> {
                if (memberId.isEmpty()) {
                    getActivity(context)?.let {
                        clickBean.set(bean)
                        ListenMySheetDetailActivity.startActivity(it, bean.sheet_id.toString())
                    }
                } else {
                    getActivity(context)?.let {
                        RouterHelper.createRouter(HomeService::class.java)
                            .startHomeSheetDetailActivity(it, bean.sheet_id.toString())
                    }
                }
            }
        }
    }

    private fun showTipDialog(context: Context, content: String, sheetId: String) {
        getActivity(context)?.let {
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.business_tips)
                contentText = content
                rightBtnText = context.String(R.string.business_sure)
                leftBtnText = context.String(R.string.business_cancel)
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    deleteSheet(sheetId)
                    dismiss()
                }
            }.show(it)
        }
    }

    /**
     * 删除听单回调
     */
    fun remove(sheetId: String) {
        clickBean.get()?.let {
            if (it.sheet_id.toString() == sheetId) {
                mAdapter.remove(it)
                showTip("删除成功")
            }

            if (mAdapter.data.size <= 0) {
                showDataEmpty()
            }
        }
    }

    /**
     * 修改听单数据
     */
    fun changeData(sheetId: String, sheetName: String, sheetAudioNum: Int) {
        clickBean.get()?.let {
            if (it.sheet_id.toString() == sheetId) {
                val indexOf = mAdapter.data.indexOf(it)
                if (indexOf != -1) {
                    if (mAdapter.data[indexOf].sheet_name != sheetName) {
                        mAdapter.data[indexOf].sheet_name = sheetName
                    }
                    mAdapter.data[indexOf].num_audio = sheetAudioNum
                    mAdapter.notifyItemChanged(indexOf)
                }
            }
        }
    }

}