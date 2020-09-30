package com.rm.module_play.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.databinding.ActivityBookPlayerBinding
import com.rm.module_play.viewmodel.PlayViewModel

class CommentCenterActivity : BaseVMActivity<ActivityBookPlayerBinding, PlayViewModel>() {

    companion object {
        const val AUDIO_ID = "AUDIO_ID"
        fun toCommentCenterActivity(context: Context, audioID: String) {
            context.startActivity(Intent(context, CommentCenterActivity::class.java).apply {
                putExtra(AUDIO_ID, audioID)
            })
        }
    }


    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle("评论中心")
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
    }

    override fun getLayoutId(): Int = R.layout.activity_comment_center

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.audioCommentList.observe(this, Observer {
            mViewModel.mAdapter.setList(it)
        })
    }

    override fun initData() {
        intent.getStringExtra(AUDIO_ID)?.let {
            mViewModel.audioID.set(it)
            mViewModel.commentCenterAudioComments(it)
        }
    }


}