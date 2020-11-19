package com.rm.module_home.activity.boutique

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.binding.paddingBindData
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeFragmentBoutiqueBinding
import com.rm.business_lib.xbanner.XBanner

/**
 * desc   : 精品Fragment
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragment(
    private val categoryTabBean: CategoryTabBean,
    private val bannerList: List<BannerInfoBean>
) : BaseVMFragment<HomeFragmentBoutiqueBinding, BoutiqueFragmentViewModel>() {

    private val headView by lazy {
        View.inflate(activity, R.layout.home_header_banner, null)
    }
    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
    }

    override fun initLayoutId(): Int = R.layout.home_fragment_boutique

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        super.initView()
        mViewModel.categoryTabBean = categoryTabBean
        headView.findViewById<XBanner>(R.id.home_head_banner).apply {
            setIsClipChildrenMode(false)
            paddingBindData(bannerList, true)
            setOnItemClickListener { _, _, _, position ->
                BannerJumpUtils.onBannerClick(context, bannerList[position].banner_jump)
            }
        }

        mViewModel.bookAdapter.addHeaderView(headView)
    }

    override fun initData() {
        mViewModel.getBookList()
    }

    override fun startObserve() {
        mViewModel.refreshStatusModel.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStatusModel.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.bookAdapter.removeAllFooterView()
                    mViewModel.bookAdapter.addFooterView(footView)
                } else {
                    mViewModel.bookAdapter.removeAllFooterView()
                }
            }
        })
    }


}