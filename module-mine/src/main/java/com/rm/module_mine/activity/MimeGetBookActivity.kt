package com.rm.module_mine.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.rm.baselisten.helper.KeyboardStatusDetector.Companion.bindKeyboardVisibilityListener
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivityGetBookBinding
import com.rm.module_mine.viewmodel.MineGetBookViewModel
import kotlinx.android.synthetic.main.mine_activity_get_book.*

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
class MimeGetBookActivity : BaseVMActivity<MineActivityGetBookBinding, MineGetBookViewModel>(),
    View.OnFocusChangeListener {
    companion object {
        const val GET_BOOK_REQUEST_CODE = 1003
        const val GET_BOOK_RESULT_CODE = 1004
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(
                Intent(activity, MimeGetBookActivity::class.java),
                GET_BOOK_REQUEST_CODE
            )
        }
    }

    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.mine_activity_get_book
    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setTitle(getString(R.string.mine_free_book))
            .setLeftIcon(R.drawable.business_icon_return_bc)
            .setLeftIconClick { finish() }

        mViewModel.baseTitleModel.value = titleModel
        mine_get_book_ed_book.onFocusChangeListener = this
        mine_get_book_ed_author.onFocusChangeListener = this
        mine_get_book_ed_member.onFocusChangeListener = this
        mine_get_book_ed_contact.onFocusChangeListener = this


        bindKeyboardVisibilityListener { it, keyboardHeight ->
            mViewModel.keyboardVisibilityListener(it)
            if (it) {
                mDataBind?.mineGetBookView?.apply {
                    layoutParams.height = keyboardHeight
                    visibility = View.VISIBLE
                }
            } else {
                mDataBind?.mineGetBookView?.visibility = View.GONE
                mine_get_book_ed_book.clearFocus()
                mine_get_book_ed_author.clearFocus()
                mine_get_book_ed_member.clearFocus()
                mine_get_book_ed_contact.clearFocus()
            }
        }
    }

    override fun startObserve() {
    }

    override fun initData() {
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.mine_get_book_ed_book -> {
                mViewModel.contactIconIsVisibility.set(false)
                mViewModel.memberIconIsVisibility.set(false)
                mViewModel.authorIconIsVisibility.set(false)

                mViewModel.bookIconIsVisibility.set(
                    mViewModel.keyboardIsVisibility.get() == true &&
                            mViewModel.bookName.get()!!.isNotEmpty() &&
                            hasFocus
                )
            }
            R.id.mine_get_book_ed_author -> {
                mViewModel.contactIconIsVisibility.set(false)
                mViewModel.memberIconIsVisibility.set(false)
                mViewModel.bookIconIsVisibility.set(false)

                mViewModel.authorIconIsVisibility.set(
                    mViewModel.keyboardIsVisibility.get() == true &&
                            mViewModel.author.get()!!.isNotEmpty() &&
                            hasFocus
                )
            }
            R.id.mine_get_book_ed_member -> {
                mViewModel.contactIconIsVisibility.set(false)
                mViewModel.bookIconIsVisibility.set(false)
                mViewModel.authorIconIsVisibility.set(false)

                mViewModel.memberIconIsVisibility.set(
                    mViewModel.keyboardIsVisibility.get() == true &&
                            mViewModel.member.get()!!.isNotEmpty() &&
                            hasFocus
                )
            }
            R.id.mine_get_book_ed_contact -> {
                mViewModel.bookIconIsVisibility.set(false)
                mViewModel.memberIconIsVisibility.set(false)
                mViewModel.authorIconIsVisibility.set(false)

                mViewModel.contactIconIsVisibility.set(
                    mViewModel.keyboardIsVisibility.get() == true &&
                            mViewModel.contact.get()!!.isNotEmpty() &&
                            hasFocus
                )
            }
        }
    }


}