package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetBean

class ListenSheetCollectedListViewModel : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<SheetBean>>()

    fun getData() {
        data.postValue(data())
    }

    private fun data(): MutableList<SheetBean> {
        val list = mutableListOf<SheetBean>()
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )

        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )

        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )

        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )

        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )

        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        list.add(
            SheetBean(
                audioData(),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "小丸子",
                0,
                20,
                0,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "",
                "幻海航行 | 领略科幻的海洋",
                "", 1
            )
        )
        return list
    }

    private fun audioData(): AudioListBean {
        val list = mutableListOf<AudioBean>()
        list.add(
            AudioBean(
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "",
                "小丸子",
                "",
                "",
                "",
                "小丸子",
                1,
                "文案",
                "",
                "小丸子",
                "",
                "", 1, 1, 1, "", 1
            )
        )
        list.add(
            AudioBean(
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "",
                "小丸子",
                "",
                "",
                "",
                "小丸子",
                1,
                "文案",
                "",
                "小丸子",
                "",
                "", 1, 1, 1, "", 1
            )
        )

        list.add(
            AudioBean(
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "",
                "小丸子",
                "",
                "",
                "",
                "小丸子",
                1,
                "文案",
                "",
                "小丸子",
                "",
                "", 1, 1, 1, "", 1
            )
        )
        return AudioListBean(list, 1)
    }
}