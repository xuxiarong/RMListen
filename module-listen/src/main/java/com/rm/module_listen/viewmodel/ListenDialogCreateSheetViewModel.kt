package com.rm.module_listen.viewmodel

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_ADD_SHEET
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.databinding.ListenDialogCreateSheetBinding
import com.rm.module_listen.repository.ListenPatchSheetBean
import com.rm.module_listen.repository.ListenRepository

class ListenDialogCreateSheetViewModel(
    private val mActivity: FragmentActivity,
    private val baseViewModel: BaseVMViewModel
) :
    BaseVMViewModel() {

    private val repository by lazy {
        ListenRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    //  输入的密码值
    val inputText = ObservableField<String>("")

    //编辑成功
    var editSuccess: (String) -> Unit = {}

    //编辑的听单
    val sheetId = ObservableField<String>()

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { inputText.set(it) }

    var audioId = ""

    var dataBinding: ListenDialogCreateSheetBinding? = null


    /**
     * 懒加载创建dialog对象
     */
    var mDialog: CommonDragMvDialog? = null

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
                    if (TextUtils.isEmpty(audioId)) {
                        baseViewModel.showToast(CONTEXT.getString(R.string.listen_add_success_tip))
                        mDialog?.dismiss()
                    } else {
                        addSheet(it.sheet_id)
                    }
                    baseViewModel.showContentView()
                },
                onError = {
                    baseViewModel.showContentView()
                    showErrorTip(it)
                }
            )
        }
    }

    /**
     * 添加到听单列表
     */
    private fun addSheet(sheetId: String) {
        launchOnIO {
            repository.addSheetList(sheetId, audioId).checkResult(
                onSuccess = {
                    baseViewModel.showContentView()
                    addSheetSuccess(sheetId)
                },
                onError = {
                    baseViewModel.showContentView()

                    showErrorTip(it)
                }
            )
        }
    }

    /**
     * 添加成功
     */
    private fun addSheetSuccess(sheetId: String) {
        if (IS_FIRST_ADD_SHEET.getBooleanMMKV(true)) {
            CustomTipsFragmentDialog().apply {
                titleText = mActivity.getString(R.string.listen_add_success)
                contentText = mActivity.getString(R.string.listen_add_success_content)
                leftBtnText = mActivity.getString(R.string.listen_know)
                rightBtnText = mActivity.getString(R.string.listen_goto_look)
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startListenSheetList(
                        mActivity,
                        LISTEN_SHEET_LIST_MY_LIST
                    )
                    dismiss()
                }
                customView =
                    ImageView(mActivity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(mActivity)
        } else {
            if (mActivity is BaseActivity) {
                mActivity.tipView.showTipView(mActivity, "添加成功")
            } else {
                baseViewModel.showToast(mActivity.getString(R.string.listen_add_success_tip))
            }
        }
        mDialog?.dismiss()
        IS_FIRST_ADD_SHEET.putMMKV(false)
    }

    private fun showErrorTip(msg: String?) {
        if (mActivity is BaseActivity) {
            mActivity.tipView.showTipView(
                mActivity,
                tipText = "$msg",
                tipColor = R.color.business_color_ff5e5e
            )
        } else {
            baseViewModel.showToast("$msg")
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
                    mDialog?.dismiss()
                },
                onError = {
                    baseViewModel.showContentView()
                    showErrorTip(it)
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
        mDialog?.dismiss()
    }

    /**
     * 确认点击事件
     */
    fun clickSureFun() {
        checkText()
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