package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.SheetBean

class ListenSheetMyListViewModel : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<SheetBean>>()
    private val itemClick: (SheetBean) -> Unit = {}

    fun itemClickFun(bean: SheetBean) {
        itemClick(bean)
    }

    fun getData() {
        data.postValue(data())
    }

    private fun data(): MutableList<SheetBean> {
        val list = mutableListOf<SheetBean>()
        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "我喜欢的书籍",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )

        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "不要总认为自己比别人聪明",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )
        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "never never give up",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )
        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "别把沙子放大为绊脚石",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )
        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "当你的伙伴需要你伸出一只手时，不妨把肩膀也给他",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )
        list.add(
            SheetBean(
                null,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg",
                "2020/9/5",
                0,
                "",
                "重复等于强调",
                0,
                20,
                0,
                "",
                0L,
                "",
                "",
                1
            )
        )
        return list
    }
}