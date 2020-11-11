package com.rm.module_mine.fragment

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.binding.bindPlayCount
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberActivity.Companion.MEMBER_ID
import com.rm.module_mine.databinding.MineFragmentMemberMainBinding
import com.rm.module_mine.viewmodel.MineFragmentMemberMainViewModel
import java.lang.StringBuilder

/**
 * 主播/个人 发布的书籍/创建听单/收藏听单
 */
class MineMemberMainFragment :
    BaseVMFragment<MineFragmentMemberMainBinding, MineFragmentMemberMainViewModel>() {
    companion object {
        fun newInstance(memberId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            val fragment = MineMemberMainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.data.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val data = mViewModel.data.get()!!
                val publishNum = data.publish_info.total
                val createNum = data.sheet_info.total
                val favorNum = data.favor_sheet_info.total

                setNum(mDataBind.mineMemberCreateSheetNum, createNum)
                setNum(mDataBind.mineMemberPublishNum, publishNum)
                setNum(mDataBind.mineMemberFavorSheetNum, favorNum)
            }
        })
    }

    private fun setNum(textView: TextView, count: Int) {
        val sb = StringBuilder("(")
        val charSequence: CharSequence = when {
            count < 10000 -> {
                count.toString()
            }
            count > 99999999 -> {
                "9999w+"
            }
            else -> {
                val wan = count / 10000
                val qian = count % 10000 / 1000
                val bai = count % 1000 / 100
                "$wan" + "." + "$qian" + "w"
            }
        }
        sb.append(charSequence)
        sb.append(")")
        textView.text = sb
    }

    override fun initLayoutId() = R.layout.mine_fragment_member_main

    override fun initData() {
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.getMemberProfile(it)
        }
    }
}