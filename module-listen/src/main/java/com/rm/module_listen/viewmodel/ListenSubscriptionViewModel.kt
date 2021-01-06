package com.rm.module_listen.viewmodel

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.share.ShareManage
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSubscriptionAdapter
import com.rm.module_listen.bean.ListenSubscriptionListBean
import com.rm.module_listen.databinding.ListenDialogBottomSubscriptionBinding
import com.rm.module_listen.repository.ListenRepository

class ListenSubscriptionViewModel(private val repository: ListenRepository) :
    BaseVMViewModel() {

    /**
     * 懒加载创建adapter对象
     */
    val mAdapter by lazy {
        ListenSubscriptionAdapter(this, BR.click, BR.item)
    }

    var refreshStatusModel = SmartRefreshLayoutStatusModel()
    val contentRvId = R.id.listen_subscription_recycler_view

    private val mDialog by lazy { CommBottomDialog() }

    //订阅数据源
    val data = ObservableField<MutableList<ListenSubscriptionListBean>>()

    //记录当前点击的实体对象
    val subscriptionData = ObservableField<ListenSubscriptionListBean>()

    //当前请求的页码
    private var mPage = 1

    //每次请求数据的条数
    private val pageSize = 12

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bookBeanListen: ListenSubscriptionListBean) {
        subsRedReport(bookBeanListen)
        if (bookBeanListen.audio_status == 0) {
            showTipDialog(view.context, bookBeanListen)
        } else {
            RouterHelper.createRouter(HomeService::class.java)
                .startDetailActivity(view.context, bookBeanListen.audio_id.toString())
        }
    }

    private fun showTipDialog(context: Context, bean: ListenSubscriptionListBean) {
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
                    dialogUnsubscribeFun(bean)
                    dismiss()
                }
            }.show(it)
        }
    }

    /**
     * 更多点击事件
     */
    fun itemChildMoreClickFun(view: View, bookBeanListen: ListenSubscriptionListBean) {
        showBottomDialog(view, bookBeanListen)
    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        topSize = 0
        refreshStatusModel.setNoHasMore(false)
        getData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getData()
    }

    /**
     * 发送请求获取数据
     */
    fun getData() {
        launchOnIO {
            repository.getSubscriptionList(mPage, pageSize).checkResult(
                onSuccess = {
                    processSuccessData(it)
                },
                onError = { it, _ ->
                    processFailData()
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     *订阅红点上报
     */
    private fun subsRedReport(bookBean: ListenSubscriptionListBean) {
        launchOnIO {
            repository.listenSubsRedReport(bookBean.audio_id.toString())
                .checkResult(
                    onSuccess = {
                        val indexOf = mAdapter.data.indexOf(bookBean)
                        if (indexOf != -1) {
                            mAdapter.data[indexOf].unread = 0
                            mAdapter.notifyItemChanged(indexOf)
                        }
                    },
                    onError = { msg, _ ->
                    })
        }
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(listListen: MutableList<ListenSubscriptionListBean>) {
        showContentView()
        if (mPage == 1) {
            //刷新完成
            refreshStatusModel.finishRefresh(true)
            setTop(listListen)
            if (listListen.size > 0) {
                mAdapter.setList(listListen)
            } else {
                showDataEmpty()
            }
        } else {
            //加载更多完成
            refreshStatusModel.finishLoadMore(true)
            setTop(listListen)
            mAdapter.addData(listListen)
        }
        //是否有更多数据
        refreshStatusModel.setNoHasMore(listListen.size < pageSize)
        ++mPage
    }

    private var topSize = 0
    private fun setTop(listListen: MutableList<ListenSubscriptionListBean>) {
        listListen.forEach {
            if (it.is_top == 1) {
                topSize++
            }
        }
        mAdapter.setTopSize(topSize)
    }

    /**
     * 处理失败数据
     */
    private fun processFailData() {
        showContentView()
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
    fun dialogShareFun(context: Context) {
        getActivity(context)?.let { activity ->
            subscriptionData.get()?.let {
                ShareManage.shareAudio(activity, it.audio_id.toString(), it.audio_name.toString())
            }
            mDialog.dismiss()
        }
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
    fun dialogUnsubscribeFun(bean: ListenSubscriptionListBean?) {
        bean?.let { subBean ->
            showLoading()
            launchOnIO {
                repository.unsubscribe(subBean.audio_id.toString()).checkResult(
                    onSuccess = {
                        showContentView()
                        mDialog.dismiss()
                        if (subBean.is_top == 1) {
                            mAdapter.setTopSize(--topSize)
                        }
                        mAdapter.remove(subBean)
                        if (mAdapter.data.size <= 0) {
                            showDataEmpty()
                        }
                        BusinessInsertManager.doInsertKeyAndAudio(
                            BusinessInsertConstance.INSERT_TYPE_AUDIO_UNSUBSCRIBED,
                            subBean.audio_id.toString()
                        )
                    },
                    onError = { it, _ ->
                        showContentView()
                    }
                )
            }
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
                onError = { it, _ ->
                    showContentView()
//                    showTip(CONTEXT.getString(R.string.listen_set_top_fail))
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 置顶成功
     */
    private fun setTopSuccess() {
        showContentView()
        showTip(CONTEXT.getString(R.string.listen_set_top_success))
        subscriptionData.get()?.let {
            mAdapter.remove(it)
            it.is_top = 1
            mAdapter.data.add(0, it)
            mAdapter.setTopSize(++topSize)
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
                onError = { it, _ ->
                    showContentView()
//                    showToast(CONTEXT.getString(R.string.listen_cancel_top_fail))
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 取消置顶成功
     */
    private fun cancelTopSuccess() {
        showContentView()
        showTip(CONTEXT.getString(R.string.listen_cancel_top_success))
        subscriptionData.get()?.let {
            mAdapter.remove(it)
            it.is_top = 0
            mAdapter.data.add(mAdapter.data.lastIndex + 1, it)
            mAdapter.setTopSize(--topSize)
            mAdapter.notifyDataSetChanged()
        }
        mDialog.dismiss()
    }

    /**
     * 现实底部弹窗
     */
    private fun showBottomDialog(view: View, beanListen: ListenSubscriptionListBean) {
        subscriptionData.set(beanListen)
        mDialog.initDialog = {
            val subscriptionBinding =
                mDialog.mDataBind as ListenDialogBottomSubscriptionBinding

            subscriptionBinding.listenDialogBottomSubscriptionTop.text =
                if (beanListen.is_top == 0) {
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