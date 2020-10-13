package com.rm.module_listen.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.SubscriptionListBean
import com.rm.module_listen.databinding.ListenDialogBottomSubscriptionBinding
import com.rm.module_listen.repository.ListenSubscriptionRepository

class ListenSubscriptionViewModel(private val repository: ListenSubscriptionRepository) :
    BaseVMViewModel() {

    /**
     * 懒加载创建adapter对象
     */
    val mAdapter by lazy {
        CommonBindVMAdapter<SubscriptionListBean>(
            this,
            mutableListOf(),
            R.layout.listen_adapter_subscription,
            BR.click,
            BR.item
        )
    }

    var refreshStatusModel = SmartRefreshLayoutStatusModel()

    private val mDialog by lazy { CommBottomDialog() }

    //订阅数据源
    val data = ObservableField<MutableList<SubscriptionListBean>>()

    //记录当前点击的实体对象
    private val subscriptionData = ObservableField<SubscriptionListBean>()

    //当前请求的页码
    private var mPage = 1

    //每次请求数据的条数
    private val pageSize = 10

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bookBean: SubscriptionListBean) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bookBean.audio_id.toString())
    }

    /**
     * 更多点击事件
     */
    fun itemChildMoreClickFun(view: View, bookBean: SubscriptionListBean) {
        showBottomDialog(view, bookBean)
    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        getData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        getData()
    }

    /**
     * 发送请求获取数据
     */
    fun getData() {
        launchOnIO {
            repository.getSubscriptionList(mPage, pageSize).checkResult(
                onSuccess = {
                    data.set(it)
                    processSuccessData(it)
                },
                onError = {
                    showServiceError()
                    processFailData()
                }
            )
        }
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(list: MutableList<SubscriptionListBean>) {
        showContentView()
        if (mPage == 1) {
            //刷新完成
            refreshStatusModel.finishRefresh(true)
            mAdapter.setList(list)
        } else {
            //加载更多完成
            refreshStatusModel.finishLoadMore(true)
            mAdapter.addData(list)
        }
        //是否有更多数据
        refreshStatusModel.setHasMore(list.size >= pageSize)
    }

    /**
     * 处理失败数据
     */
    private fun processFailData() {
        if (mPage == 1) {
            refreshStatusModel.finishRefresh(false)
        } else {
            refreshStatusModel.finishLoadMore(false)
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
                    mAdapter.remove(subscriptionData.get()!!)
                },
                onError = {
                    showContentView()
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
                    setTopSuccess()
                },
                onError = {
                    showContentView()
                    showToast(CONTEXT.getString(R.string.listen_set_top_fail))
                }
            )
        }
    }

    /**
     * 置顶成功
     */
    private fun setTopSuccess() {
        showContentView()
        showToast(CONTEXT.getString(R.string.listen_set_top_success))
        subscriptionData.get()?.let {
            mAdapter.remove(it)
            mAdapter.data.add(0, it)
            mAdapter.notifyDataSetChanged()
        }
        mDialog.dismiss()
    }

    /**
     * 取消置顶
     */
    private fun cancelTop(audioId: Long?) {
        showLoading()
        launchOnIO {
            repository.cancelTop(audioId.toString()).checkResult(
                onSuccess = {
                    cancelTopSuccess()
                },
                onError = {
                    showContentView()
                    showToast(CONTEXT.getString(R.string.listen_cancel_top_fail))
                }
            )
        }
    }

    /**
     * 取消置顶成功
     */
    private fun cancelTopSuccess() {
        showContentView()
        showToast(CONTEXT.getString(R.string.listen_cancel_top_success))
        subscriptionData.get()?.let {
            mAdapter.remove(it)
            mAdapter.data.add(mAdapter.data.lastIndex + 1, it)
            mAdapter.notifyDataSetChanged()
        }
        mDialog.dismiss()
    }

    /**
     * 现实底部弹窗
     */
    private fun showBottomDialog(view: View, bean: SubscriptionListBean) {
        subscriptionData.set(bean)
        mDialog.initDialog = {
            val subscriptionBinding =
                mDialog.mDataBind as ListenDialogBottomSubscriptionBinding

            subscriptionBinding.listenDialogBottomSubscriptionTop.text = if (bean.is_top == 0) {
                view.resources.getString(R.string.listen_set_top)
            } else {
                view.resources.getString(R.string.listen_cancel_top)
            }
        }
        getActivity(view.context)?.let {
            mDialog.showCommonDialog(
                it,
                R.layout.listen_dialog_bottom_subscription,
                this,
                BR.viewModel
            )
        }
    }

}