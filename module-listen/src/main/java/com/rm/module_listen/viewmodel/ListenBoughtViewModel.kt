package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BookBean

class ListenBoughtViewModel : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<BookBean>>()

    var itemClick: (BookBean) -> Unit = {}

    fun itemClickFun(bookBean: BookBean) {
        itemClick(bookBean)
    }

    fun getData() {
        data.postValue(get())
    }

    private fun get(): MutableList<BookBean> {
        val list = mutableListOf<BookBean>()
        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )
        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )
        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )

        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )
        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )
        list.add(
            BookBean(
                0,
                "小丸子",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "空间打开借记卡健康的卡夫卡",
                "幻海航行 | 领略科幻的海洋",
                "小丸子"
            )
        )
        return list
    }

}