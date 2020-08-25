package com.rm.module_home.activity.boutique

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.observe
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.module_home.R
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.databinding.HomeActivityBoutiqueBinding
import kotlinx.android.synthetic.main.home_activity_boutique.*

/**
 * 精品推荐界面
 */
class BoutiqueActivity :
    BaseNetActivity<HomeActivityBoutiqueBinding, BoutiqueViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BoutiqueActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_boutique

    override fun startObserve() {
        mViewModel.tabList.observe(this) {
            home_boutique_view_pager.adapter = BoutiqueViewPageAdapter(supportFragmentManager, it)
            home_boutique_tab_layout.setupWithViewPager(home_boutique_view_pager)
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_boutique))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
        dataBind.run {
            viewModel = mViewModel
        }
    }

    override fun initData() {
        mViewModel.getTabListInfo()
    }
}

private class BoutiqueViewPageAdapter(
    fm: FragmentManager,
    private val tabList: List<CategoryTabBean>
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return BoutiqueFragment(tabList[position])
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].name
    }
}