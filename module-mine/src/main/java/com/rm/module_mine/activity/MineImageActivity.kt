package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_mine.R
import kotlinx.android.synthetic.main.mine_activity_image.*

/**
 *
 * @author yuan fang
 * @date 11/10/20
 * @description 图片查看
 *
 */
class MineImageActivity : BaseActivity() {
    companion object {
        const val IMAGE_URL = "imageUrl"
        fun startActivity(context: Context, imageUrl: String) {
            context.startActivity(
                Intent(context, MineImageActivity::class.java).putExtra(
                    IMAGE_URL,
                    imageUrl
                )
            )
        }
    }

    override fun getLayoutId() = R.layout.mine_activity_image


    override fun initData() {
        setTransparentStatusBar()
        intent.getStringExtra(IMAGE_URL)?.let {
            mine_image.bindUrl(bindUrl = it)
        }
        mine_image.setOnClickListener { finish() }
    }
}