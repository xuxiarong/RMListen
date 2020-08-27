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
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )
        list.add(
            TopListBean(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "小丸子",
                "幻海航行 | 领略科幻的海洋"
            )
        )


        return list
    }
}