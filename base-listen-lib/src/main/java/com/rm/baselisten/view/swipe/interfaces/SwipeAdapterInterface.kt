package com.rm.baselisten.view.swipe.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun getLottieResourceId(position: Int): Int
    fun notifyDatasetChanged()
}