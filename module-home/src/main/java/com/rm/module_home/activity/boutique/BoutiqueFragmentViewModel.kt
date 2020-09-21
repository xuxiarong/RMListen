package com.rm.module_home.activity.boutique

import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.BR
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.repository.BoutiqueRepository

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragmentViewModel(private val repository: BoutiqueRepository) : BaseVMViewModel() {
    private var page = 1
    private val pageSize = 10

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var categoryTabBean: CategoryTabBean? = null
    val bookAdapter by lazy {
        CommonBindAdapter<AudioBean>(
            mutableListOf(),
            R.layout.business_adapter_auto_noraml_item,
            BR.audioItem
        ).apply {
            setOnItemClickListener { _, _, position ->
                startActivity(HomeDetailActivity::class.java,HomeDetailActivity.getIntent(data[position].audio_id))
            }
        }
    }

    fun getBookList() {
        if (page == 1) {
            showLoading()
        }
        launchOnIO {
            repository.getBoutiqueRecommendInfoList(categoryTabBean!!.class_id, page, pageSize)
                .checkResult(
                    onSuccess = {
                        bookAdapter.data.addAll(it.list)
                        bookAdapter.notifyDataSetChanged()
                        if (page == 1) {
                            if (it.list.isEmpty()) {
                                showDataEmpty()
                            } else {
                                showContentView()
                            }
                        } else {
                            // 加载更多
                            refreshStatusModel.finishLoadMore(true)
                        }
                        // 设置是否还有下一页数据
                        refreshStatusModel.setHasMore(it.list.size > 0)
                        page++
                    },
                    onError = {
                        if (page == 1) {
                            showNetError()
                        } else {
                            // 获取下一页失败，显示列表加载失败的视图
                            refreshStatusModel.finishLoadMore(false)
                        }
                    }
                )
        }
    }

    fun refresh() {
        page = 0
        launchOnIO {
            repository.getBoutiqueRecommendInfoList(categoryTabBean!!.class_id, page, pageSize)
                .checkResult(
                    onSuccess = {
                        // 清空所有数据
                        bookAdapter.data.clear()
                        it.list.let { it1 -> bookAdapter.data.addAll(it1) }
                        bookAdapter.notifyDataSetChanged()
                        page++
                        refreshStatusModel.finishRefresh(true)
                        // 设置是否还有下一页数据
                        refreshStatusModel.setHasMore(it.list.size > 0)

                    },
                    onError = {
                        // 下拉刷新失败
                        refreshStatusModel.finishRefresh(false)
                    }
                )
        }
    }
}