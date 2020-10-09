package com.rm.module_home.activity.boutique

import android.view.View
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.binding.paddingBindData
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeFragmentBoutiqueBinding
import com.stx.xhb.androidx.XBanner

/**
 * desc   : 精品Fragment
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragment(
    private val categoryTabBean: CategoryTabBean,
    private val bannerList: List<BannerInfoBean>
) :

    BaseVMFragment<HomeFragmentBoutiqueBinding, BoutiqueFragmentViewModel>() {

    private val headView by lazy {
        View.inflate(activity, R.layout.home_header_banner, null).apply {
            setPadding(
                paddingLeft,
                dip(14f),
                paddingRight,
                paddingBottom
            )
        }
    }

    override fun initLayoutId(): Int = R.layout.home_fragment_boutique

    override fun initModelBrId(): Int = BR.viewModel

    override fun initView() {
        super.initView()
        mViewModel.categoryTabBean = categoryTabBean

        headView.findViewById<XBanner>(R.id.home_head_banner).apply {
            setIsClipChildrenMode(false)
            paddingBindData(bannerList)
            setOnItemClickListener { banner, model, view, position ->
                BannerJumpUtils.onBannerClick(context,bannerList[position].banner_jump)
            }
        }

        mViewModel.bookAdapter.addHeaderView(headView)
    }

    override fun initData() {
        mViewModel.getBookList()
    }

    override fun startObserve() {
    }


}