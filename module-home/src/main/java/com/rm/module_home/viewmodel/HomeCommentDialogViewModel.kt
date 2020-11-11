package com.rm.module_home.viewmodel

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.receiver.NetworkChangeReceiver
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.loginUser
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
    private val baseViewModel: BaseVMViewModel,
    private val audioId: String,
    private val commentSuccessBlock: () -> Unit
) : BaseVMViewModel() {
    init {
        NetworkChangeReceiver.isAvailable.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (!NetworkChangeReceiver.isAvailable.get()) {
                    showTip("当前网络不可用", R.color.business_color_ff5e5e, true)
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

    private var showAnim: ObjectAnimator? = null
    private var hideAnim: ObjectAnimator? = null

    /**
     * 懒加载创建dialog对象
     */
    val mDialog by lazy {
        CommBottomDialog().apply {
            dialogHeightIsMatchParent = true
            initDialog = {
                dataBinding = mDataBind as HomeDialogCommentBinding
                dataBinding?.apply {
                    initDialog = {
                        initAnim(homeDialogCommentLayout)
                    }
                }

                dialog?.setOnShowListener {
                    home_dialog_comment_ed.postDelayed({
                        home_dialog_comment_ed.isFocusable = true
                        home_dialog_comment_ed.requestFocus()
                        home_dialog_comment_ed.isFocusableInTouchMode = true
                        val inputManager =
                            home_dialog_comment_ed.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }, 50)
                }
            }
        }
    }

    private fun initAnim(view: View) {
        showAnim =
            ObjectAnimator.ofFloat(view, "translationY", 0f, CONTEXT.dip(96).toFloat()).apply {
                duration = 200
            }
        hideAnim =
            ObjectAnimator.ofFloat(view, "translationY", CONTEXT.dip(96).toFloat(), 0f).apply {
                duration = 200
            }
    }

    /**
     * 输入框发生变化
     */
    private fun inputChange(content: String) {
        inputComment.set(content.trim().trimEnd())
        if (inputComment.get()!!.length > 200) {
            showTip(
                CONTEXT.getString(R.string.home_comment_input_limit),
                R.color.business_color_ff5e5e,
                true
            )
        }
    }

    private fun showTip(msg: String, color: Int, isDelayGone: Boolean) {
        dataBinding?.homeDialogCommentLayout?.apply {
            dataBinding?.homeDialogCommentTip?.text = msg
            dataBinding?.homeDialogCommentTip?.setTextColor(
                ContextCompat.getColor(
                    context,
                    color
                )
            )
            visibility = View.VISIBLE
            showAnim?.start()
            handler.removeCallbacksAndMessages(null)
            if (isDelayGone) {
                handler.postDelayed({
                    hideTipView()
                }, 3000)
            }
        }
    }

    private fun hideTipView() {
        dataBinding?.homeDialogCommentLayout?.visibility = View.GONE
        dataBinding?.homeDialogCommentLayout?.clearAnimation()
        hideAnim?.start()
    }


    /**
     * 点击发送按钮
     */
    fun clickSend(view: View) {
        inputComment.get()?.let {
            if (it.length > 200) {
                showTip(
                    CONTEXT.getString(R.string.home_comment_input_limit),
                    R.color.business_color_ff5e5e, true
                )

            } else {
                showTip("评论中", R.color.business_text_color_333333, false)
                if (NetworkChangeReceiver.isAvailable.get()) {
                    sendComment(view, it, audioId, loginUser.get()!!.id!!)
                } else {
                    showTip("当前网络不可用", R.color.business_color_ff5e5e, true)
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
        audio_id: String,
        anchor_id: String
    ) {
        launchOnIO {
            repository.homeSendComment(content, audio_id, anchor_id).checkResult(
                onSuccess = {
                    val imm =
                        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                    }
                    commentSuccessBlock()
                    mDialog.dismiss()
                    hideTipView()
                },
                onError = {
                    showTip("$it", R.color.business_color_ff5e5e, true)
                }
            )
        }
    }
}