package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.business_lib.xbanner.BannerInfo
import com.rm.module_home.api.HomeApiService
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.bean.MenuItemBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuRepository(val service: HomeApiService) : BaseRepository() {
    fun getMenuBanner(): MutableList<BannerInfo> {
        val bannerList = mutableListOf<BannerInfo>()
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg"))
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg"))
        bannerList.add(BannerInfo("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg"))
        bannerList.add(BannerInfo("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg"))
        return bannerList
    }

    fun getMenuListInfo(): MutableList<MenuItemBean> {
        // 模拟听单列表数据
        val bookList = mutableListOf<BookBean>()
        bookList.add(
            BookBean(
                0,
                "Afamefuna\nOkparo",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "古代"
            )
        )
        bookList.add(
            BookBean(
                1,
                "Sakane Miiko",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095666640&di=59e290804282ed57054f6e7237e82434&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D19a60d9f8744ebf86d716437e9f8d736%2Fd87ae686c9177f3e15cfc89170cf3bc79d3d56e0.jpg",
                "现代"
            )
        )
        bookList.add(
            BookBean(
                2,
                "Emiliya\nPerevalova",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095570935&di=073c83a1d4756bbb0646b3d3fc846dff&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Frushidao%2Fpics%2Fhv1%2F20%2F108%2F1744%2F113431160.jpg",
                "未来"
            )
        )
        val menuItemList = mutableListOf<MenuItemBean>()
        menuItemList.add(
            MenuItemBean(
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
                ,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg"
                ,
                "3.4W",
                30,
                bookList
            )
        )


        val bookList1 = mutableListOf<BookBean>()
        bookList1.add(
            BookBean(
                0,
                "1_0",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598075810332&di=b8ec1ef91766907dd06bd27b674d9243&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg",
                "古代"
            )
        )
        bookList1.add(
            BookBean(
                1,
                "1_1",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598075810332&di=3132d4d94e28e5632c4043d1f5572643&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F56%2F12%2F01300000164151121576126282411.jpg",
                "现代"
            )
        )
        bookList1.add(
            BookBean(
                2,
                "1_2",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598075810331&di=b0fbc54b483e31bd000bfbfefb485460&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F22%2F59%2F19300001325156131228593878903.jpg",
                "未来"
            )
        )
        menuItemList.add(
            MenuItemBean(
                "第二个item",
                "一个人不寂寞"
                ,
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1027245443,3552957153&fm=26&gp=0.jpg"
                ,
                "3.9W",
                120,
                bookList1
            )
        )
        return menuItemList
    }
}