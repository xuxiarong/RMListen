package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.utils.DeviceUtils
import com.rm.module_mine.R
import com.rm.module_mine.repository.MineRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author yuan fang
 * @date 9/21/20
 * @description
 *
 */
class MineGetBookViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    var bookName = ObservableField("")

    /**
     * 书籍信息
     */
    val bookInfo: (String) -> Unit = { bookName.set(it) }

    var author = ""

    /**
     * 作者
     */
    val authorInfo: (String) -> Unit = { author = it }

    var member = ""

    /**
     * 主播
     */
    val memberInfo: (String) -> Unit = { member = it }

    var contact = ""

    /**
     * 联系方式
     */
    val contactInfo: (String) -> Unit = { contact = it }

    /**
     * 提交
     */
    fun requestBook() {
        if (bookName.get()!!.trim().trimEnd().isEmpty()) {
            showTip("书名不能为空", R.color.business_color_ff5e5e)
            return
        }
        launchOnIO {
            repository.mineRequestBook(
                bookName.get()!!,
                author,
                member,
                contact,
                DeviceUtils.uniqueDeviceId
            ).checkResult(
                onSuccess = {
                    showTip("提交成功")
                    viewModelScope.launch {
                        delay(1500)
                        finish()
                    }
                },
                onError = {
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }
}