package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * desc   :
 * date   : 2020/09/19
 * version: 1.0
 */
@BindingAdapter("bindAdapter","bindingOffscreenPageLimit",requireAll = false)
fun ViewPager.bindAdapter(adapter: FragmentPagerAdapter?,pageLimit:Int?){
    if(adapter != null){
        setAdapter(adapter)
    }
    if(pageLimit!= null && pageLimit > 0){
        offscreenPageLimit = pageLimit
    }
}