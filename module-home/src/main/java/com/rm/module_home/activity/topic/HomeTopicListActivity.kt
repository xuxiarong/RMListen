package com.rm.module_home.activity.topic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.databinding.Observable
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
        fun startActivity(
            context: Context,
            pageId: Int,
            blockId: Int,
            topicId: Int,
            topicName: String
        ) {
            context.startActivity(
                Intent(
                    context,
                    HomeTopicListActivity::class.java
                ).apply {
                    putExtra("pageId", pageId)
                    putExtra("blockId", blockId)
                    putExtra("topicId", topicId)
                    putExtra("topicName", topicName)
                })
        }
    }

    private val footView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    override fun getLayoutId(): Int = R.layout.home_activity_topic_list

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.refreshStatusModel.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStatusModel.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.mAdapter.removeAllFooterView()
                    mViewModel.mAdapter.addFooterView(footView)
                } else {
                    mViewModel.mAdapter.removeAllFooterView()
                }
            }
        })
    }

    override fun initView() {
        super.initView()
        mViewModel.pageId = intent.getIntExtra("pageId", -1)
        mViewModel.blockId = intent.getIntExtra("blockId", -1)
        mViewModel.topicId = intent.getIntExtra("topicId", -1)

        val stringExtra = intent.getStringExtra("topicName")
        home_topic_title.text = stringExtra


    }

    override fun initData() {
        mViewModel.getTopicList()
    }
}