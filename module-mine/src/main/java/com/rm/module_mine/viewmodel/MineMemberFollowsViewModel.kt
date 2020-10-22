package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.loginUser
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.bean.MineMemberFollowBean
import com.rm.module_mine.bean.MineMemberFollowDetailBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineMemberFollowsViewModel(private val repository: MineRepository) : BaseVMViewModel() {
    //每页加载的条数
    private val pageSize = 12

    //当前的页码
    private var followPage = 1

    //主播/用户id
    var memberId = ""

    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //当前用户信息  用来判断当前用户是否是自己  控制是否显示关注按钮
    val userInfo = loginUser

    val followAdapter by lazy {
        CommonBindVMAdapter<MineMemberFollowDetailBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_follow,
            BR.fansViewModel,
            BR.followViewModel
        )
    }


    /**
     *  关注列表
     */
    fun mineMemberFollowList() {
        launchOnIO {
            repository.mineMemberFollowList(memberId, followPage, pageSize).checkResult(
                onSuccess = {
                    processSuccessData(it)
                },
                onError = {
                    processFailureData(it)
                }
            )
        }
    }

    /**
     * 关注主播
     */
    private fun attentionAnchor(bean: MineMemberFollowDetailBean) {
        showLoading()
        launchOnIO {
            repository.attentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    changeState(bean, 1)
                    showToast("关注成功")
                },
                onError = {
                    showContentView()
                    DLog.i("--->", "$it")
                })
        }
    }

    /**
     * 取消关注主播
     */
    private fun unAttentionAnchor(bean: MineMemberFollowDetailBean) {
        showLoading()
        launchOnIO {
            repository.unAttentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    changeState(bean, 0)
                    showToast("取消关注成功")
                },
                onError = {
                    DLog.i("--->", "$it")
                    showContentView()
                })
        }
    }

    /**
     * 刷新修改item
     */
    private fun changeState(bean: MineMemberFollowDetailBean, isFollow: Int) {
        val indexOf = followAdapter.data.indexOf(bean)
        if (indexOf != -1) {
            bean.is_follow = isFollow
            followAdapter.notifyItemChanged(indexOf)
        }
    }

    /**
     * 处理失败数据
     */
    private fun processFailureData(msg: String?) {
        if (followPage == 1) {
            refreshStatusModel.finishRefresh(false)
        } else {
            refreshStatusModel.finishLoadMore(false)
        }
        DLog.i("---->", "$msg")
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(bean: MineMemberFollowBean) {
        if (followPage == 1) {
            refreshStatusModel.finishRefresh(true)
            if (bean.list.isNotEmpty()) {
                followAdapter.setList(bean.list)
            } else {
                showDataEmpty()
            }
        } else {
            refreshStatusModel.finishLoadMore(true)
            followAdapter.addData(bean.list)
        }
        refreshStatusModel.setHasMore(bean.list.size >= pageSize)
    }


    /**
     * 刷新数据
     */
    fun refreshData() {
        followPage = 1
        mineMemberFollowList()
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData() {
        ++followPage
        mineMemberFollowList()
    }

    /**
     * item点击事件
     */
    fun clickItemFun() {}

    /**
     * item关注点击事件
     */
    fun clickItemFollowFun(context: Context, bean: MineMemberFollowDetailBean) {
        getActivity(context)?.let {
            if (bean.is_follow == 1) {
                unAttentionAnchor(bean)
            } else {
                attentionAnchor(bean)
            }
        }
    }

}