package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeDetailContentBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel

/**
 * 书籍详情
 */
class DetailActivity : BaseNetActivity<HomeDetailContentBinding,HomeDetailViewModel>() {


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DetailActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.home_detail_content

    override fun startObserve() {

    }

    override fun initData() {

    }
}