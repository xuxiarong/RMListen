package com.rm.module_home.activity.boutique

import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.repository.BoutiqueRepository

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
class BoutiqueViewModel(private val repository: BoutiqueRepository) :
    BaseVMViewModel() {
    // 类别标签列表
    private val tabList = mutableListOf<CategoryTabBean>()
    private val bannerList = mutableListOf<BannerInfoBean>()
    var tabSize = ObservableInt()

    var fragmentManager: FragmentManager? = null

    val tabAdapter by lazy {
        BoutiqueViewPageAdapter(fragmentManager!!, tabList, bannerList)
    }

    fun getTabListInfo() {
        showLoading()
        launchOnIO {
            repository.getTabList().checkResult(
                onSuccess = {
                    showContentView()
                    tabList.addAll(it.class_list)
                    bannerList.addAll(it.banner_list)
                    tabAdapter.notifyDataSetChanged()
                    tabSize.set(tabList.size)
                },
                onError = {
                    showServiceError()
                }
            )
        }
    }
}

class BoutiqueViewPageAdapter(
    fm: FragmentManager,
    private val tabList: List<CategoryTabBean>,
    private val bannerList: List<BannerInfoBean>
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return BoutiqueFragment(tabList[position], bannerList)
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].class_name
    }
}