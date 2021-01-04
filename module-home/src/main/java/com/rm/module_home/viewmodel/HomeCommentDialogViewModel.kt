package com.rm.module_home.viewmodel

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.model.BaseToastModel
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.receiver.NetworkChangeReceiver
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_home.R
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.databinding.HomeDialogCommentBinding
import com.rm.module_home.repository.HomeRepository
import kotlinx.android.synthetic.main.home_dialog_comment.*

/**
 *
 * @author yuanfang
 * @date 10/16/20
 * @description
 *
 */
class HomeCommentDialogViewModel(
    private val audioId: String,
    private val anchorId: String,
    private val viewModel: BaseVMViewModel,
    private val commentSuccessBlock: () -> Unit
) : BaseVMViewModel() {
    init {
        NetworkChangeReceiver.isAvailable.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (!NetworkChangeReceiver.isAvailable.get()) {
                    viewModel.showErrorToast("当前网络不可用")
                }
            }
        })
    }

    private val repository by lazy {
        HomeRepository(BusinessRetrofitClient().getService(HomeApiService::class.java))
    }
    private var dataBinding: HomeDialogCommentBinding? = null

    val inputText: (String) -> Unit = { inputChange(it) }

    val inputComment = ObservableField<String>()

    /**
     * 懒加载创建dialog对象
     */
    val mDialog by lazy {
        CommBottomDialog().apply {
            dialogHeightIsMatchParent = true
            initDialog = {
                dataBinding = mDataBind as HomeDialogCommentBinding

                dialog?.setOnShowListener {
                    home_dialog_comment_ed?.apply {
                        postDelayed({
                            isFocusable = true
                            requestFocus()
                            isFocusableInTouchMode = true
                            val inputManager =
                                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                        }, 50)
                    }
                }
            }
        }
    }


    /**
     * 输入框发生变化
     */
    private fun inputChange(content: String) {
        inputComment.set(content.trim().trimEnd())
        if (inputComment.get()!!.length > 200) {
            viewModel.showErrorToast(CONTEXT.getString(R.string.home_comment_input_limit))
        }
    }


    /**
     * 点击发送按钮
     */
    fun clickSend(view: View) {
        inputComment.get()?.let {
            if (it.length > 200) {
                viewModel.showErrorToast(CONTEXT.getString(R.string.home_comment_input_limit))
            } else {
                viewModel.showToast(BaseToastModel(content = "提交评论中", canAutoCancel = false))
                if (NetworkChangeReceiver.isAvailable.get()) {
                    sendComment(view, it, audioId)
                } else {
                    viewModel.showErrorToast("当前网络不可用")
                }
            }
        }
    }

    fun clickLayout() {
        //防止点击消失dialog
    }

    /**
     * 发送评论
     */
    private fun sendComment(
        view: View,
        content: String,
        audio_id: String
    ) {
        launchOnIO {
            repository.homeSendComment(content, audio_id, anchorId).checkResult(
                onSuccess = {
                    val imm =
                        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                    }
                    viewModel.cancelToast()
                    commentSuccessBlock()
                    mDialog.dismiss()
                },
                onError = { it, _ ->
                    viewModel.showErrorToast("$it")
                }
            )
        }
    }
}