package com.rm.module_home.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.HomeTopListActivity
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.home.HomeAudioModel
import com.rm.module_home.model.home.HomeBlockModel
import com.rm.module_home.model.home.HomeMenuModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment : BaseVMFragment<HomeHomeFragmentBinding, HomeFragmentViewModel>() {

    private val mHomeAdapter: HomeAdapter by lazy {
        HomeAdapter(mViewModel, BR.viewModel, BR.item)
    }

    override fun initLayoutId() = R.layout.home_home_fragment

    override fun initModelBrId(): Int = BR.viewModel

    override fun initData() {
        mViewModel.getHomeDataFromService()
    }

    override fun initView() {

        setStatusBar(R.color.base_activity_bg_color)

        mViewModel.collectItemClickList = { initCollectClick(it) }
        mViewModel.doubleRvLeftScrollOpenDetail = { startBoutique() }
        mViewModel.audioClick = { onAudioClick(it) }
        mViewModel.blockClick = { onBlockClick(it) }
        mDataBind.homeRv.bindVerticalLayout(mHomeAdapter)

    }

    private fun initCollectClick(model: HomeMenuModel) {
        when (model.menu_name) {
            getString(R.string.home_boutique) -> {
                startBoutique()
            }
            getString(R.string.home_rank) -> {
                startRank()
            }
            getString(R.string.home_listen_list) -> {
                startMenu()
            }
        }
    }

    /**
     * 数据监听
     */
    override fun startObserve() {
        mViewModel.homeAllData.observe(this, Observer {
            mHomeAdapter.setList(it)
        })
    }

    /**
     * 跳转精品
     */
    private fun startBoutique() {
        context?.let {
            BoutiqueActivity.startActivity(it)
        }
    }

    /**
     * 跳转排行
     */
    private fun startRank() {
        context?.let {
            HomeTopListActivity.startActivity(it)
        }
    }

    /**
     * 跳转听单
     */
    private fun startMenu() {
        context?.let {
            HomeMenuActivity.startActivity(it)
        }
    }

    /**
     * 不同模版的音频Item的点击事件
     * @param audioModel HomeAudioModel
     */
    private fun onAudioClick(audioModel: HomeAudioModel) {
        HomeDetailActivity.startActivity(activity!!, audioModel.audio_id)
    }


    /**
     * 首页板块的点击事件
     * @param blockModel HomeBlockModel
     */
    private fun onBlockClick(blockModel: HomeBlockModel) {
        HomeTopicListActivity.startActivity(
            activity!!,
            blockModel.page_id,
            blockModel.block_id,
            blockModel.topic_id,
            blockModel.block_name
        )
    }

}