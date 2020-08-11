package com.rm.baselisten.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseVMActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
        initView()
        initData()
    }

    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()
}