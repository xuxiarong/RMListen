package com.rm.module_search.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_search.*
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentAnchorViewModel(private val repository: SearchRepository) : BaseVMViewModel() {

    val keyword = searchKeyword

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_anchor,
            BR.viewModel,
            BR.item
        )
    }


    val refreshStateMode = SmartRefreshLayoutStatusModel()
    val contentRvId = R.id.search_adapter_content_rv

    //页码
    var mPage = 1

    //每页展示数量
    private val mPageSize = 12


    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStateMode.setNoHasMore(false)
        requestData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        requestData()
    }

    /**
     * 请求数据
     */
    private fun requestData() {
        launchOnIO {
            repository.searchResult(searchKeyword.get()!!, REQUEST_TYPE_MEMBER, mPage, mPageSize)
                .checkResult(
                    onSuccess = {
                        successData(it)
                    },
                    onError = { msg, _ ->
                        failData(msg)
                    }
                )
        }
    }


    /**
     * 成功数据
     */
    fun successData(bean: SearchResultBean) {
        showContentView()
        if (mPage == 1) {
            refreshStateMode.finishRefresh(true)
        } else {
            refreshStateMode.finishLoadMore(true)
        }
        if (mPage == 1) {
            if (bean.member_list.isEmpty()) {
                showSearchDataEmpty()
            } else {
                anchorAdapter.setList(bean.member_list)
            }
        } else {
            bean.member_list.let { anchorAdapter.addData(it) }
        }
        if (anchorAdapter.data.size >= bean.member) {
            refreshStateMode.setNoHasMore(true)
        } else {
            ++mPage
        }
    }

    /**
     * 失败数据
     */
    private fun failData(msg: String?) {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(false)
        } else {
            refreshStateMode.finishLoadMore(false)
        }
        showTip("$msg", R.color.business_color_ff5e5e)
    }

    /**
     * 关注主播
     */
    private fun attentionAnchor(bean: MemberBean) {
        showLoading()
        launchOnIO {
            repository.attentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    val indexOf = anchorAdapter.data.indexOf(bean)
                    bean.is_follow = 1
                    anchorAdapter.notifyItemChanged(indexOf)
                    showTip("关注成功")
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }

    /**
     * 取消关注主播
     */
    private fun unAttentionAnchor(bean: MemberBean) {
        showLoading()
        launchOnIO {
            repository.unAttentionAnchor(bean.member_id).checkResult(
                onSuccess = {
                    showContentView()
                    val indexOf = anchorAdapter.data.indexOf(bean)
                    bean.is_follow = 0
                    anchorAdapter.notifyItemChanged(indexOf)
                    showTip("取消关注成功")
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                })
        }
    }


    /**
     *   item点击事件
     */
    fun itemClickFun(context: Context, bean: MemberBean) {
        RouterHelper.createRouter(MineService::class.java).toMineMember(context, bean.member_id)
    }

    /**
     * 关注点击事件
     */
    fun clickAttentionFun(context: Context, bean: MemberBean) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                if (bean.is_follow == 1) {
                    unAttentionAnchor(bean)
                } else {
                    attentionAnchor(bean)
                }
            }
        }
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(it, loginSuccess = {})
    }
}