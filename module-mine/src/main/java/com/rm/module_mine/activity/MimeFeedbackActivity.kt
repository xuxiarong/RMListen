package com.rm.module_mine.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityFeedbackBinding
import com.rm.module_mine.viewmodel.MineFeedbackViewModel

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MimeFeedbackActivity : BaseVMActivity<MineActivityFeedbackBinding, MineFeedbackViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MimeFeedbackActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.mine_activity_feedback
    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setTitle(getString(R.string.mine_feedback))
            .setLeftIcon(R.drawable.business_icon_return_bc)
            .setLeftIconClick { finish() }

        mViewModel.baseTitleModel.value = titleModel
    }

    override fun startObserve() {
    }

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.i("----->onActivityResult", "requestCode:$requestCode   resultCode:$resultCode ")
        if (requestCode == 100) {
            when (resultCode) {
                MineCropActivity.RESULT_CODE_CROP -> {
                    data?.getStringExtra(MineCropActivity.FILE_PATH)?.let {
                        DLog.i("----->onActivityResult", "CROP $it")
                        mViewModel.addImageView(it)
                    }
                }
            }
        }
    }

}