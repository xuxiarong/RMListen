package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.business_lib.bean.BookBean
import com.rm.business_lib.bean.BannerInfo
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.MenuDetailBean
import com.rm.module_home.bean.MenuItemBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuDetailRepository(val service: HomeApiService) : BaseRepository() {

    fun getMenuItem(): MenuDetailBean {
        return MenuDetailBean(
            "乐于揪头发，勇于照镜子",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
            "一个人不寂寞",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1027245443,3552957153&fm=26&gp=0.jpg",
            "3.9W",
            "温实打头只回当府角京拉听断在何除想民起部加公实知是在般决用色平们件商加大示前府特后且化西。应白流再线并住科立流改下指族始论包入便包规将成较反量千更据那被术信新民难期马打厂往场准团及复上论适基。么则时南正而常史至重关改作主向多始又只委你从分积百便料将事响去住之等即类王许无等毛日系七头界属物。队制价求那斗前除叫京易风养利器装容热看三九机己条进整件系识号九矿七化青音而活几地非马化基位般队并从想内别电必增。时但确展权关飞示题",
            false,
            getMenuListInfo()
        )

    }

        fun getMenuListInfo(): MutableList<BookBean> {
        // 模拟听单列表数据
        val bookList = mutableListOf<BookBean>()
        bookList.add(
            BookBean(
                0,
                "Afamefuna\nOkparo",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
        bookList.add(
            BookBean(
                1,
                "Sakane Miiko",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095666640&di=59e290804282ed57054f6e7237e82434&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D19a60d9f8744ebf86d716437e9f8d736%2Fd87ae686c9177f3e15cfc89170cf3bc79d3d56e0.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
        bookList.add(
            BookBean(
                2,
                "Emiliya\nPerevalova",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095570935&di=073c83a1d4756bbb0646b3d3fc846dff&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Frushidao%2Fpics%2Fhv1%2F20%2F108%2F1744%2F113431160.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
       bookList.add(
            BookBean(
                0,
                "Afamefuna\nOkparo",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
        bookList.add(
            BookBean(
                1,
                "Sakane Miiko",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095666640&di=59e290804282ed57054f6e7237e82434&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D19a60d9f8744ebf86d716437e9f8d736%2Fd87ae686c9177f3e15cfc89170cf3bc79d3d56e0.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
        bookList.add(
            BookBean(
                2,
                "Emiliya\nPerevalova",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598095570935&di=073c83a1d4756bbb0646b3d3fc846dff&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Frushidao%2Fpics%2Fhv1%2F20%2F108%2F1744%2F113431160.jpg",
                "公司要为员工创造环境，但员工的成长最终是靠自己",
                "测试文本信息",
                "小丸子"
            )
        )
        return bookList
    }
}