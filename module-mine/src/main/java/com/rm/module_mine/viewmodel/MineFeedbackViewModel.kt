package com.rm.module_mine.viewmodel

import android.Manifest
import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.EmojiUtils
import com.rm.baselisten.util.FileUtils
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineImageActivity
import com.rm.module_mine.bean.MineFeedbackImgBean
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

    val inputText = ObservableField("")

    val inputContact = ObservableField("")
    val contactVisibility = ObservableField(false)

    val tipText = ObservableField<String>("图片(0/5)")
    var photoHelp: CommonTakePhotoHelp? = null

    /**
     * 问题描述
     */
    val inputAction: (String) -> Unit = {
        if (it.length > 500) {
            showTip("文字超出最大限制", R.color.business_color_ff5e5e)
        }
        inputText.set(it)
        contactVisibility.set(false)
    }

    private val successList = mutableListOf<String>()
    val cameraList = mutableListOf<String>()

    private var uploadIndex = -1
    var checkedId = -1


    /**
     * 输入法是否显示
     */
    val keyboardIsVisibility = ObservableField<Boolean>(false)

    /**
     * 输入法显示/隐藏监听
     */
    var keyboardVisibility: (Boolean, Int) -> Unit = { it, _ -> keyboardVisibilityListener(it) }

    /**
     * 键盘的显示隐藏监听
     */
    private fun keyboardVisibilityListener(keyboardVisibility: Boolean) {
        keyboardIsVisibility.set(keyboardVisibility)
        contactVisibility.set(false)
    }

    /**
     * 联系方式
     */
    val inputContactAction: (String) -> Unit = {
        inputContact.set(it)
        contactVisibility.set(it.isNotEmpty() && keyboardIsVisibility.get() == true)
    }


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
                            successList.add(bean.path)
                            uploadIndex++
                            if (mAdapter.data.size > uploadIndex && !mAdapter.data[uploadIndex].path.isNullOrEmpty()) {
                                uploadPic(mAdapter.data[uploadIndex])
                            } else {
                                feedback()
                            }
                        },
                        onError = { msg, _ ->
                            showContentView()
                            showTip("$msg", R.color.business_color_ff5e5e)
                        }
                    )
                }
            }
        }
    }

    private fun feedback() {
        showContentView()
        val toTypedArray: Array<String> = successList.toTypedArray()
        launchOnIO {
            repository.mineFeedback(
                getUploadType(),
                inputText.get()!!,
                toTypedArray,
                inputContact.get()!!
            )
                .checkResult(
                    onSuccess = {
                        showContentView()
                        deleteCameraImage()
                        setResultAndFinish(1002)
                    },
                    onError = { msg, _ ->
                        showContentView()
                        showTip("$msg", R.color.business_color_ff5e5e)
                    }
                )
        }
    }

    private fun deleteCameraImage() {
        cameraList.forEach {
            FileUtils.delete(it)
        }
    }

    private fun getUploadType(): Int? {
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
                -1
            }
        }
    }

    /**
     * 提交
     */
    fun requestBook() {
        when {
            inputText.get()!!.trim().trimEnd().isEmpty() -> {
                showTip("反馈的内容不能为空", R.color.business_color_ff5e5e)
            }
            inputText.get()!!.length > 500 -> {
                showTip("字数最多不能超过500个", R.color.business_color_ff5e5e)
            }
            inputContact.get()!!.length > 50 -> {
                showTip("联系方式字数不能50个", R.color.business_color_ff5e5e)
            }
            mAdapter.data.size > 0 && !mAdapter.data[0].path.isNullOrEmpty() -> {
                showLoading()
                uploadIndex = 0
                uploadPic(mAdapter.data[0])
            }
            else -> {
                showLoading()
                feedback()
            }
        }

    }

    /**
     * 添加图片
     */
    fun clickCamera(context: Context) {
        getActivity(context)?.let {
            if (it is BaseActivity) {
                it.requestPermissionForResult(
                    mutableListOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), actionDenied = {
                        showTip(it.getString(R.string.business_listen_to_set_storage_permission))                    }, actionGranted = {
                        photoHelp = CommonTakePhotoHelp(activity = it)
                        photoHelp?.showTakePhoto()
                    },
                    actionPermanentlyDenied = {
                        showTip(it.getString(R.string.business_listen_to_set_storage_permission))
                    })
            }
        }
    }


    /**
     * 图片点击事件
     */
    fun clickImage(context: Context, bean: MineFeedbackImgBean) {
        MineImageActivity.startActivity(context, bean.path ?: "", R.drawable.base_ic_default)
    }

    /**
     * 删除图片
     */
    fun clickDeleteImg(bean: MineFeedbackImgBean) {
        mAdapter.remove(bean)
        if (mAdapter.data.indexOf(MineFeedbackImgBean("")) == -1) {
            mAdapter.addData(MineFeedbackImgBean(""))
        }
        changeText()
    }

    fun clickContact() {
        inputContact.set("")
    }

    fun addImageView(imgPath: String) {
        if (mAdapter.data.size < 6) {
            val bean = MineFeedbackImgBean(imgPath)
            mAdapter.addData(0, bean)
        }

        if (mAdapter.data.size == 6) {
            mAdapter.remove(MineFeedbackImgBean(""))
        }

        changeText()
    }

    private fun changeText() {
        var index = -1
        mAdapter.data.forEachIndexed { i, bean ->
            if (TextUtils.equals("", bean.path)) {
                index = i
            }
        }
        if (index == -1) {
            tipText.set("图片(${mAdapter.data.size}/5)")
        } else {
            tipText.set("图片(${mAdapter.data.size - 1}/5)")
        }
    }
}
