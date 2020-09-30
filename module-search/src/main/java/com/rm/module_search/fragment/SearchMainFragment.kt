package com.rm.module_search.fragment

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.adapter.SearchMainAdapter
import com.rm.module_search.databinding.SearchFragmentMainBinding
import com.rm.module_search.hotRecommend
import com.rm.module_search.type
import com.rm.module_search.viewmodel.SearchMainViewModel
import kotlinx.android.synthetic.main.search_fragment_main.*

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainFragment : BaseVMFragment<SearchFragmentMainBinding, SearchMainViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.search_fragment_main


    override fun initData() {
        mViewModel.searchHotRecommend()
        mViewModel.searchRecommend()
        search_main_tv_search.setOnClickListener {
            mViewModel.searchResult("周")
        }
    }


    private fun attachViewPager() {
        BendTabLayoutMediator(search_main_tab_layout, search_main_view_pager,
            BendTabLayoutMediator.TabConfigurationStrategy { tab, position ->
                mViewModel.mTabDataList.get()?.let {
                    if (it.size > position) {
                        tab.text = it[position].title
                    }
                }
            }).attach()

    }

    override fun startObserve() {
        hotRecommend.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                hotRecommend.get()?.let {
                    setData()
                }
            }
        })

        mViewModel.contentData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mViewModel.contentData.get()?.let {
                    search_main_view_pager.currentItem = 0

                    if (mViewModel.hasAddContentTab.get() == true) {
                        setData()
                    }
                }
            }
        })
        type.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                type.get()!!
            }
        })

    }

    private fun setData() {
        mViewModel.mTabDataList.get()?.let {
            search_main_view_pager.adapter = SearchMainAdapter(this, it)
            attachViewPager()
        }
    }
}