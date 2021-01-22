package com.rm.module_home.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindHorizontalLayoutNoScroll
import com.rm.business_lib.binding.paddingBindData
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeItemBannerBinding
import com.rm.module_home.databinding.HomeItemMenuRvBinding
import com.rm.module_home.model.home.banner.HomeBannerRvModel
import com.rm.module_home.model.home.collect.HomeMenuRvModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel

/**
 * desc   :
 * date   : 2020/08/22
 * version: 1.0
 */
class HomeAdapter(
    private var homeViewModel: HomeFragmentViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(homeViewModel, modelBrId, itemBrId) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItemViewType(position)) {
            R.layout.home_item_banner -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemBannerBinding
                homeItemBannerBinding.mainBanner.apply {
                    val bannerData = data[position] as HomeBannerRvModel
                    bannerData.bannerList?.let {
                        paddingBindData(it)
                        this.setOnItemClickListener { _, _, _, position ->
                            BannerJumpUtils.onBannerClick(context, it[position].banner_jump)
                        }
                        setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {

                            }
                            override fun onPageSelected(position: Int) {
                                val bannerInfoBean = it[position]
                                if (bannerInfoBean.isAd) {
                                    BusinessInsertManager.doInsertKeyAndAd(
                                        BusinessInsertConstance.INSERT_TYPE_AD_EXPOSURE,
                                        bannerInfoBean.ad_id.toString()
                                    )
                                }
                            }
                            override fun onPageScrollStateChanged(state: Int) {
                            }
                        })
                    }
                }
            }
            R.layout.home_item_menu_rv -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemMenuRvBinding
                val menuData = data[position] as HomeMenuRvModel
                val homeCollectAdapter = CommonMultiVMAdapter(
                    homeViewModel,
                    menuData.menuList.toMutableList(),
                    BR.viewModel,
                    BR.item
                )
                if (homeCollectAdapter.data.size <= 3) {
                    homeItemBannerBinding.homeItemRvCollect.bindHorizontalLayoutNoScroll(
                        homeCollectAdapter
                    )
                } else {
                    homeItemBannerBinding.homeItemRvCollect.bindHorizontalLayout(homeCollectAdapter)
                }

            }
        }
    }
}

