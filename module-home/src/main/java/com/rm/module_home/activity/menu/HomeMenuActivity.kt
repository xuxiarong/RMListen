package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Observable
import androidx.lifecycle.observe
import com.rm.baselisten.model.BaseTitleModel
import com.rm.business_lib.binding.paddingBindData
import com.rm.business_lib.xbanner.XBanner
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import com.rm.module_home.viewmodel.HomeMenuViewModel

class HomeMenuActivity :
    ComponentShowPlayActivity<HomeActivityListenMenuBinding, HomeMenuViewModel>() {

    //懒加载头部banner
    private val headView by lazy {
        View.inflate(this, R.layout.home_header_banner, null)
    }
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeMenuActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    override fun startObserve() {
        mViewModel.menuList.observe(this) {
            //设置banner数据源
            headView.findViewById<XBanner>(R.id.home_head_banner).apply {
                paddingBindData(it.banner_list)
//                setIsClipChildrenMode(false)
                setOnItemClickListener { _, _, _, position ->
                    BannerJumpUtils.onBannerClick(context, it.banner_list!![position].banner_jump)
                }
            }
        }
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
        mViewModel.menuAdapter.addHeaderView(headView)

    }

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getMenuListInfo()
        mViewModel.getSheetList()
    }

}