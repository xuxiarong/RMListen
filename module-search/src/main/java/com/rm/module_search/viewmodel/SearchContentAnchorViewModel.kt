package com.rm.module_search.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
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

    val userInfo = loginUser

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

    //页码
    private var mPage = 1

    //每页展示数量
    private var mPageSize = 12


    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        requestData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
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
                    onError = {
                        failData()
                    }
                )
        }
    }

    /**
     * 成功数据
     */
    private fun successData(bean: SearchResultBean) {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(true)
        } else {
            refreshStateMode.finishLoadMore(true)
        }
        refreshStateMode.setHasMore(bean.member_list.isNotEmpty())

        if (mPage == 1) {
            if (bean.member_list.isEmpty()) {
                showDataEmpty()
            } else {
                anchorAdapter.setList(bean.member_list)
            }
        } else {
            bean.member_list.let { anchorAdapter.addData(it) }
        }
    }

    /**
     * 失败数据
     */
    private fun failData() {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(false)
        } else {
            refreshStateMode.finishLoadMore(false)
        }
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
                    showToast("关注成功")
                },
                onError = {
                    showContentView()
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
                    showToast("取消关注成功")
                },
                onError = {
                    showContentView()
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
                if (bean.is_follow == 1L) {
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
            .quicklyLogin(this, it, loginSuccess = {
//                intDetailInfo(audioId.get()!!)
            })
    }
}