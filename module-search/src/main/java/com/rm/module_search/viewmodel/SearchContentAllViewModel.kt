package com.rm.module_search.viewmodel

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_search.*
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.bean.SearchSheetBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentAllViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    val keyword = searchKeyword

    val data = ObservableField<SearchResultBean>()

    //书籍adapter
    val bookAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_books,
            BR.viewModel,
            BR.item
        )
    }

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_anchor,
            BR.viewModel,
            BR.item
        )
    }


    //听单adapter
    val sheetAdapter by lazy {
        CommonBindVMAdapter<SearchSheetBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_sheet,
            BR.viewModel,
            BR.item
        )
    }

    /**
     * 全部-书籍更多点击事件
     */
    fun clickBookMoreFun() {
        curType.postValue(REQUEST_TYPE_AUDIO)
    }

    /**
     * 全部-主播更多点击事件
     */
    fun clickAnchorMoreFun() {
        curType.postValue(REQUEST_TYPE_MEMBER)
    }

    /**
     * 全部-听单更多点击事件
     */
    fun clickSheetMoreFun() {
        curType.postValue(REQUEST_TYPE_SHEET)
    }

    /**
     * 全部-书籍点击事件
     */
    fun clickBookFun(view: View, bean: DownloadAudio) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bean.audio_id.toString())
    }

    /**
     * 全部-主播点击事件
     */
    fun clickAnchorFun(context: Context, bean: MemberBean) {
        RouterHelper.createRouter(MineService::class.java).toMineMember(context, bean.member_id)
    }

    /**
     * 全部-听单点击事件
     */
    fun clickSheetFun(view: View, bean: SearchSheetBean) {
        getActivity(view.context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id)
        }
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
                onError = {
                    showContentView()
                    showTip("$it",R.color.business_color_ff5e5e)
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
                onError = {
                    showContentView()
                    showTip("$it",R.color.business_color_ff5e5e)
                })
        }
    }

}