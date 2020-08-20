package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.module_home.R
import com.rm.module_home.bean.BannerInfo
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuActivity : BaseNetActivity<HomeActivityListenMenuBinding, MenuViewModel>() {
    private val menuViewModel by viewModel<MenuViewModel>()

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MenuActivity::class.java))
        }
    }


    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    override fun initViewModel(): MenuViewModel = menuViewModel

    override fun startObserve() {
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        menuViewModel.baseTitleModel.value = baseTitleModel
        dataBind.run {
            viewModel = menuViewModel
        }
    }

    override fun initData() {
        val bannerList = mutableListOf<BannerInfo>()
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg"))
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg"))
        bannerList.add(BannerInfo("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg"))
        bannerList.add(BannerInfo("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg"))
        menuViewModel.bannerInfoList.value = bannerList

//        xbanner.setBannerData(bannerList)
//        xbanner.loadImage { _, _, view, position ->
//            loadImage(view as ImageView,bannerList[position].imgUrl)
//        }
    }
}