package com.rm.business_lib.swipe.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun getLottieResourceId(position: Int): Int
    fun notifyDatasetChanged()
}