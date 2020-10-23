package com.rm.module_search.fragment

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getListString
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_search.BR
import com.rm.module_search.HISTORY_KEY
import com.rm.module_search.R
import com.rm.module_search.adapter.SearchMainAdapter
import com.rm.module_search.databinding.SearchFragmentMainBinding
import com.rm.module_search.hotRecommend
import com.rm.module_search.viewmodel.SearchMainViewModel
import kotlinx.android.synthetic.main.search_fragment_main.*
import java.lang.ref.WeakReference
import kotlin.random.Random


/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainFragment : BaseVMFragment<SearchFragmentMainBinding, SearchMainViewModel>() {
    private var hintTask: AutoTask? = null

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.search_fragment_main

    override fun initView() {
        super.initView()
        mDataShowView = search_main_view_pager
        hintTask = AutoTask(this)

        mViewModel.clearInput = {
            mDataBind.searchMainEditText.setText("")
        }
    }

    override fun initData() {
        mViewModel.searchHotRecommend()
        mViewModel.searchRecommend()
        mViewModel.searchHintBanner()
    }

    /**
     * tabLayout绑定viewpager滑动事件
     */
    private fun attachViewPager() {
        BendTabLayoutMediator(search_main_tab_layout, search_main_view_pager,
            BendTabLayoutMediator.TabConfigurationStrategy { tab, position ->
                mViewModel.mTabDataList.get()?.let {
                    if (it.size > position) {
                        tab.text = it[position]
                    }
                }
            }).attach()

    }

    override fun startObserve() {
        //推荐搜索数据监听
        hotRecommend.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                hotRecommend.get()?.let {
                    setData()
                }
            }
        })

        //搜索框轮播数据监听
        mViewModel.hintBannerList.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val list = mViewModel.hintBannerList.get()!!
                mDataBind.searchMainEditText.hint = list[0]
                startHintBanner()
            }
        })
    }

    private fun setData() {
        DLog.i("------->", "setData--->${search_main_view_pager == null}")
        mViewModel.mTabDataList.get()?.let {
            search_main_view_pager?.let { viewPager ->
                viewPager.adapter = SearchMainAdapter(this, it)
                attachViewPager()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.clickClearInput()
        mViewModel.hintBannerList.get()?.let { startHintBanner() }

        refreshHistoryData()
    }


    override fun onStop() {
        super.onStop()
        stopHintBanner()
    }

    /**
     * 刷新搜索历史
     */
    private fun refreshHistoryData() {
        val list = HISTORY_KEY.getListString()
        mViewModel.historyIsVisible.set(list.size > 0)
        mViewModel.historyList.set(list)
    }

    /**
     * 开始轮播
     */
    private fun startHintBanner() {
        stopHintBanner()
        mDataBind.searchMainEditText.postDelayed(hintTask, 3000)
    }

    /**
     * 结束轮播
     */
    private fun stopHintBanner() {
        mDataBind.searchMainEditText.removeCallbacks(hintTask)
    }

    /**
     * 定时任务
     */
    class AutoTask(fragment: SearchMainFragment) : Runnable {
        private val weakReference = WeakReference(fragment)
        override fun run() {
            weakReference.get()?.let {
                it.mViewModel.hintBannerList.get()?.let { list ->
                    val random = Random.nextInt(list.size)
                    val str = list[random]
                    it.mViewModel.hintKeyword = str
                    it.mDataBind.searchMainEditText.hint = str
                    it.startHintBanner()
                }
            }
        }
    }

}