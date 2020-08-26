package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.bean.TopListBean

class TopListRepository(private val service: HomeApiService) : BaseRepository() {
    fun getTabList(): MutableList<CategoryTabBean> {
        val list = mutableListOf<CategoryTabBean>()
        list.add(CategoryTabBean(1, "热门榜"))
        list.add(CategoryTabBean(2, "热销榜"))
        list.add(CategoryTabBean(3, "新书榜"))
        list.add(CategoryTabBean(4, "搜索榜"))
        list.add(CategoryTabBean(5, "好评榜"))
        return list
    }

    fun getDataList(): MutableList<TopListBean> {
        val list = mutableListOf<TopListBean>()
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                "yuan",
                "今天天气不错"
            )
        )
list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg",
                "yuan",
                "今天天气不错"
            )
        )
        list.add(
            TopListBean(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                "yuan",
                "今天天气不错"
            )
        )

        return list
    }
}