package com.rm.module_home.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.databinding.HomeDialogCommentBinding

/**
 *
 * @author yuanfang
 * @date 10/16/20
 * @description
 *
 */
class HomeCommentDialogViewModel(private val baseViewModel: BaseVMViewModel) :
    BaseVMViewModel() {

    private var dataBinding: HomeDialogCommentBinding? = null

    val inputText: (String) -> Unit = { inputComment.set(it) }

    val inputComment = ObservableField<String>()

    /**
     * 懒加载创建dialog对象
     */
    val mDialog by lazy {
        CommBottomDialog().apply {
            initDialog = {
                dataBinding = mDataBind as HomeDialogCommentBinding
                dataBinding?.apply {

                }
            }
        }
    }
}