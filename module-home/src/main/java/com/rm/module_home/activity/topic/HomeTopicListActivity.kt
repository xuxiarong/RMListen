package com.rm.module_home.activity.topic

import android.content.Context
import android.content.Intent
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityTopicListBinding
import kotlinx.android.synthetic.main.home_activity_topic_list.*

/**
 * desc   : 专题列表界面
 * date   : 2020/09/11
 * version: 1.0
 */
class HomeTopicListActivity :
    ComponentShowPlayActivity<HomeActivityTopicListBinding, HomeTopicListViewModel>() {

    companion object {
        fun startTopicActivity(
                context: Context,
                topicId: Int,
                block_name: String
        ) {
            context.startActivity(
                Intent(
                    context,
                    HomeTopicListActivity::class.java
                ).apply {
                    putExtra("topicId", topicId)
                    putExtra("block_name", block_name)
                })
        }
    }


    override fun getLayoutId(): Int = R.layout.home_activity_topic_list

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mDataShowView = home_topic_refreshLayout
    }

    override fun initData() {
        mViewModel.topicId = intent.getIntExtra("topicId", -1)
        mViewModel.blockName.set(intent.getStringExtra("block_name")?:"")
        mViewModel.getTopicList()
    }
}