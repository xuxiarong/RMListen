package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.R
import com.rm.module_home.model.HomeRecommendModel
import com.rm.module_home.model.home.collect.HomeCollectModel
import com.rm.module_home.model.home.grid.HomeGridRecommendModel
import com.rm.module_home.model.home.hordouble.HomeRecommendHorDoubleModel
import com.rm.module_home.model.home.horsingle.HomeRecommendHorSingleModel
import com.rm.module_home.model.home.ver.HomeRecommendVerModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel : BaseVMViewModel() {

    var homeBannerInfoList = MutableLiveData<MutableList<BannerInfoBean>>()
    var homeCollectModel = MutableLiveData<MutableList<MultiItemEntity>>()
    var homeHorSingleList = MutableLiveData<MutableList<HomeRecommendHorSingleModel>>()
    var homeHorDoubleList = MutableLiveData<MutableList<HomeRecommendHorDoubleModel>>()
    var homeGridList = MutableLiveData<MutableList<HomeGridRecommendModel>>()
    var collectItemClickList :(HomeCollectModel)->Unit ={}

    var homeVerList = MutableLiveData<MutableList<MultiItemEntity>>()
    var doubleRvLeftScrollOpenDetail : ()->Unit = {}

    fun initBannerInfo() {
        val bannerList = mutableListOf<BannerInfoBean>()
        bannerList.add(BannerInfoBean(0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg","",0,0))
        bannerList.add(BannerInfoBean(0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg","",0,0))
        bannerList.add(BannerInfoBean(0,"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg","",0,0))
        bannerList.add(BannerInfoBean(0,"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg","",0,0))
        homeBannerInfoList.value = bannerList
    }

    fun initCollect() {

        homeCollectModel.value = mutableListOf(
            HomeCollectModel(
                R.drawable.home_icon_collect_recommend,
                "精品推荐"
            ) ,
            HomeCollectModel(
                R.drawable.home_icon_collect_rank,
                "榜单"
            ) ,
            HomeCollectModel(
                R.drawable.home_icon_head_reading,
                "看书"
            ) ,
            HomeCollectModel(
                R.drawable.home_icon_collect_listen,
                "听单"
            )
        )
    }

    fun collectClick(model : HomeCollectModel){
        collectItemClickList(model)
        DLog.d("suolong","model = ${model.collectName} ")
    }

    fun initCollectClick(vararg clickList: () -> Unit){

    }

    fun doubleRvOpenDetail(){
        doubleRvLeftScrollOpenDetail()
    }


    fun initSingleList() {
        val singleList = mutableListOf<HomeRecommendHorSingleModel>()
        singleList.add(
            HomeRecommendHorSingleModel(
                HomeRecommendModel(
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                    8.0f,
                    "1111111",
                    "小丸子11",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeRecommendHorSingleModel(
                HomeRecommendModel(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                    8.0f,
                    "222222222",
                    "小丸子22",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeRecommendHorSingleModel(
                HomeRecommendModel(
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                    8.0f,
                    "3333333333",
                    "小丸子33",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeRecommendHorSingleModel(
                HomeRecommendModel(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                    8.0f,
                    "4444444444",
                    "小丸子44",
                    "Vip"
                )
            )
        )
        this.homeHorSingleList.value = singleList
    }

    fun initDoubleList() {
        val recommendHorDoubleList = ArrayList<HomeRecommendHorDoubleModel>()

        val top1 = HomeRecommendModel(
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )

        val bottom1 = HomeRecommendModel(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )
        val top2 = HomeRecommendModel(
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )

        val bottom2 = HomeRecommendModel(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )
        val top3 = HomeRecommendModel(
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )
        val bottom3 = HomeRecommendModel(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )
        val top4 = HomeRecommendModel(
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )

        val bottom4 = HomeRecommendModel(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
            8.0f,
            "Carolyn Gregory",
            "小丸子",
            "Vip"
        )

        recommendHorDoubleList.add(
            HomeRecommendHorDoubleModel(
                top1,
                bottom1
            )
        )
        recommendHorDoubleList.add(
            HomeRecommendHorDoubleModel(
                top2,
                bottom2
            )
        )
        recommendHorDoubleList.add(
            HomeRecommendHorDoubleModel(
                top3,
                bottom3
            )
        )
        recommendHorDoubleList.add(
            HomeRecommendHorDoubleModel(
                top4,
                bottom4
            )
        )
        this.homeHorDoubleList.value = recommendHorDoubleList
    }

    fun initGridList() {
        val singleList = ArrayList<HomeGridRecommendModel>()
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                    8.0f,
                    "1111111",
                    "小丸子11",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                    8.0f,
                    "222222222",
                    "小丸子22",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                    8.0f,
                    "3333333333",
                    "小丸子33",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                    8.0f,
                    "4444444444",
                    "小丸子44",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                    8.0f,
                    "3333333333",
                    "小丸子33",
                    "Vip"
                )
            )
        )
        singleList.add(
            HomeGridRecommendModel(
                HomeRecommendModel(
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                    8.0f,
                    "4444444444",
                    "小丸子44",
                    "Vip"
                )
            )
        )
        this.homeGridList.value = singleList
    }

    fun initVerList(){
        homeVerList.value = mutableListOf(
            HomeRecommendVerModel(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "Vip",
                "你自己觉得有，别人感觉不",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "楼芸蓓"
            ),
            HomeRecommendVerModel(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "Vip",
                "你自己觉得有，别人感觉不",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "楼芸蓓"
            ),
            HomeRecommendVerModel(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "Vip",
                "你自己觉得有，别人感觉不",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "楼芸蓓"
            ),
            HomeRecommendVerModel(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "Vip",
                "你自己觉得有，别人感觉不",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "楼芸蓓"
            ),
            HomeRecommendVerModel(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "Vip",
                "你自己觉得有，别人感觉不",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "楼芸蓓"
            )
        )
    }
}