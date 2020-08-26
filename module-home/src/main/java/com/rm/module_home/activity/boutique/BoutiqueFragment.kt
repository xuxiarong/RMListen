package com.rm.module_home.activity.boutique

import android.view.View
import androidx.lifecycle.observe
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DisplayUtils
import com.rm.business_lib.binding.paddingBindData
import com.rm.module_home.R
import com.rm.module_home.adapter.BookAdapter
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeFragmentBoutiqueBinding
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.home_fragment_boutique.*

/**
 * desc   : 精品Fragment
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragment(private val categoryTabBean: CategoryTabBean) :
    BaseVMFragment<HomeFragmentBoutiqueBinding, BoutiqueFragmentViewModel>() {

    private val headView by lazy {
        View.inflate(activity, R.layout.home_header_banner, null).apply {
            setPadding(
                paddingLeft,
                DisplayUtils.dip2px(BaseApplication.CONTEXT, 14f),
                paddingRight,
                paddingBottom
            )
        }
    }

    private val bookAdapter by lazy {
        BookAdapter().apply {
            addHeaderView(headView)
        }
    }

//    private val boutiqueViewModel by viewModel<BoutiqueFragmentViewModel>()

    override fun initLayoutId(): Int = R.layout.home_fragment_boutique

    override fun initView() {
        super.initView()
        mViewModel.categoryName = categoryTabBean.name
        dataBind.viewModel = mViewModel
    }

    override fun initData() {
        mViewModel.getBannerInfo()
        mViewModel.getBookInfo()
    }

    override fun startObserve() {
        mViewModel.bannerInfoList.observe(this) {
            headView.findViewById<XBanner>(R.id.home_head_banner).apply {
                setIsClipChildrenMode(false)
                paddingBindData(it)
            }
        }

        mViewModel.bookInfoList.observe(this) {
            bookAdapter.setNewInstance(it)
            home_boutique_fragment_recycler_view.bindVerticalLayout(bookAdapter)
        }
    }


}