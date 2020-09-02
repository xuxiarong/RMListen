package com.rm.module_home.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.baselisten.adapter.multi.CommonMultiAdapter
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.holder.BaseBindHolder
import com.rm.business_lib.binding.bindData
import com.rm.business_lib.binding.bindLeftScroll
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.*
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

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItemViewType(position)) {
            R.layout.home_item_banner -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemBannerBinding
                homeItemBannerBinding.mainBanner.bindData(homeViewModel.homeBannerInfoList.value!!)
            }
            R.layout.home_item_collect_rv -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemCollectRvBinding
                if (homeViewModel.homeCollectModel.value != null) {
                    val homeCollectAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeCollectModel.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    homeItemBannerBinding.homeItemRvCollect.bindHorizontalLayout(homeCollectAdapter)
                }
            }
            R.layout.home_item_recommend_hor_single_rv -> {
                val singleRvBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemRecommendHorSingleRvBinding
                if (homeViewModel.homeHorSingleList.value != null) {
                    val singleAdapter =
                        CommonMultiAdapter(
                            homeViewModel.homeHorSingleList.value!!,
                            BR.item
                        )
                    singleRvBinding.homeRvRecommendHorSingle.bindHorizontalLayout(singleAdapter)
                }
            }

            R.layout.home_item_recommend_hor_double_rv -> {
                val singleRvBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemRecommendHorDoubleRvBinding
                if (homeViewModel.homeHorDoubleList.value != null) {
                    val doubleAdapter =
                        CommonMultiAdapter(
                            homeViewModel.homeHorDoubleList.value!!,
                            BR.item
                        )
                    singleRvBinding.homeRvRecommendHorDouble.bindHorizontalLayout(doubleAdapter)
                    singleRvBinding.homeRvRecommendHorDouble.bindLeftScroll { homeViewModel.doubleRvOpenDetail() }
                }
            }
            R.layout.home_item_recommend_grid_rv -> {
                val gridBanding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemRecommendGridRvBinding
                if (homeViewModel.homeGridList.value != null) {
                    val doubleAdapter =
                        CommonMultiAdapter(
                            homeViewModel.homeGridList.value!!,
                            BR.item
                        )
                    gridBanding.homeRvRecommendGrid.bindGridLayout(doubleAdapter, 3)
                }
            }
            R.layout.home_item_recommend_ver_rv -> {
                val verBanding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemRecommendVerRvBinding
                if (homeViewModel.homeVerList.value != null) {
                    val verAdapter =
                        CommonMultiAdapter(
                            homeViewModel.homeVerList.value!!,
                            BR.item
                        )
                    verBanding.homeRvRecommendVer.bindVerticalLayout(verAdapter)
                }
            }
        }

    }
}

