package com.rm.module_mine.viewmodel

import android.content.Context
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.bean.MinePublishBean
import com.rm.module_mine.bean.MinePublishDetailBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineMemberReleaseBooksViewModel(private val repository: MineRepository) : BaseVMViewModel() {
    //每页加载的条数
    private val pageSize = 12

    //当前的页码
    private var mPage = 1

    //主播/用户id
    var memberId = ""

    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    val mAdapter by lazy {
        CommonBindVMAdapter<MinePublishDetailBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_publish_book,
            BR.releaseViewModel,
            BR.item
        )
    }

    /**
     * 发布的书籍
     */
    fun mineMemberReleaseBookList() {
        launchOnIO {
            repository.minePublishList(memberId, mPage, pageSize).checkResult(
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
     * 处理失败数据
     */
    private fun processFailureData(msg: String?) {
        if (mPage == 1) {
            refreshStatusModel.finishRefresh(false)
        } else {
            refreshStatusModel.finishLoadMore(false)
        }
        DLog.i("---->", "$msg")
    }

    /**
     * 处理成功数据
     */
    private fun processSuccessData(bean: MinePublishBean) {
        if (mPage == 1) {
            refreshStatusModel.finishRefresh(true)
            if (bean.list.isNotEmpty()) {
                mAdapter.setList(bean.list)
            } else {
                showDataEmpty()
            }
        } else {
            refreshStatusModel.finishLoadMore(true)
            mAdapter.addData(bean.list)
        }
        refreshStatusModel.setNoHasMore(mAdapter.data.size >= bean.total || bean.list.size < pageSize)
    }


    /**
     * 刷新数据
     */
    fun refreshData() {
        mPage = 1
        refreshStatusModel.setNoHasMore(false)
        mineMemberReleaseBookList()
    }

    /**
     * 加载更多数据
     */
    fun loadMoreData() {
        ++mPage
        mineMemberReleaseBookList()
    }

    /**
     * item点击事件
     */
    fun clickItemFun(context: Context, bean: MinePublishDetailBean) {
        RouterHelper.createRouter(HomeService::class.java).toDetailActivity(context, bean.audio_id)
    }

}