package com.rm.business_lib.wedgit.coordinator

import com.google.android.material.tabs.TabLayout

interface OnTabSelectedListener {
    fun onTabSelected(tab: TabLayout.Tab?)
    fun onTabUnselected(tab: TabLayout.Tab?)
    fun onTabReselected(tab: TabLayout.Tab?)
}