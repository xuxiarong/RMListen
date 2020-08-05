package com.lm.mvvmcore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initView()
        initData()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()

}