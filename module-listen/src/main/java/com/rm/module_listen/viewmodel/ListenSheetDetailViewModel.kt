package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_listen.repository.ListenSheetDetailRepository

class ListenSheetDetailViewModel(private val repository: ListenSheetDetailRepository) :
    BaseVMViewModel() {


    val mDialog by lazy { CommBottomDialog() }

    //数据源
    val data = MutableLiveData<SheetInfoBean>()

    //删除
    val deleteQuery = MutableLiveData<Boolean>(false)

    //编辑成功回调
    var editSheetClick: (SheetInfoBean) -> Unit = {}

    //音频移除成功
    var removeAudio: (AudioBean) -> Unit = {}

    /**
     * 获取听单列表
     */
    fun getData(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.getSheetDetail(sheetId).checkResult(
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

    /**
     * 编辑点击事件
     */
    fun dialogSheetDetailEditSheetFun() {
        data.value?.let {
            editSheetClick(it)
        }
    }

    /**
     * 删除点击事件
     */
    fun dialogSheetDetailDeleteFun() {
        showLoading()
        launchOnIO {
            repository.deleteSheet("${data.value?.sheet_id}").checkResult(
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
        DLog.i("---------->","听单移除")
        showLoading()
        launchOnIO {
            repository.removeAudio("${data.value?.sheet_id}", bean.audio_id).checkResult(
                onSuccess = {
                    showContentView()
                    removeAudio(bean)
                    DLog.i("-------->", "移除成功  $it")
                },
                onError = {
                    showContentView()
                    DLog.i("-------->", "移除失败  $it")
                }
            )
        }
    }

}