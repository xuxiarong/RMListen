package com.rm.module_home.activity.boutique

import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.R
import com.rm.module_home.adapter.BookAdapter
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeFragmentBoutiqueBinding
import kotlinx.android.synthetic.main.home_fragment_boutique.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragment(private val categoryTabBean: CategoryTabBean) :
    BaseVMFragment<HomeFragmentBoutiqueBinding>(R.layout.home_fragment_boutique) {

    private val boutiqueViewModel by viewModel<BoutiqueFragmentViewModel>()

    override fun initView() {
        super.initView()
        binding.viewModel = boutiqueViewModel
        boutiqueViewModel.categoryName = categoryTabBean.name
    }

    override fun initData() {
        boutiqueViewModel.getBannerInfo()
        boutiqueViewModel.getBookInfo()
    }

    override fun startObserve() {
        boutiqueViewModel.bookInfoList.observe(this){
            home_boutique_fragment_recycler_view.bindVerticalLayout(BookAdapter().apply { setNewInstance(it) })
        }
    }
}