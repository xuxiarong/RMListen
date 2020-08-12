package com.rm.module_home

import android.content.Context
import android.content.Intent
import com.rm.baselisten.activity.BaseActivity

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
class TestActivity : BaseActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TestActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_test

    override fun initView() {
    }

    override fun initData() {
    }
}