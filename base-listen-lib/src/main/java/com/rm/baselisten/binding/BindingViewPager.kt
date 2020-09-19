package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * desc   :
 * date   : 2020/09/19
 * version: 1.0
 */
@BindingAdapter("bindAdapter")
fun ViewPager.bindAdapter(adapter: FragmentPagerAdapter?){
    if(adapter == null) return
    setAdapter(adapter)
}