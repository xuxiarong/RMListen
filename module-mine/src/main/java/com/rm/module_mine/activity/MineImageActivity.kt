package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.binding.bindUrl
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityImageBinding

/**
 *
 * @author yuan fang
 * @date 11/10/20
 * @description 图片查看
 *
 */
class MineImageActivity : ComponentShowPlayActivity<MineActivityImageBinding, BaseVMViewModel>() {
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

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initData() {
//        val titleModel = BaseTitleModel()
//            .setLeftIcon(R.drawable.business_icon_return_bc)
//            .setLeftIcon1Click { finish() }
//
//        mViewModel.baseTitleModel.value = titleModel
        intent.getStringExtra(IMAGE_URL)?.let {
            mDataBind.mineImage.bindUrl(bindUrl = it)
        }
        mDataBind.mineImage.setOnClickListener { finish() }
    }
}