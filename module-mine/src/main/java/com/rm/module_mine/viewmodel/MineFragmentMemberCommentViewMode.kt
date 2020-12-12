package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.bean.MineMemberListBean
import com.rm.module_mine.bean.MineMemberListDetailBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineFragmentMemberCommentViewMode(private val repository: MineRepository) :
    BaseVMViewModel() {

    var memberId = ""
    var memberType = 0

    //每次价值数据的条数
    private var pageSize = 12

    //当前的页码
    private var mPage = 1

    val refreshStateModel = SmartRefreshLayoutStatusModel()

    val contentRvId=R.id.mine_member_comment_recycler_view

    val isShowNoData = ObservableField<Boolean>(false)

    val commentAdapter by lazy {
        CommonBindVMAdapter<MineMemberListDetailBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_comment,
            BR.commentViewModel,
            BR.commentItem
        )
    }

    fun getData() {
        if (memberType == 1) {
            mineMemberCommentList(memberId)
        } else {
            mineAnchorCommentList(memberId)
        }
    }

    /**
     * 用户详情页评论
     * @param memberId 用户ID
     */
    private fun mineMemberCommentList(memberId: String) {
        launchOnIO {
            repository.mineMemberCommentList(memberId, mPage, pageSize).checkResult(
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
     * 主播详情页评论
     * @param anchorId 主播id
     */
    private fun mineAnchorCommentList(anchorId: String) {
        launchOnIO {
            repository.mineAnchorCommentList(anchorId, mPage, pageSize).checkResult(
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
     * 处理请求成功的数据
     */
    private fun processSuccessData(bean: MineMemberListBean) {
        if (mPage == 1) {
            refreshStateModel.finishRefresh(true)
            if (bean.list.size > 0) {
                commentAdapter.setList(bean.list)
                isShowNoData.set(false)
            } else {
                isShowNoData.set(true)
            }

        } else {
            commentAdapter.addData(bean.list)
            refreshStateModel.finishLoadMore(true)
        }
        //是否有更多数据
        refreshStateModel.setNoHasMore(bean.list.size < pageSize)
    }


    /**
     * 处理失败的数据
     */
    private fun processFailureData(msg: String?) {
        if (mPage == 1) {
            refreshStateModel.finishRefresh(false)
            showServiceError()
        } else {
            refreshStateModel.finishLoadMore(false)
            mPage--
        }
        showTip("$msg", R.color.business_color_ff5e5e)
    }

    /**
     * 上拉刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStateModel.setNoHasMore(false)
        getData()
    }

    /**
     * 下拉加载更多
     */
    fun loadMoreData() {
        ++mPage
        getData()
    }

    fun commentBookItemClick(context: Context, audioId: String) {
        RouterHelper.createRouter(HomeService::class.java).startDetailActivity(context, audioId)
    }
}