package com.rm.module_home.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.business_lib.binding.bindData
import com.rm.business_lib.binding.bindLeftScroll
import com.rm.component_comm.utils.BannerJumpUtils
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

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItemViewType(position)) {
            R.layout.home_item_banner -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemBannerBinding
                homeItemBannerBinding.mainBanner.apply {
                    bindData(homeViewModel.homeBannerInfoList.value!!)
                    this.setOnItemClickListener { banner, model, view, position ->
                        BannerJumpUtils.onBannerClick(context,homeViewModel.homeBannerInfoList.value!![position].banner_jump)
                    }
                }
            }
            R.layout.home_item_menu_rv -> {
                val homeItemBannerBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemMenuRvBinding
                if (homeViewModel.homeMenuList.value != null) {
                    val homeCollectAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeMenuList.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    homeItemBannerBinding.homeItemRvCollect.bindHorizontalLayout(homeCollectAdapter)
                }
            }
            R.layout.home_item_audio_hor_single_rv -> {
                val singleRvBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemAudioHorSingleRvBinding
                if (homeViewModel.homeHorSingleList.value != null) {
                    val singleAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeHorSingleList.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    singleRvBinding.homeRvRecommendHorSingle.bindHorizontalLayout(singleAdapter)
                }
            }

            R.layout.home_item_audio_hor_double_rv -> {
                val singleRvBinding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemAudioHorDoubleRvBinding
                if (homeViewModel.homeHorDoubleList.value != null) {
                    val doubleAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeHorDoubleList.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    singleRvBinding.homeRvRecommendHorDouble.bindHorizontalLayout(doubleAdapter)
                    singleRvBinding.homeRvRecommendHorDouble.bindLeftScroll { homeViewModel.doubleRvOpenDetail() }
                }
            }
            R.layout.home_item_audio_grid_rv -> {
                val gridBanding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemAudioGridRvBinding
                if (homeViewModel.homeGridList.value != null) {
                    val doubleAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeGridList.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    gridBanding.homeRvRecommendGrid.bindGridLayout(doubleAdapter, 3)
                }
            }
            R.layout.home_item_audio_ver_rv -> {
                val verBanding =
                    DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView) as HomeItemAudioVerRvBinding
                if (homeViewModel.homeVerList.value != null) {
                    val verAdapter =
                        CommonMultiVMAdapter(
                            homeViewModel,
                            homeViewModel.homeVerList.value!!,
                            BR.viewModel,
                            BR.item
                        )
                    verBanding.homeRvRecommendVer.bindVerticalLayout(verAdapter)
                }
            }
        }

    }
}

