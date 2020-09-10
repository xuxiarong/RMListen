package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.*
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.repository.MenuRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuViewModel(private val repository: MenuRepository) : BaseVMViewModel() {
    // 听单列表数据
    var menuList = MutableLiveData<MenuSheetBean>()


    fun getMenuListInfo() {
          launchOnIO {
              repository.sheet().checkResult(
                  onSuccess = {
                      menuList.value = it
                  },
                  onError = {
                      DLog.i("------>", "$it")
                  }
              )
          }

//        menuList.value = getData()
    }

    private fun getData(): MenuSheetBean {
        return MenuSheetBean(getBannerList(), null, null, getSheetList(), 1, "")
    }

    private fun getBannerInfo(): BannerInfoBean {
        return BannerInfoBean(
            0,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg",
            "",
            0,
            0
        )
    }

    private fun getBannerList(): MutableList<BannerInfoBean> {
        val mutableListOf = mutableListOf<BannerInfoBean>()
        mutableListOf.add(getBannerInfo())
        mutableListOf.add(getBannerInfo())
        mutableListOf.add(getBannerInfo())
        mutableListOf.add(getBannerInfo())
        mutableListOf.add(getBannerInfo())
        return mutableListOf
    }

    private fun getSheetList(): SheetListBean {
        val sheetList = mutableListOf<SheetBean>()
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        sheetList.add(getSheet())
        return SheetListBean(sheetList,1)
    }

    private fun getSheet(): SheetBean {
        return SheetBean(
            getAudioListBean(),
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
    }

    private fun getAudioListBean(): AudioListBean {
        return AudioListBean(getAudioBean(), 1)
    }

    private fun getAudioBean(): MutableList<AudioBean> {
        val mutableListOf = mutableListOf<AudioBean>()
        mutableListOf.add(getAudio())
        mutableListOf.add(getAudio())
        mutableListOf.add(getAudio())
        mutableListOf.add(getAudio())
        mutableListOf.add(getAudio())
        return mutableListOf
    }

    private fun getAudio(): AudioBean {
        return AudioBean(
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
            "",
            "小丸子",
            "",
            "",
            "",
            "小丸子",
            1,
            "",
            "文案",
            "",
            "",
            "小丸子", 1, 1, 1, "", 1
        )
    }

}