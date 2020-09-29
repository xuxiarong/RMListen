package com.rm.module_download.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.marginStart
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.binding.afterTextChanged
import com.rm.baselisten.dialog.BaseFragmentDialog
import com.rm.baselisten.utilExt.screenWidth
import com.rm.module_download.R
import java.lang.Exception

/**
 * 下载选集弹窗
 */
class DownloadSelectChaptersDialog(private val endIndex: Int) : BaseFragmentDialog() {
    init {
        val bundle = Bundle()
        bundle.putInt(LAYOUT_ID, R.layout.download_dialog_select_chapters)
        arguments = bundle
        dialogHasBackground = true
        dialogCanceledOnTouchOutside = false
        dialogInterceptClickInput = true
    }

    // 下载按钮点击
    var downloadClick: (Int, Int) -> Unit = { _, _ -> }

    var calcMemoryUse: (Int, Int) -> String = { _, _ -> "" }
    var etStart: EditText? = null
    var etEnd: EditText? = null


    override fun initView() {
        super.initView()
        dialogWidth = screenWidth * 9 / 10
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        rootView?.findViewById<AppCompatButton>(R.id.download_btn_download)
            ?.setOnClickListener { downloadClick(etStart?.text.toString().toInt(), etEnd?.text.toString().toInt()) }
        etStart = rootView?.findViewById<EditText>(R.id.download_et_start)?.apply {
            requestFocus()
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.trim()?.length == 0 || s?.toString() == "") {
                        return
                    }
                    removeTextChangedListener(this)
                    when (s.toString().toInt()) {
                        !in 1 until endIndex -> etStart?.setText("1")
                        else -> {
                            etEnd?.setText(etEnd?.text.toString())
                        }
                    }
                    addTextChangedListener(this)
                    etStart?.text?.apply {
                        setSelection(this.length)
                    }
                    calcMemoryUse(etStart?.text.toString().toInt(), etEnd?.text.toString().toInt())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
        etEnd = rootView?.findViewById<EditText>(R.id.download_et_end)?.apply {
            setText("$endIndex")
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.trim()?.length == 0 || s?.toString() == "") {
                        return
                    }
                    removeTextChangedListener(this)
                    when (s.toString().toInt()) {
                        !in 1..endIndex -> etEnd?.setText("$endIndex")
                        else -> {
                            etEnd?.setText(etEnd?.text.toString().toInt().toString())
                        }
                    }
                    etEnd?.setText(etEnd?.text.toString().toInt().toString())
                    addTextChangedListener(this)
                    etEnd?.text?.apply {
                        setSelection(this.length)
                    }
                    calcMemoryUse(etStart?.text.toString().toInt(), etEnd?.text.toString().toInt())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

    }

    fun show(activity: FragmentActivity) {
        showDialog<DownloadSelectChaptersDialog>(activity)
    }

}