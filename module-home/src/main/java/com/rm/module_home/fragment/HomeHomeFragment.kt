package com.rm.module_home.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.HomeBannerModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment:BaseVMFragment<HomeHomeFragmentBinding>(R.layout.home_home_fragment) {

    private val homeViewModel by viewModel<HomeFragmentViewModel>()


    override fun initData() {
        binding.mainBanner.visibility = View.VISIBLE
    }

    override fun startObserve() {
        homeViewModel.homeBannerModel.observe(this, Observer {
            homeViewModel.homeBannerModel.value = HomeBannerModel("")
        })
    }

    companion object{
        fun getInstance(){
//            val transaction: FragmentTransaction = .beginTransaction()
//            transaction.add(frameId, fragment)
//            transaction.commit()
        }
    }

}