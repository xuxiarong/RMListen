package com.rm.module_listen.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.databinding.ListenDialogCreateSheetBinding
import com.rm.module_listen.repository.ListenDialogCreateSheetRepository
import com.rm.module_listen.repository.ListenPatchSheetBean

class ListenDialogCreateSheetViewModel(private val baseViewModel: BaseVMViewModel) :
    BaseVMViewModel() {

    private val repository by lazy {
        ListenDialogCreateSheetRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    //  输入的密码值
    private val inputText = ObservableField<String>("")

    //标题
    val titleData = ObservableField<String>("")

    //编辑成功
    var editSuccess: (String) -> Unit = {}

    //编辑的听单
    val sheetId = ObservableField<String>()

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { inputChange(it) }


    private var dataBinding: ListenDialogCreateSheetBinding? = null


    /**
     * 懒加载创建dialog对象
     */
    val mDialog by lazy {
        CommBottomDialog().apply {
            initDialog = {
                dataBinding = mDataBind as ListenDialogCreateSheetBinding
                dataBinding?.apply {

                    if (!TextUtils.isEmpty(titleData.get())) {
                        listenDialogCreateSheetTitle.text = titleData.get()
                    }

                    listenDialogCreateSheetTopDragRl.setDialog(dialog)

                    listenDialogCreateSheetTopDragRl.seDragCloseListener {
                        dismiss()
                    }
                }
            }
        }
    }

    /**
     * 创建听单
     *
     * @param sheet_name 听单名称
     * @param description 听单简介
     */
    private fun createSheet(sheet_name: String, description: String) {
        baseViewModel.showLoading()
        launchOnIO {
            repository.createSheet(sheet_name, description).checkResult(
                onSuccess = {
                    baseViewModel.showContentView()
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_add_success_tip))
                    mDialog.dismiss()
                },
                onError = {
                    baseViewModel.showContentView()
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_add_fail))
                }
            )
        }
    }


    /**
     * 编辑听单
     *
     * @param bean 听单json
     */
    private fun editSheet(bean: ListenPatchSheetBean) {
        baseViewModel.showLoading()
        launchOnIO {
            repository.editSheet(bean).checkResult(
                onSuccess = {
                    baseViewModel.showContentView()
                    editSuccess(bean.sheet_name)
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_edit_success_tip))
                    mDialog.dismiss()
                },
                onError = {
                    baseViewModel.showContentView()
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_edit_fail))
                }
            )
        }
    }


    /**
     * 检测文本信息
     */
    private fun checkText() {
        if (inputText.get() != null) {
            val str = inputText.get()!!
            val description =
                dataBinding?.listenDialogCreateSheetEditSynopsis?.text.toString()
            when {
                containsEmoji(str) -> {
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_input_char_tip))
                }
                numberOfWords(str) -> {
                    baseViewModel.showToast(CONTEXT.getString(R.string.listen_input_length_tip))
                }
                else -> {
                    if (sheetId.get() == null) {
                        createSheet(str, description)
                    } else {
                        editSheet(ListenPatchSheetBean(str, description, sheetId.get()!!))
                    }
                }
            }
        } else {
            baseViewModel.showToast(CONTEXT.getString(R.string.listen_input_title))
        }
    }

    /**
     * 取消点击事件
     */
    fun clickCancelFun() {
        mDialog.dismiss()
    }

    /**
     * 确认点击事件
     */
    fun clickSureFun() {
        checkText()
    }

    /**
     * 文本框发生变化
     */
    private fun inputChange(str: String) {
        if (str.isNotEmpty()) {
            inputText.set(str)
            dataBinding?.listenDialogCreateSheetSure?.apply {
                isEnabled = true
                setTextColor(Color(R.color.business_color_ff5e5e))
            }
        } else {
            dataBinding?.listenDialogCreateSheetSure?.apply {
                isEnabled = false
                setTextColor(Color(R.color.business_color_e8e8e8))
            }
        }
    }


    /**
     * 字符长度是否大于20
     */
    private fun numberOfWords(str: String): Boolean {
        return str.length > 20
    }

    /**
     * 判断是否是特殊字符（表情）
     */
    private fun containsEmoji(str: String): Boolean {
        str.forEach {
            return !isEmojiCharacter(it)
        }
        return false
    }

    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA ||
                codePoint.toInt() == 0xD || codePoint.toInt() in 0x20..0xD7FF ||
                codePoint.toInt() in 0xE000..0xFFFD || (codePoint.toInt() in 0x10000..0x10FFFF)
    }

}