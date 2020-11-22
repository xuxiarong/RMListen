package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity
import com.rm.module_mine.bean.MineMemberFansDetailBean
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
            BR.followViewModel,
            BR.followItem
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
                    showTip("关注成功")
                },
                onError = {
                    showContentView()
                    DLog.i("--->", "$it")
                    showTip("$it", R.color.business_color_ff5e5e)

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
                    showTip("取消关注成功")
                },
                onError = {
                    showContentView()
                    DLog.i("--->", "$it")
                    showTip("$it", R.color.business_color_ff5e5e)

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
        showContentView()
        if (followPage == 1) {
            refreshStatusModel.finishRefresh(false)
            showServiceError()
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
        followPage++
        refreshStatusModel.setNoHasMore(followAdapter.data.size >= bean.total || bean.list.size < pageSize)
    }


    /**
     * 刷新数据
     */
    fun refreshData() {
        followPage = 1
        refreshStatusModel.setNoHasMore(false)
        mineMemberFollowList()
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData() {
        mineMemberFollowList()
    }

    /**
     * item点击事件
     */
    fun clickItemFun(context: Context, bean: MineMemberFollowDetailBean) {
        MineMemberActivity.newInstance(context, bean.member_id)
    }

    /**
     * item关注点击事件
     */
    fun clickItemFollowFun(context: Context, bean: MineMemberFollowDetailBean) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                if (bean.is_follow == 1) {
                    showDialog(context, bean)
                } else {
                    attentionAnchor(bean)
                }
            } else {
                getActivity(context)?.let { quicklyLogin(it) }
            }
        }
    }

    private fun showDialog(context: Context, bean: MineMemberFollowDetailBean) {
        getActivity(context)?.let { activity ->
            TipsFragmentDialog().apply {
                titleText = context.String(R.string.business_tips)
                contentText = context.String(R.string.business_sure_cancel_attention)
                leftBtnText = context.String(R.string.business_cancel)
                rightBtnText = context.String(R.string.business_sure)
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    unAttentionAnchor(bean)
                    dismiss()
                }
            }.show(activity)
        }
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(this, it, loginSuccess = {
                refreshData()
            })
    }
}