package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetDetailBean
import com.rm.module_listen.bean.ListenSheetDetailDataBean
import com.rm.module_listen.repository.ListenSheetDetailRepository

class ListenSheetDetailViewModel(private val repository: ListenSheetDetailRepository) :
    BaseVMViewModel() {


    val mDialog by lazy { CommBottomDialog() }

    //数据源
    val data = MutableLiveData<ListenSheetDetailBean>()

    //头部详情信息 由上级页面传递过来
    val sheetBean = MutableLiveData<ListenSheetBean>()

    //删除操作
    val deleteQuery = MutableLiveData<Boolean>(false)

    var editSheetClick: (ListenSheetBean) -> Unit = {}

    /**
     * 获取听单列表
     */
    fun getData(sheetId: String, page: Int) {
        showLoading()
        launchOnIO {
            repository.getSheetDetail(sheetId, page).checkResult(
                onSuccess = {
                    showContentView()
                    data.value = it
                },
                onError = {
                    showContentView()
                    DLog.i("----->", "$it")
                }
            )
        }
    }

    //编辑点击事件
    fun dialogSheetDetailEditSheetFun() {
        sheetBean.value?.let{
            editSheetClick(it)
        }
    }

    //删除点击事件
    fun dialogSheetDetailDeleteFun() {
        showLoading()
        launchOnIO {
            repository.deleteSheet("${sheetBean.value?.sheet_id}").checkResult(
                onSuccess = {
                    showContentView()
                    DLog.i("----->", "删除成功")
                    deleteQuery.value = true
                    mDialog.dismiss()
                },
                onError = {
                    showContentView()
                    deleteQuery.value = false
                    DLog.i("----->", "$it")
                }
            )
        }
    }

    //取消点击事件
    fun dialogSheetDetailCancelFun() {
        mDialog.dismiss()
    }

    //删除事件
    fun deleteItem(bean: ListenSheetDetailDataBean){

    }

}