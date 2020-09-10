package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.repository.ListenSubscriptionRepository
import com.rm.module_listen.bean.SubscriptionListBean

class ListenSubscriptionViewModel(private val repository: ListenSubscriptionRepository) :
    BaseVMViewModel() {

    val mDialog by lazy {
        CommBottomDialog()
    }

    val data = MutableLiveData<MutableList<SubscriptionListBean>>()
    var itemClick: (SubscriptionListBean) -> Unit = {}
    var itemChildMoreClick: (SubscriptionListBean) -> Unit = {}
    private var audioId: Long? = null

    fun itemClickFun(bookBean: SubscriptionListBean) {
        itemClick(bookBean)
    }

    fun itemChildMoreClickFun(bookBean: SubscriptionListBean) {
        audioId = bookBean.audio_id
        itemChildMoreClick(bookBean)
    }

    fun getData() {
        launchOnIO {
            repository.getSubscriptionList().checkResult(
                onSuccess = {
                    data.value = it
                },
                onError = {
                    DLog.i("------->", "$it")
                }
            )
        }
    }

    /**
     * dialog 取消
     */
    fun dialogCancelFun() {
        mDialog.dismiss()
    }

    /**
     * dialog 置顶
     */
    fun dialogSetTopFun() {
        mDialog.dismiss()
        launchOnIO {
            repository.setTop(audioId.toString()).checkResult(
                onSuccess = {
                    DLog.i("------>", "置顶成功")
                },
                onError = {
                    DLog.i("------>", "置顶失败   $it")
                }
            )
        }
    }

    /**
     * dialog 分享
     */
    fun dialogShareFun() {
        mDialog.dismiss()
    }

    /**
     * dialog 找相似
     */
    fun dialogFindSimilarFun() {
        mDialog.dismiss()
    }

    /**
     * dialog 取消订阅
     */
    fun dialogUnsubscribeFun() {
        mDialog.dismiss()
        launchOnIO {
            repository.unsubscribe(audioId.toString()).checkResult(
                onSuccess = {
                    DLog.i("------>", "取消订阅成功")
                },
                onError = {
                    DLog.i("------>", "取消订阅失败   $it")
                }
            )
        }
    }

}