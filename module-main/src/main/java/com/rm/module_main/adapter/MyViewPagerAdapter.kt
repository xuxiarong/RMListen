package com.rm.module_main.adapter

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.component_comm.search.SearchService
import com.rm.module_main.TestFragment

//class MyViewPagerAdapter(fm: FragmentManager, private val size: Int) :
//    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//    private val fragments = mutableListOf<Fragment>()
//    override fun getItem(position: Int): Fragment {
//        val fragment = when (position) {
//            0 -> {
//                RouterHelper.createRouter(HomeService::class.java).getHomeFragment()
//            }
//            1 -> {
//                RouterHelper.createRouter(SearchService::class.java).getSearchFragment()
//            }
//            2 -> {
//                RouterHelper.createRouter(ListenService::class.java).getListenFragment()
//            }
//            3 -> {
//                RouterHelper.createRouter(MineService::class.java).getMineFragment()
//            }
//            else -> {
//                TestFragment().apply {
//                    arguments = Bundle().apply { putString("title", position.toString()) }
//                }
//            }
//        }
//        fragments.add(fragment)
//        return fragment
//    }
//
//    fun getFragments(): MutableList<Fragment> {
//        return fragments
//    }
//
//    override fun getCount(): Int {
//        return size
//    }
//
//}

class MyViewPagerAdapter(activity: FragmentActivity, private val size: Int) :
    FragmentStateAdapter(activity) {
    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> {
                RouterHelper.createRouter(HomeService::class.java).getHomeFragment()
            }
            1 -> {
                RouterHelper.createRouter(SearchService::class.java).getSearchFragment()
            }
            2 -> {
                RouterHelper.createRouter(ListenService::class.java).getListenFragment()
            }
            3 -> {
                RouterHelper.createRouter(MineService::class.java).getMineFragment()
            }
            else -> {
                TestFragment().apply {
                    arguments = Bundle().apply { putString("title", position.toString()) }
                }
            }
        }
        fragments.add(fragment)
        return fragment
    }

    fun getFragments(): MutableList<Fragment> {
        return fragments
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        //在绑定recycleView的时候将缓存设置为4个，让fragment不会被回收
        recyclerView.setItemViewCacheSize(4)
        super.onAttachedToRecyclerView(recyclerView)
    }

}