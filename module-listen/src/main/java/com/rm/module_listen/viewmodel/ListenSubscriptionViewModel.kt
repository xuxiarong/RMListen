package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.repository.ListenSubscriptionRepository
import com.rm.module_listen.bean.SubscriptionListBean

class ListenSubscriptionViewModel(private val repository: ListenSubscriptionRepository) :
    BaseVMViewModel() {

    val mDialog by lazy {
        CommBottomDialog()
    }

    //订阅数据源
    val data = MutableLiveData<MutableList<SubscriptionListBean>>()

    //记录当前点击的实体对象
    val subscriptionData = ObservableField<SubscriptionListBean>()

    //item点击事件闭包，提供外部调用
    var itemClick: (SubscriptionListBean) -> Unit = {}

    //更多按钮闭包
    var itemChildMoreClick: (SubscriptionListBean) -> Unit = {}


    /**
     * item点击事件
     */
    fun itemClickFun(bookBean: SubscriptionListBean) {
        itemClick(bookBean)
    }

    /**
     * 更多点击事件
     */
    fun itemChildMoreClickFun(bookBean: SubscriptionListBean) {
        itemChildMoreClick(bookBean)
    }

    /**
     * 发送请求获取数据
     */
    fun getData() {
        showLoading()
        launchOnIO {
            repository.getSubscriptionList().checkResult(
                onSuccess = {
                    showContentView()
                    data.value = it
                },
                onError = {
                    showContentView()
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
        subscriptionData.get()?.let {
            if (it.is_top == 1) {
                cancelTop(it.audio_id)
            } else {
                setTop(it.audio_id)
            }

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
        showLoading()
        launchOnIO {
            repository.unsubscribe(subscriptionData.get()!!.audio_id.toString()).checkResult(
                onSuccess = {
                    showContentView()
                    mDialog.dismiss()
                    DLog.i("------>", "取消订阅成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------>", "取消订阅失败   $it")
                }
            )
        }
    }

    /**
     * 置顶
     */
    private fun setTop(audioId: Long?) {
        showLoading()
        launchOnIO {
            repository.setTop(audioId.toString()).checkResult(
                onSuccess = {
                    mDialog.dismiss()
                    showContentView()
                    DLog.i("------>", "置顶成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------>", "置顶失败   $it")
                }
            )
        }
    }

    /**
     * 取消置顶
     */
    private fun cancelTop(audioId: Long?) {
        showLoading()
        launchOnIO {
            repository.cancelTop(audioId.toString()).checkResult(
                onSuccess = {
                    mDialog.dismiss()
                    showContentView()
                    DLog.i("------>", "置顶成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------>", "置顶失败   $it")
                }
            )
        }
    }


}