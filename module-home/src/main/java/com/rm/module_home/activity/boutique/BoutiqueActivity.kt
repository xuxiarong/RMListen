package com.rm.module_home.activity.boutique

import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import com.rm.baselisten.model.BaseTitleModel
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityBoutiqueBinding
import kotlinx.android.synthetic.main.home_activity_boutique.*

/**
 * 精品推荐界面
 */
class BoutiqueActivity :
    ComponentShowPlayActivity<HomeActivityBoutiqueBinding, BoutiqueViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, BoutiqueActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_activity_boutique

    override fun initModelBrId() = BR.viewModel


    override fun startObserve() {
        mViewModel.tabSize.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                home_boutique_view_pager.offscreenPageLimit = mViewModel.tabSize.get()
            }
        })
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_boutique))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel

        mViewModel.fragmentManager = supportFragmentManager
        home_boutique_tab_layout.setupWithViewPager(home_boutique_view_pager)

    }

    override fun initData() {
        mViewModel.getTabListInfo()
    }
}