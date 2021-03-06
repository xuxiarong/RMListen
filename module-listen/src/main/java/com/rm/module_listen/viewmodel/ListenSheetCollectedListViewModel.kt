package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetCollectedListAdapter
import com.rm.module_listen.bean.SheetFavorAudioBean
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
                onError = { _, _ ->
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
                onError = { _, _ ->
                    failData()
                }
            )
        }
    }

    /**
     * 听单取消收藏
     */
    private fun unFavoriteSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.listenUnFavoriteSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    removeIndex(sheetId)
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
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
                showDataEmpty(CONTEXT.getString(R.string.listen_not_collect_now))
            }
        } else {
            //加载更多完成
            refreshStateModel.finishLoadMore(true)
            mAdapter.addData(bean.list)
        }
        //是否有更多数据
        refreshStateModel.setNoHasMore(bean.list.size < pageSize)
        mPage++
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
        refreshStateModel.setResetNoMoreData(true)
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
    fun itemClickFun(context: Context, bean: SheetFavorDataBean) {
        clickBean = bean
        when (bean.pre_deleted_from) {
            1 -> {
                showTipDialog(context, context.getString(R.string.listen_content_ill), bean.sheet_id ?: "")
            }
            2 -> {
                showTipDialog(context, context.getString(R.string.listen_delete_and_not_collect), bean.sheet_id ?: "")
            }
            else -> {
                getActivity(context)?.let {
                    RouterHelper.createRouter(HomeService::class.java)
                        .startHomeSheetDetailActivity(it, bean.sheet_id ?: "")
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
                    unFavoriteSheet(sheetId)
                    dismiss()
                }
            }.show(it)
        }
    }


    /**
     * 子view item点击事件
     */
    fun itemChildClickFun(view: View, bean: SheetFavorAudioBean) {
        if (bean.status == 0) {
            showTip(CONTEXT.getString(R.string.listen_content_offline), R.color.business_color_ff5e5e)
        } else {
            getActivity(view.context)?.let {
                RouterHelper.createRouter(HomeService::class.java)
                    .startDetailActivity(it, bean.audio_id.toString())
            }
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

        if (mAdapter.data.size <= 0) {
            showDataEmpty(CONTEXT.getString(R.string.listen_not_collect_now))
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