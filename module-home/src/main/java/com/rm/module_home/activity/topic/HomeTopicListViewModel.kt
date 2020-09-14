package com.rm.module_home.activity.topic

import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.BR
import com.rm.business_lib.bean.AudioBean
import com.rm.module_home.R
import com.rm.module_home.repository.HomeTopicRepository

/**
 * desc   : 专题列表viewModel
 * date   : 2020/09/11
 * version: 1.0
 */
class HomeTopicListViewModel(val repository: HomeTopicRepository) : BaseVMViewModel() {
    var pageId = -1
    var blockId = -1
    var topicId = -1
    var page = 1
    val pageSize = 10

    val mAdapter by lazy {
        CommonBindAdapter<AudioBean>(
            mutableListOf(),
            R.layout.business_adapter_auto_noraml_item,
            BR.audioItem
        ).apply {
            setOnItemClickListener { _, _, position ->
                DLog.i(
                    "llj",
                    "item点击事件-position--->>>$position"
                )
            }
        }
    }

    /**
     * 获取专题列表
     */
    fun getTopicList() {
        launchOnIO {
            repository.getTopicList(pageId, blockId, topicId, page, pageSize).checkResult(
                onSuccess = {
                    it.list?.let { it1 -> mAdapter.data.addAll(it1) }
                    mAdapter.notifyDataSetChanged()
                    page ++
                },
                onError = {
                    if(page == 1){
                        // 获取第一页数据就失败
                        // TODO 显示错误视图
                    }else{
                        // TODO 获取下一页失败，显示列表加载失败的视图
                    }
                }
            )
        }
    }
}