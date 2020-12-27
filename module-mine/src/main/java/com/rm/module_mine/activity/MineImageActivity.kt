package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
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
        const val DEFAULT_RES_ID = "defaultResId"
            fun startActivity(context: Context, imageUrl: String, defaultResId: Int) {
            context.startActivity(
                Intent(context, MineImageActivity::class.java)
                    .putExtra(IMAGE_URL, imageUrl)
                    .putExtra(DEFAULT_RES_ID, defaultResId)
            )
        }
    }

    override fun getLayoutId() = R.layout.mine_activity_image


    override fun initData() {
        setTransparentStatusBar()
        intent.getStringExtra(IMAGE_URL)?.let {
            val defaultResId =
                intent.getIntExtra(DEFAULT_RES_ID, R.drawable.base_ic_default)
            mine_image.bindUrl(
                bindUrl = it,
                defaultIcon = ContextCompat.getDrawable(this, defaultResId)
            )
        }
        mine_image.setOnClickListener { finish() }
    }
}