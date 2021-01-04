package com.rm.module_mine.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.rm.baselisten.binding.bindKeyboardVisibilityListener
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.FileUtils
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.Drawable
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.utils.BusinessCameraAndAlbum.Companion.ALBUM_REQUEST_CODE
import com.rm.business_lib.utils.BusinessCameraAndAlbum.Companion.CAMERA_REQUEST_CODE
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityFeedbackBinding
import com.rm.module_mine.util.BitmapUtil
import com.rm.module_mine.viewmodel.MineFeedbackViewModel
import kotlinx.android.synthetic.main.mine_activity_feedback.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MimeFeedbackActivity : BaseVMActivity<MineActivityFeedbackBinding, MineFeedbackViewModel>(),
    View.OnFocusChangeListener {
    companion object {
        const val FEEDBACK_REQUEST_CODE = 1001
        const val FEEDBACK_RESULT_CODE = 1002
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, MimeFeedbackActivity::class.java),
                FEEDBACK_REQUEST_CODE
            )
        }
    }

    private val checkBoxList = mutableListOf<AppCompatTextView>()
    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.mine_activity_feedback
    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setTitle(getString(R.string.mine_feedback))
            .setLeftIcon(R.drawable.business_icon_return_bc)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

        checkBoxList.add(mine_feedback_one)
        checkBoxList.add(mine_feedback_two)
        checkBoxList.add(mine_feedback_three)
        checkBoxList.add(mine_feedback_four)

        mine_feedback_content.onFocusChangeListener = this
        mine_feedback_ed_contact.onFocusChangeListener = this

        onClickListener()


        mDataBind.mineFeedbackContent.bindKeyboardVisibilityListener { it, keyboardHeight ->
            if (it) {
                mDataBind.mineFeedbackView.apply {
                    layoutParams.height = keyboardHeight - dip(80)
                    visibility = View.VISIBLE
                }
                val contactNotEmpty = mViewModel.inputContact.get()!!.isNotEmpty()
                val hasFocus = mDataBind.mineFeedbackEdContact.hasFocus()

                mViewModel.contactVisibility.set(contactNotEmpty && it && hasFocus)
                mViewModel.contactVisibility.notifyChange()
            } else {
                mDataBind.mineFeedbackView.visibility = View.GONE
            }
        }
    }

    override fun startObserve() {
    }

    override fun initData() {
    }

    private fun onClickListener() {
        checkBoxList.forEach { checkBox ->
            checkBox.tag = false
            checkBox.setOnClickListener { changeState(checkBox) }
        }
    }

    private fun changeState(textView: AppCompatTextView) {
        checkBoxList.forEach {
            if (it == textView) {
                if (it.tag == false) {
                    mViewModel.checkedId = it.id
                    changeTextStyle(it, true)
                } else {
                    mViewModel.checkedId = -1
                    changeTextStyle(it, false)
                }
            } else {
                changeTextStyle(it, false)
            }
        }
    }

    private fun changeTextStyle(textView: AppCompatTextView, isCheck: Boolean) {
        if (isCheck) {
            textView.apply {
                tag = true
                setTextColor(Color(R.color.business_text_color_ffffff))
                background = Drawable(R.drawable.mine_feedback_check_select)
            }
        } else {
            textView.apply {
                tag = false
                setTextColor(Color(R.color.business_color_b1b1b1))
                background = Drawable(R.drawable.mine_feedback_check_un_select)
            }
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.mine_feedback_ed_contact -> {
                val contactNotEmpty = mViewModel.inputContact.get()!!.isNotEmpty()
                val keyboardIsVisibility = mViewModel.keyboardIsVisibility.get()
                mViewModel.contactVisibility.set(contactNotEmpty && keyboardIsVisibility == true && hasFocus)
                mViewModel.contactVisibility.notifyChange()
            }
            else -> {
                mViewModel.contactVisibility.set(false)
                mViewModel.contactVisibility.notifyChange()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mViewModel.photoHelp?.getCameraUri()?.let {
                        val path = FileUtils.getPath(this, it)
                        path?.let { filePath ->
                            compress(filePath, true)
                        }
                    }
                } else {
                    val cameraImagePath = mViewModel.photoHelp?.getCameraImagePath()
                    cameraImagePath?.let {
                        compress(it, true)
                    }
                }
            } else {
                Toast.makeText(this, "拍照被取消", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == ALBUM_REQUEST_CODE) {
            data?.data?.let {
                val path = FileUtils.getPath(this, it)
                path?.let { filePath ->
                    compress(filePath, false)
                }
            }
        }
    }

    /**
     * 将图片进行压缩
     */
    private fun compress(path: String, isDelete: Boolean) {
        Luban.with(this)
            .load(path)
            .ignoreBy(80)
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(file: File) {
                    mViewModel.addImageView(file.path)
                    if (isDelete) {
                        FileUtils.delete(path)
                        mViewModel.cameraList.add(file.path)
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
            .launch()
    }

}