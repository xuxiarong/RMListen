package com.rm.module_listen.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.databinding.ListenHeaderSheetDetailBinding
import com.rm.module_listen.repository.ListenSheetDetailRepository
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper

class ListenSheetDetailViewModel(private val repository: ListenSheetDetailRepository) :
    BaseVMViewModel() {

    /**
     * 懒加载构建adapter对象
     */
    val mAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            this,
            mutableListOf(),
            R.layout.listen_adapter_book,
            BR.viewModel,
            BR.item
        )
    }

    //头部dataBinding对象
    var dataBinding: ListenHeaderSheetDetailBinding? = null

    private val mDialog by lazy { CommBottomDialog() }

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    //数据源
    val data = ObservableField<SheetInfoBean>()

    //每页加载的条数
    private val pageSize = 10

    //当前请求数据的页码
    private var mPage = 1

    private lateinit var sheetId: String

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        getSheetInfo(sheetId)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        getAudioList()
    }

    /**
     * 获取听单列表
     */
    fun getSheetInfo(sheetId: String) {
        this.sheetId = sheetId
        launchOnIO {
            repository.getSheetInfo(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    data.set(it)

                    //头部数据绑定
                    dataBinding?.let { dataBinding ->
                        dataBinding.root.visibility = View.VISIBLE
                        dataBinding.setVariable(BR.item, it)
                    }

                    //刷新完成
                    refreshStateModel.finishRefresh(true)
                    //设置新数据源
                    mAdapter.setList(it.audio_list)

                    //是否有更多数据
                    refreshStateModel.setHasMore(it.audio_list?.size ?: 0 > pageSize)
                },

                onError = {
                    showContentView()
                    refreshStateModel.finishRefresh(false)
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

                    refreshStateModel.setHasMore(it.list.size > pageSize)
                },
                onError = {
                    refreshStateModel.finishLoadMore(false)
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
                ListenDialogCreateSheetHelper(this, activity).setTitle(CONTEXT.getString(R.string.listen_edit_sheet))
                    .showEditDialog(
                        it.sheet_id,
                        success = { sheetName ->
                            editSuccess(sheetName)
                        }
                    )
            }
        }
    }

    /**
     * 编辑成功
     */
    private fun editSuccess(sheetName: String) {
        dataBinding?.listenSheetDetailDescription?.text = sheetName

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
    fun removeAudioFun(bean: AudioBean) {
        showLoading()
        launchOnIO {
            repository.removeAudio("${data.get()?.sheet_id}", bean.audio_id).checkResult(
                onSuccess = {
                    showContentView()
                    mAdapter.remove(bean)
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
    fun itemClickFun(view: View, bean: AudioBean) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bean.audio_id)
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