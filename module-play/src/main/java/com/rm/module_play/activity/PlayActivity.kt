package com.rm.module_play.activity

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.wedgit.seekbar.BubbleSeekBar
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity :
    BaseNetActivity<com.rm.module_play.databinding.ActivityPlayBinding, PlayViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_play
    override fun initView() {
        dataBind.viewModel = mViewModel
        val textView = TextView(this)
        textView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setBackgroundResource(R.drawable.bubble_bg)
        textView.text = ""
        textView.setTextColor(-0x1)
        textView.gravity = Gravity.CENTER
        music_play_bubbleSeekBar.addBubbleFL(textView)
        music_play_bubbleSeekBar.setOnProgressChangedListener(object : BubbleSeekBar.OnProgressChangedListener {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                val str ="${progress.toInt()}/${bubbleSeekBar?.getMax()?.toInt()}"
                music_play_bubbleSeekBar.updateThumbText(str)
                textView.text = str
            }

            override fun onStartTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

            override fun onStopTrackingTouch(bubbleSeekBar: BubbleSeekBar?) {
            }

        })
        music_play_bubbleSeekBar.setProgress(60f)
    }

    override fun startObserve() {
    }

    override fun initData() {
        ToastUtil.show(this, mViewModel.msg)
    }
}