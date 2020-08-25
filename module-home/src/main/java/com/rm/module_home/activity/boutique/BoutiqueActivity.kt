package com.rm.module_home.activity.boutique

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityBoutiqueRecommendBinding
import com.rm.module_main.adapter.HomeTestViewPagerAdapter
import kotlinx.android.synthetic.main.home_activity_boutique_recommend.*

/**
 * 精品推荐界面
 */
class BoutiqueActivity :
    BaseNetActivity<HomeActivityBoutiqueRecommendBinding, BoutiqueViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BoutiqueActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_boutique_recommend

    override fun startObserve() {
        mViewModel.tabList.observe(this) {
            home_boutique_view_pager.adapter = HomeTestViewPagerAdapter(supportFragmentManager, it)
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