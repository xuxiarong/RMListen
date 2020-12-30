package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.R
import com.rm.module_mine.activity.MimeGetBookActivity.Companion.GET_BOOK_RESULT_CODE
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuan fang
 * @date 9/21/20
 * @description
 *
 */
class MineGetBookViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    /**
     * 书籍信息
     */
    val bookInfo: (String) -> Unit = {
        bookName.set(it)
        authorIconIsVisibility.set(false)
        memberIconIsVisibility.set(false)
        contactIconIsVisibility.set(false)
        bookIconIsVisibility.set(keyboardIsVisibility.get() == true && it.isNotEmpty())
    }
    var bookName = ObservableField("")
    val bookIconIsVisibility = ObservableField(false)

    /**
     * 作者
     */
    val authorInfo: (String) -> Unit = {
        author.set(it)

        bookIconIsVisibility.set(false)
        memberIconIsVisibility.set(false)
        contactIconIsVisibility.set(false)
        authorIconIsVisibility.set(keyboardIsVisibility.get() == true && it.isNotEmpty())
    }
    var author = ObservableField("")
    val authorIconIsVisibility = ObservableField(false)

    /**
     * 主播
     */
    val memberInfo: (String) -> Unit = {
        member.set(it)

        authorIconIsVisibility.set(false)
        bookIconIsVisibility.set(false)
        contactIconIsVisibility.set(false)
        memberIconIsVisibility.set(keyboardIsVisibility.get() == true && it.isNotEmpty())
    }
    var member = ObservableField("")
    val memberIconIsVisibility = ObservableField(false)


    /**
     * 联系方式
     */
    val contactInfo: (String) -> Unit = {
        contact.set(it)

        authorIconIsVisibility.set(false)
        memberIconIsVisibility.set(false)
        bookIconIsVisibility.set(false)
        contactIconIsVisibility.set(keyboardIsVisibility.get() == true && it.isNotEmpty())
    }
    var contact = ObservableField("")
    val contactIconIsVisibility = ObservableField(false)


    //输入法是否显示
    val keyboardIsVisibility = ObservableField<Boolean>(false)

    //输入法显示/隐藏监听
    var keyboardVisibility: (Boolean, Int) -> Unit = { it, _ -> keyboardVisibilityListener(it) }

    /**
     * 键盘的显示隐藏监听
     */
    private fun keyboardVisibilityListener(keyboardVisibility: Boolean) {
        keyboardIsVisibility.set(keyboardVisibility)
        bookIconIsVisibility.set(false)
        memberIconIsVisibility.set(false)
        contactIconIsVisibility.set(false)
        authorIconIsVisibility.set(false)
    }

    /**
     * 提交
     */
    fun requestBook() {
        when {
            (bookName.get()!!.isEmpty()) -> {
                showTip("书籍名称不能为空", R.color.business_color_ff5e5e)
            }
            bookName.get()!!.length > 50 -> {
                showTip("书籍名称字数不能超过50", R.color.business_color_ff5e5e)
            }
            author.get()!!.length > 50 -> {
                showTip("作者名字数不能超过50", R.color.business_color_ff5e5e)
            }
            member.get()!!.length > 50 -> {
                showTip("主播名字数不能超过50", R.color.business_color_ff5e5e)
            }
            contact.get()!!.length > 50 -> {
                showTip("联系方式字数不能超过50", R.color.business_color_ff5e5e)
            }
            else -> {
                launchOnIO {
                    repository.mineRequestBook(
                        bookName.get()!!,
                        author.get()!!,
                        member.get()!!,
                        contact.get()!!
                    ).checkResult(
                        onSuccess = {
                          setResultAndFinish(GET_BOOK_RESULT_CODE)
                        },
                        onError = {it,_->
                            showTip("$it", R.color.business_color_ff5e5e)
                        }
                    )
                }
            }
        }
    }

    fun clickBookName() {
        bookName.set("")
    }

    fun clickAuthor() {
        author.set("")
    }

    fun clickMember() {
        member.set("")
    }

    fun clickContact() {
        contact.set("")
    }
}