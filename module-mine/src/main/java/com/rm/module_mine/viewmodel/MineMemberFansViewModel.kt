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
import com.rm.module_mine.bean.MineMemberFansBean
import com.rm.module_mine.bean.MineMemberFansDetailBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineMemberFansViewModel(private val repository: MineRepository) : BaseVMViewModel() {
    //每页加载的条数
    private val pageSize = 12

    //当前的页码
    private var fansPage = 1

    //主播/用户id
    var memberId = ""

    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //当前用户信息  用来判断当前用户是否是自己  控制是否显示关注按钮
    val userInfo = loginUser

    val fanAdapter by lazy {
        CommonBindVMAdapter<MineMemberFansDetailBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_fans,
            BR.fansViewModel,
            BR.fansItem
        )
    }

    /**
     * 粉丝列表
     */
    fun mineMemberFansList() {
        launchOnIO {
            repository.mineMemberFansList(memberId, fansPage, pageSize).checkResult(
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
    private fun attentionAnchor(bean: MineMemberFansDetailBean) {
        showLoading()
        launchOnIO {
            repository.attentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    changeState(bean, 1)
                    showTip("关注成功")
                },
                onError = {
                    showContentView()
                    DLog.i("--->", "$it")
                    showTip("$it",R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 取消关注主播
     */
    private fun unAttentionAnchor(bean: MineMemberFansDetailBean) {
        showLoading()
        launchOnIO {
            repository.unAttentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    changeState(bean, 0)
                    showTip("取消关注成功")
                },
                onError = {
                    DLog.i("--->", "$it")
                    showContentView()
                    showTip("$it",R.color.business_color_ff5e5e)

                })
        }
    }

    /**
     * 刷新修改item
     */
    private fun changeState(bean: MineMemberFansDetailBean, isFollow: Int) {
        val indexOf = fanAdapter.data.indexOf(bean)
        if (indexOf != -1) {
            bean.is_follow = isFollow
            fanAdapter.notifyItemChanged(indexOf)
        }
    }

    /**
     * 处理失败数据
     */
    private fun processFailureData(msg: String?) {
        if (fansPage == 1) {
            refreshStatusModel.finishRefresh(false)
        } else {
            refreshStatusModel.finishLoadMore(false)
        }
        DLog.i("---->", "$msg")
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(bean: MineMemberFansBean) {
        if (fansPage == 1) {
            refreshStatusModel.finishRefresh(true)
            if (bean.list.isNotEmpty()) {
                fanAdapter.setList(bean.list)
            } else {
                showDataEmpty()
            }
        } else {
            refreshStatusModel.finishLoadMore(true)
            fanAdapter.addData(bean.list)
        }
        refreshStatusModel.setHasMore(bean.list.size >= pageSize)
    }


    /**
     * 刷新数据
     */
    fun refreshData() {
        fansPage = 1
        mineMemberFansList()
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData() {
        ++fansPage
        mineMemberFansList()
    }

    /**
     * item点击事件
     */
    fun clickItemFun() {}

    /**
     * item关注点击事件
     */
    fun clickItemFollowFun(context: Context, bean: MineMemberFansDetailBean) {
        getActivity(context)?.let {
            if (bean.is_follow == 1) {
                unAttentionAnchor(bean)
            } else {
                attentionAnchor(bean)
            }
        }
    }

}