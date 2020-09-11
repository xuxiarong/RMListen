package com.rm.business_lib.base.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.binding.bindText
import com.rm.baselisten.dialog.BaseFragmentDialog
import com.rm.baselisten.utilExt.Color
import com.rm.business_lib.R

/**
 * desc   : 可添加自定义视图的提示dialog
 * date   : 2020/09/10
 * version: 1.0
 */
class CustomTipsFragmentDialog : BaseFragmentDialog() {
    init {
        val bundle = Bundle()
        bundle.putInt(LAYOUT_ID, R.layout.business_dialog_custom_tips_layout)
        arguments = bundle
    }

    // 显示内容
    var contentText = ""

    // 标题
    var titleText = ""

    // 左边按钮文本
    var leftBtnText = ""

    @ColorRes
    // 左边按钮颜色值
    var leftBtnTextColor = 0

    // 右边按钮文本
    var rightBtnText = ""

    @ColorRes
    // 右边按钮颜色值
    var rightBtnTextColor = 0

    // 左边按钮点击事件
    var leftBtnClick: (View) -> Unit = {}

    // 右边按钮点击事件
    var rightBtnClick: (View) -> Unit = {}

    // 添加的自定义View
    var customView: View? = null


    override fun initView() {
        super.initView()
        // 设置dialog有自己的背景
        dialogHasBackground = true
        // 设置标题显示
        rootView?.findViewById<TextView>(R.id.business_tips_dialog_title)?.bindText(titleText)
        // 设置内容显示
        rootView?.findViewById<TextView>(R.id.business_tips_dialog_content)?.bindText(contentText)

        // 设置左边按钮
        rootView?.findViewById<TextView>(R.id.business_tips_dialog_left_btn)?.apply {
            if (TextUtils.isEmpty(leftBtnText)) {
                visibility = View.GONE
                rootView?.findViewById<View>(R.id.business_tips_dialog_left_right_line)?.visibility =
                    View.GONE
            } else {
                visibility = View.VISIBLE
                text = leftBtnText
            }

            if (leftBtnTextColor != 0) {
                setTextColor(Color(leftBtnTextColor))
            }
        }?.setOnClickListener {
            leftBtnClick(it)
        }

        // 设置右边按钮
        rootView?.findViewById<TextView>(R.id.business_tips_dialog_right_btn)?.apply {
            if (TextUtils.isEmpty(rightBtnText)) {
                visibility = View.GONE
                rootView?.findViewById<View>(R.id.business_tips_dialog_left_right_line)?.visibility =
                    View.GONE
            } else {
                visibility = View.VISIBLE
                text = rightBtnText
            }

            if (rightBtnTextColor != 0) {
                setTextColor(Color(rightBtnTextColor))
            }
        }?.setOnClickListener {
            rightBtnClick(it)
        }

        // 添加自定义的View
        rootView?.findViewById<FrameLayout>(R.id.business_tips_dialog_custom_lay)?.apply {
            customView?.let {
                addView(
                    it,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }

    }

    fun show(activity: FragmentActivity) {
        showDialog<CustomTipsFragmentDialog>(activity)
    }
}