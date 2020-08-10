package com.rm.baselisten.mvvm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * desc   : 基类的MvvmActivity
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseVMActivity : AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(
            @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId).apply {
        lifecycleOwner = this@BaseVMActivity
    } }

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