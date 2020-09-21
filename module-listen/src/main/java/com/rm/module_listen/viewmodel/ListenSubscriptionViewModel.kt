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

    //取消订阅
    var dialogUnsubscribe: () -> Unit = {}

    //置顶
    var dialogSetTop: (SubscriptionListBean) -> Unit = {}

    //取消置顶
    var dialogCancelTop: (SubscriptionListBean) -> Unit = {}

    //刷新/加载更多是否成功
    val isRefreshOrLoadComplete = MutableLiveData<Boolean>()


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
    fun getData(page: Int, pageSize: Int) {
        showLoading()
        launchOnIO {
            repository.getSubscriptionList(page, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    data.value = it
                    isRefreshOrLoadComplete.value = true
                },
                onError = {
                    showNetError()
                    isRefreshOrLoadComplete.value = true
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
                    dialogUnsubscribe()
                    DLog.i("------>", "取消订阅成功")
                },
                onError = {
                    showNetError()
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
                    subscriptionData.get()?.let{
                        dialogSetTop(it)
                    }
                    showToast("置顶成功")
                    DLog.i("------>", "置顶成功")
                },
                onError = {
                    showNetError()
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
                    subscriptionData.get()?.let{
                        dialogCancelTop(it)
                    }
                    showToast("取消置顶成功")
                    DLog.i("------>", "取消置顶成功")
                },
                onError = {
                    showNetError()
                    DLog.i("------>", "取消置顶失败   $it")
                }
            )
        }
    }


}