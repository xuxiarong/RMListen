package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.share.ShareManage
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.repository.ListenRepository
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper

class ListenSheetDetailViewModel(private val repository: ListenRepository) : BaseVMViewModel() {

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
    val contentRvId = R.id.listen_sheet_detail_recycler_view

    //数据源
    val data = ObservableField<SheetInfoBean>()
    val nowSheetName = ObservableField<String>()

    //每页加载的条数
    private val pageSize = 12

    //当前请求数据的页码
    private var mPage = 1

    var sheetId: String = ""

    //是否显示没数据
    val showNoData = ObservableField<Boolean>(false)

    //音频书籍数量
    val audioNum = ObservableField(0)

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStateModel.setResetNoMoreData(true)
        getAudioList()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getAudioList()
    }

    /**
     * 获取列表
     */
    fun getSheetInfo() {
        showLoading()
        launchOnIO {
            repository.getSheetInfo(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    audioNum.set(it.num_audio)
                    data.set(it)
                    nowSheetName.set(it.sheet_name)
                },

                onError = { it, _ ->
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
    fun getAudioList() {
        launchOnIO {
            repository.getAudioList(mPage, pageSize, sheetId).checkResult(
                onSuccess = {
                    processAudioList(it)
                },
                onError = { it, _ ->
                    if (mPage == 1) {
                        refreshStateModel.finishRefresh(false)
                    } else {
                        refreshStateModel.finishLoadMore(false)
                    }
                })
        }
    }

    private fun processAudioList(bean: AudioListBean) {
        if (mPage == 1) {
            //刷新完成
            refreshStateModel.finishRefresh(true)
            if (bean.list?.size ?: 0 > 0) {
                mAdapter.setList(bean.list)
                showNoData.set(false)
            } else {
                showNoData.set(true)
            }

        } else {
            refreshStateModel.finishLoadMore(true)
            bean.list?.let { audioList ->
                mAdapter.addData(audioList)
            }
        }
        if (mAdapter.data.size >= bean.total || bean.list?.size ?: 0 < pageSize) {
            refreshStateModel.setNoHasMore(true)
        } else {
            ++mPage
        }

    }

    private fun deleteSheet() {
        showLoading()
        launchOnIO {
            repository.deleteSheet("${data.get()?.sheet_id}").checkResult(
                onSuccess = {
                    showContentView()
                    deleteSuccess()
                    mDialog.dismiss()
                },
                onError = { it, _ ->
                    showContentView()
                }
            )
        }
    }

    /**
     * 删除点击事件
     */
    fun dialogSheetDetailDeleteClickFun(context: Context) {
        getActivity(context)?.let {
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.business_tips)
                contentText = "确认删除该听单？"
                leftBtnText = "再想想"
                rightBtnText = "删除"
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                    mDialog.dismiss()
                }
                rightBtnClick = {
                    deleteSheet()
                }
            }.show(it)
        }
    }

    private fun showTipDialog(context: Context, bean: DownloadAudio) {
        getActivity(context)?.let {
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.business_tips)
                contentText = "该书籍已下架，是否移除？"
                rightBtnText = "移除"
                leftBtnText = "不移除"
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    removeAudioFun(bean)
                    dismiss()
                }
            }.show(it)
        }
    }

    /**
     * 删除成功
     */
    private fun deleteSuccess() {
        val hasMap = getHasMap()
        hasMap[ListenMySheetDetailActivity.SHEET_ID] = sheetId
        hasMap[ListenMySheetDetailActivity.SHEET_AUDIO_NUM] = audioNum.get()!!
        setResultAndFinish(ListenMySheetDetailActivity.LISTEN_SHEET_DETAIL_DELETE, hasMap)
    }

    /**
     * 分享
     */
    fun dialogSheetDetailShareSheetFun(context: Context) {
        getActivity(context)?.let { activity ->
            data.get()?.let {
                ShareManage.shareSheet(activity, it.sheet_id, it.sheet_name)
            }
        }

    }

    /**
     * 编辑点击事件
     */
    fun dialogSheetDetailEditSheetFun(context: Context) {
        data.get()?.let {
            getActivity(context)?.let { activity ->
                mDialog.dismiss()
                ListenDialogCreateSheetHelper(activity) {
                }.showEditDialog(
                    it.sheet_name,
                    it.sheet_id,
                    success = { sheetName ->
                        editSuccess(sheetName)
                        nowSheetName.set(sheetName)
                        it.sheet_name=sheetName
                        data.set(it)
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
        setResult(sheetName)
    }

    private fun setResult(sheetName: String) {
        val map = getHasMap()
        map[ListenMySheetDetailActivity.SHEET_ID] = sheetId
        map[ListenMySheetDetailActivity.SHEET_NAME] = sheetName
        map[ListenMySheetDetailActivity.SHEET_AUDIO_NUM] = audioNum.get()!!
        setResult(ListenMySheetDetailActivity.LISTEN_SHEET_DETAIL, map)
    }

    /**
     * 取消点击事件
     */
    fun dialogSheetDetailCancelClickFun() {
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
                    audioNum.set(audioNum.get()!! - 1)
                    if (mAdapter.data.size <= 0) {
                        showNoData.set(true)
                    }
                    setResult(data.get()?.sheet_name ?: "")
                },
                onError = { it, _ ->
                    DLog.i("-------->", "移除失败  $it")
                }
            )
        }
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: DownloadAudio) {
        if (TextUtils.equals(bean.status, "0")) {
            showTipDialog(view.context, bean)
        } else {
            RouterHelper.createRouter(HomeService::class.java)
                .startDetailActivity(view.context, bean.audio_id.toString())
        }

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