package com.rm.module_home.viewmodel

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableField
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.loginUser
import com.rm.business_lib.net.BusinessRetrofitClient
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
    private val baseViewModel: BaseVMViewModel,
    private val audioId: String,
    private val commentSuccessBlock: () -> Unit
) : BaseVMViewModel() {

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
            initDialog = {
                dataBinding = mDataBind as HomeDialogCommentBinding
                dataBinding?.apply {
                    initDialog = {
                        home_dialog_comment_ed.isFocusable = true
                        home_dialog_comment_ed.requestFocus()
                        home_dialog_comment_ed.isFocusableInTouchMode = true;
                        val inputManager =
                            home_dialog_comment_ed.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }
                }
            }
        }
    }

    private fun inputChange(content: String) {
        inputComment.set(content.trim().trimEnd())
    }

    fun clickSend(view: View) {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
        inputComment.get()?.let {
            sendComment(it, audioId, loginUser.get()!!.id)
        }
    }

    private fun sendComment(
        content: String,
        audio_id: String,
        anchor_id: String
    ) {
        launchOnIO {
            repository.homeSendComment(content, audio_id, anchor_id).checkResult(
                onSuccess = {
                    commentSuccessBlock()
                    mDialog.dismiss()
                },
                onError = {
//                    mDialog.dismiss()
                    baseViewModel.showToast("$it")
                }
            )
        }
    }
}