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
    val contentRvId = R.id.mine_adapter_member_fans

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
                onError = {it,_->
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
                onError = {it,_->
                    showContentView()
                    DLog.i("--->", "$it")
                    showTip("$it", R.color.business_color_ff5e5e)
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
                onError = {it,_->
                    DLog.i("--->", "$it")
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)

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
            showServiceError()
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
        fansPage++
        refreshStatusModel.setNoHasMore(fanAdapter.data.size >= bean.total || bean.list.size < pageSize)
    }


    /**
     * 刷新数据
     */
    fun refreshData() {
        fansPage = 1
        refreshStatusModel.setNoHasMore(false)
        mineMemberFansList()
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData() {
        mineMemberFansList()
    }

    /**
     * item点击事件
     */
    fun clickItemFun(context: Context, bean: MineMemberFansDetailBean) {
        MineMemberActivity.newInstance(context, bean.member_id)
    }

    /**
     * item关注点击事件
     */
    fun clickItemFollowFun(context: Context, bean: MineMemberFansDetailBean) {
        getActivity(context)?.let {
            if (isLogin.get()) {
                if (bean.is_follow == 1) {
                    showDialog(context, bean)
                } else {
                    attentionAnchor(bean)
                }
            } else {
                quicklyLogin(it)
            }
        }
    }

    private fun showDialog(context: Context, bean: MineMemberFansDetailBean) {
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
                fansPage = 1
                refreshStatusModel.setNoHasMore(false)
                mineMemberFansList()
            })
    }
}