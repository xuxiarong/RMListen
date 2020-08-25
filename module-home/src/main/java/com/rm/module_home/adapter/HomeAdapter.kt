package com.rm.module_home.adapter

import android.view.ViewGroup
import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.holder.BaseBindHolder
import com.rm.business_lib.binding.bindData
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.*
import com.rm.module_home.viewmodel.HomeFragmentViewModel

/**
 * desc   :
 * date   : 2020/08/22
 * version: 1.0
 */
class HomeAdapter(var homeViewModel : HomeFragmentViewModel,var list : List<IBindItemType>, var bindId : Int)  : BaseMultiAdapter<BaseMultiAdapter.IBindItemType> (list,bindId){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        return super.onCreateViewHolder(parent, viewType)
    }


    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when(getItemViewType(position)){
            R.layout.home_item_banner ->{
                val homeItemBannerBinding   = holder.binding as HomeItemBannerBinding
                homeItemBannerBinding.mainBanner.bindData(homeViewModel.homeBannerInfoList.value)
            }
            R.layout.home_item_collect_rv ->{
                val homeItemBannerBinding   = holder.binding as HomeItemCollectRvBinding
                if(homeViewModel.homeCollectModel.value!=null){
                    val homeCollectAdapter = HomeCollectAdapter(homeViewModel.homeCollectModel.value!!,R.layout.home_item_collect,BR.item)
                    homeItemBannerBinding.homeItemRvCollect.bindHorizontalLayout(homeCollectAdapter)
                }
            }
            R.layout.home_item_recommend_hor_single_rv ->{
                val singleRvBinding = holder.binding as HomeItemRecommendHorSingleRvBinding
                if(homeViewModel.homeHorSingleList.value!=null){
                    val singleAdapter = CommonMultiAdapter(homeViewModel.homeHorSingleList.value!!,BR.item)
                    singleRvBinding.homeRvRecommendHorSingle.bindHorizontalLayout(singleAdapter)
                }
            }

            R.layout.home_item_recommend_hor_double_rv ->{
                val singleRvBinding = holder.binding as HomeItemRecommendHorDoubleRvBinding
                if(homeViewModel.homeHorDoubleList.value!=null){
                    val doubleAdapter = CommonMultiAdapter(homeViewModel.homeHorDoubleList.value!!,BR.item)
                    singleRvBinding.homeRvRecommendHorDouble.bindHorizontalLayout(doubleAdapter)
                }
            }
            R.layout.home_item_recommend_grid_rv ->{
                val gridBanding = holder.binding as HomeItemRecommendGridRvBinding
                if(homeViewModel.homeGridList.value!=null){
                    val doubleAdapter = CommonMultiAdapter(homeViewModel.homeGridList.value!!,BR.item)
                    gridBanding.homeRvRecommendGrid.bindGridLayout(doubleAdapter,3)
                }
            }
        }

    }
}

