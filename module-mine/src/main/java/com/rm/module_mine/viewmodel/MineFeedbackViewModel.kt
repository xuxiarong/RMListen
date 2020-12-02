package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.bean.MineFeedbackImgBean
import com.rm.module_mine.repository.MineRepository
import com.rm.module_mine.util.CommentTakePhotoHelp

/**
 *
 * @author yuan fang
 * @date 9/21/20
 * @description
 *
 */
class MineFeedbackViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    var inputText = ObservableField("")

    private var inputContact = ""

    /**
     * 问题描述
     */
    val inputAction: (String) -> Unit = { inputText.set(it) }


    /**
     * 联系方式
     */
    val inputContactAction: (String) -> Unit = { inputContact = it }

    val mAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf(MineFeedbackImgBean("")),
            R.layout.mine_adapter_feedback,
            BR.feedbackViewModel,
            BR.feedbackItem
        )
    }

    /**
     * 提交
     */
    fun requestBook() {

    }

    /**
     * 添加图片
     */
    fun clickCamera(context: Context) {
        getActivity(context)?.let {
            CommentTakePhotoHelp(it,false).showTakePhoto()
        }
    }

    fun addImageView(imgPath: String) {
        if (mAdapter.data.size < 5) {
            val bean = MineFeedbackImgBean(imgPath)
            mAdapter.addData(0,bean)
        }

    }
}
