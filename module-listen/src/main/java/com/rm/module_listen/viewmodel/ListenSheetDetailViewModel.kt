package com.rm.module_listen.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.repository.ListenRepository
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper

class ListenSheetDetailViewModel(private val repository: ListenRepository) :
    BaseVMViewModel() {

    /**
     * 懒加载构建adapter对象
     */
    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.listen_adapter_book,
            BR.viewModel,
            BR.item
        )
    }
    private val mDialog by lazy { CommBottomDialog() }

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    //数据源
    val data = ObservableField<SheetInfoBean>()

    //每页加载的条数
    private val pageSize = 12

    //当前请求数据的页码
    private var mPage = 1

    private lateinit var sheetId: String

    //编辑成功
    var blockSuccess: (String) -> Unit = {}

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStateModel.setNoHasMore(false)
        getSheetInfo(sheetId)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getAudioList()
    }

    /**
     * 获取听单列表
     */
    fun getSheetInfo(sheetId: String) {
        this.sheetId = sheetId
        showLoading()
        launchOnIO {
            repository.getSheetInfo(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    data.set(it)
                    //刷新完成
                    refreshStateModel.finishRefresh(true)
                    //设置新数据源

                    if (it.audio_list.list.size > 0) {
                        mAdapter.setList(it.audio_list.list)
                    } else {
                        showDataEmpty()
                    }
                    //是否有更多数据
                    refreshStateModel.setNoHasMore(it.audio_list.list.size < pageSize)
                    ++mPage
                },

                onError = {
                    showContentView()
                    refreshStateModel.finishRefresh(false)
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 获取音频列表
     */
    private fun getAudioList() {
        launchOnIO {
            repository.getAudioList(mPage, pageSize).checkResult(
                onSuccess = {
                    refreshStateModel.finishLoadMore(true)
                    mAdapter.addData(it.list)
                    refreshStateModel.setNoHasMore(it.list.size < pageSize)
                    ++mPage
                },
                onError = {
                    refreshStateModel.finishLoadMore(false)
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 删除点击事件
     */
    fun dialogSheetDetailDeleteFun() {
        showLoading()
        launchOnIO {
            repository.deleteSheet("${data.get()?.sheet_id}").checkResult(
                onSuccess = {
                    showContentView()
                    deleteSuccess()
                    mDialog.dismiss()
                },
                onError = {
                    showContentView()
                }
            )
        }
    }

    /**
     * 删除成功
     */
    private fun deleteSuccess() {
        val hasMap = getHasMap()
        hasMap[ListenMySheetDetailActivity.SHEET_ID] = sheetId
        setResultAndFinish(ListenMySheetDetailActivity.LISTEN_SHEET_DETAIL_DELETE, hasMap)
    }

    /**
     * 编辑点击事件
     */
    fun dialogSheetDetailEditSheetFun(view: View) {
        data.get()?.let {
            getActivity(view.context)?.let { activity ->
                ListenDialogCreateSheetHelper(activity) {

                }.showEditDialog(
                    it.sheet_id,
                    success = { sheetName ->
                        editSuccess(sheetName)
                        showTip("编辑成功")
                    }
                )
            }
        }
    }

    /**
     * 编辑成功
     */
    private fun editSuccess(sheetName: String) {
        mDialog.dismiss()
        blockSuccess(sheetName)
        val map = getHasMap()
        map[ListenMySheetDetailActivity.SHEET_ID] = sheetId
        map[ListenMySheetDetailActivity.SHEET_NAME] = sheetName
        setResult(ListenMySheetDetailActivity.LISTEN_SHEET_DETAIL_EDIT, map)
    }

    /**
     * 取消点击事件
     */
    fun dialogSheetDetailCancelFun() {
        mDialog.dismiss()
    }


    /**
     * 将音频从听单移除
     */
    fun removeAudioFun(bean: DownloadAudio) {
        showLoading()
        launchOnIO {
            repository.removeAudio("${data.get()?.sheet_id}", bean.audio_id.toString()).checkResult(
                onSuccess = {
                    showContentView()
                    mAdapter.remove(bean)
                    if(mAdapter.data.size <=0){
                        showDataEmpty()
                    }
                    DLog.i("-------->", "移除成功  $it")
                },
                onError = {
                    DLog.i("-------->", "移除失败  $it")
                }
            )
        }
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: DownloadAudio) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bean.audio_id.toString())
    }

    /**
     * 返回点击事件
     */
    fun clickBack() {
        finish()
    }

    /**
     * 更多点击事件
     */
    fun clickMore(view: View) {
        getActivity(view.context)?.let {
            mDialog.showCommonDialog(
                it,
                R.layout.listen_dialog_bottom_sheet_detail,
                this,
                BR.viewModel
            )
        }
    }
}