package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.net.util.GsonUtils
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineImageActivity
import com.rm.module_mine.bean.MineFeedbackImgBean
import com.rm.module_mine.bean.MineUploadPic
import com.rm.module_mine.repository.MineRepository
import com.rm.module_mine.util.CommonTakePhotoHelp

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

    private val successList = mutableListOf<MineUploadPic>()
    private val failureList = mutableListOf<MineFeedbackImgBean>()
    private var uploadIndex = -1

    var checkedId = -1
    val radioCheckedChange: (Int) -> Unit = {
        checkedId = it
    }

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

    private fun uploadPic(bean: MineFeedbackImgBean) {
        bean.path?.let {
            if (it.isNotEmpty()) {
                launchOnIO {
                    repository.uploadCommon(it).checkResult(
                        onSuccess = { bean ->
                            successList.add(bean)
                            uploadIndex++
                            if (mAdapter.data.size > uploadIndex && !mAdapter.data[uploadIndex].path.isNullOrEmpty()) {
                                uploadPic(mAdapter.data[uploadIndex])
                            } else {
                                feedback()
                            }
                        },
                        onError = { msg ->
                            showContentView()
                            failureList.add(bean)
                            showTip("$msg", R.color.business_color_ff5e5e)
                        }
                    )
                }
            }
        }
    }

    private fun feedback() {
        showContentView()
        DLog.i("=========>", "$successList   ${getUploadType()}")
//        GsonUtils.toJson()
//        launchOnIO {
//            repository.mineFeedback(getUploadType(),inputText.get()!!,,inputContact).checkResult(
//                onSuccess = {
//                    showContentView()
//                    showTip("反馈成功")
//                },
//                onError = {msg->
//                    showContentView()
//                    showTip("$msg", R.color.business_color_ff5e5e)
//                }
//            )
//        }
    }

    private fun getUploadType(): Int {
        return when (checkedId) {
            R.id.mine_feedback_one -> {
                2
            }
            R.id.mine_feedback_two -> {
                1
            }
            R.id.mine_feedback_three -> {
                3
            }
            R.id.mine_feedback_four -> {
                0
            }
            else -> {
                0
            }
        }


    }

    /**
     * 提交
     */
    fun requestBook() {
        if (inputText.get()!!.trim().trimEnd().isEmpty()) {
            showTip("反馈的内容不能为空", R.color.business_color_ff5e5e)
            return
        }

        if (mAdapter.data.size > 0 && !mAdapter.data[0].path.isNullOrEmpty()) {
            showLoading()
            uploadIndex = 0
            uploadPic(mAdapter.data[0])
        } else {
            showLoading()
            feedback()
        }
    }

    /**
     * 添加图片
     */
    fun clickCamera(context: Context) {
        getActivity(context)?.let {
            CommonTakePhotoHelp(
                activity = it,
                isCropPic = false,
                onSuccess = { path ->
                    addImageView(path)
                },
                onFailure = {

                }).showTakePhoto()
        }
    }

    /**
     * 图片点击事件
     */
    fun clickImage(context: Context, bean: MineFeedbackImgBean) {
        MineImageActivity.startActivity(context, bean.path ?: "")
    }

    /**
     * 删除图片
     */
    fun clickDeleteImg(bean: MineFeedbackImgBean) {
        mAdapter.remove(bean)

        if (mAdapter.data.indexOf(MineFeedbackImgBean("")) == -1) {
            mAdapter.addData(MineFeedbackImgBean(""))
        }
    }

    private fun addImageView(imgPath: String) {
        if (mAdapter.data.size < 6) {
            val bean = MineFeedbackImgBean(imgPath)
            mAdapter.addData(0, bean)
        }

        if (mAdapter.data.size == 6) {
            mAdapter.remove(MineFeedbackImgBean(""))
        }

    }
}
