package com.rm.module_listen.viewmodel

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.EmojiUtils
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_ADD_SHEET
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.loginUser
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
    private val successBlock: () -> Unit
) : BaseVMViewModel() {

    private val repository by lazy {
        ListenRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    //  输入的值
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
        showTip(
            msg = "创建听单中",
            color = R.color.business_text_color_333333,
            isDelayGone = false,
            isShowProgress = true
        )
        launchOnIO {
            repository.createSheet(sheet_name, description).checkResult(
                onSuccess = {
                    if (TextUtils.isEmpty(audioId)) {
                        showTip(
                            msg = CONTEXT.getString(R.string.listen_add_success_tip),
                            color = R.color.business_text_color_333333
                        )
                        mDialog?.dismiss()
                    } else {
                        addSheet(it.sheet_id)
                    }
                },
                onError = {
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
                    addSheetSuccess()
                },
                onError = {
                    showErrorTip("$it")
                }
            )
        }
    }

    /**
     * 添加成功
     */
    private fun addSheetSuccess() {
        if (IS_FIRST_ADD_SHEET.getBooleanMMKV(true)) {
            hideTipView()
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
                        LISTEN_SHEET_LIST_MY_LIST,
                        ""
                    )
                    dismiss()
                }
                customView =
                    ImageView(mActivity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(mActivity)
        } else {
            successBlock()
            showTip(msg = "添加成功", color = R.color.business_text_color_333333)
        }
        mDialog?.dismiss()
        IS_FIRST_ADD_SHEET.putMMKV(false)
    }

    private fun showErrorTip(msg: String?) {
        showTip(msg = "$msg", color = R.color.business_color_ff5e5e)
    }

    /**
     * 编辑听单
     *
     * @param bean 听单json
     */
    private fun editSheet(bean: ListenPatchSheetBean) {
        showTip(
            msg = "修改听单中",
            color = R.color.business_text_color_333333,
            isDelayGone = false,
            isShowProgress = true
        )
        launchOnIO {
            repository.editSheet(bean).checkResult(
                onSuccess = {
                    editSuccess(bean.sheet_name)
                    showTip(
                        msg = CONTEXT.getString(R.string.listen_edit_success_tip),
                        color = R.color.business_text_color_333333
                    )
                    mDialog?.dismiss()
                },
                onError = {
//                    showErrorTip(msg = CONTEXT.getString(R.string.listen_edit_fail))
                    showErrorTip(msg = "$it")
                }
            )
        }
    }


    /**
     * 检测文本信息
     */
    private fun checkText() {
        val str = inputText.get()!!.trim().trimEnd()
        val description =
            dataBinding?.listenDialogCreateSheetEditSynopsis?.text.toString()
        when {
            str.isEmpty() -> {
                showErrorTip(msg = CONTEXT.getString(R.string.listen_input_no_null_tip))
            }
            EmojiUtils.containsEmoji(str) -> {
                showErrorTip(msg = CONTEXT.getString(R.string.listen_input_char_tip))
            }
            numberOfWords(str) -> {
                showErrorTip(msg = CONTEXT.getString(R.string.listen_input_length_tip))
            }
            else -> {
                if (sheetId.get() == null) {
                    createSheet(str, description)
                } else {
                    editSheet(ListenPatchSheetBean(str, description, sheetId.get()!!))
                }
            }
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


    private fun showTip(
        msg: String,
        color: Int,
        isDelayGone: Boolean? = true,
        isShowProgress: Boolean? = false
    ) {
        dataBinding?.listenDialogCreateSheetLayout?.apply {
            dataBinding?.listenDialogCreateSheetTip?.text = msg
            dataBinding?.listenDialogCreateSheetTip?.setTextColor(
                ContextCompat.getColor(
                    context,
                    color
                )
            )
            visibility = View.VISIBLE
            handler.removeCallbacksAndMessages(null)
            if (isDelayGone == true) {
                handler.postDelayed({
                    hideTipView()
                }, 3000)
            }
        }
        if (isShowProgress == true) {
            dataBinding?.listenDialogCreateSheetProgress?.visibility = View.VISIBLE
        } else {
            dataBinding?.listenDialogCreateSheetProgress?.visibility = View.GONE
        }
    }

    fun hideTipView() {
        dataBinding?.listenDialogCreateSheetLayout?.visibility = View.GONE
    }

}