package com.rm.module_search.fragment

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.bean.AudioBean
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchSheetBean
import com.rm.module_search.databinding.SearchFragmentContentAllBinding
import com.rm.module_search.searchResultData
import com.rm.module_search.viewmodel.SearchContentAllViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 全部
 *
 */
class SearchContentAllFragment :
    BaseVMFragment<SearchFragmentContentAllBinding, SearchContentAllViewModel>() {


    override fun initLayoutId() = R.layout.search_fragment_content_all

    override fun initModelBrId() = BR.viewModel

    override fun initData() {

    }

    override fun startObserve() {
        searchResultData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val data = searchResultData.get()!!
                processAnchorData(data.member_list)
                processSheetData(data.sheet_list)
                processBookData(data.audio_list)

                if (data.audio_list.isEmpty() && data.member_list.isEmpty() && data.sheet_list.isEmpty()) {
                    mViewModel.showDataEmpty()
                }
            }
        })
    }

    /**
     * 处理书籍数据
     */
    private fun processBookData(audioList: List<AudioBean>) {
        if (audioList.size > 3) {
            mViewModel.bookAdapter.setList(audioList.subList(0, 3))
        } else {
            mViewModel.bookAdapter.setList(audioList)
        }
    }

    /**
     * 处理听单数据
     */
    private fun processSheetData(sheetList: List<SearchSheetBean>) {
        if (sheetList.size > 3) {
            mViewModel.sheetAdapter.setList(sheetList.subList(0, 3))
        } else {
            mViewModel.sheetAdapter.setList(sheetList)
        }
    }

    /**
     * 处理主播数据
     */
    private fun processAnchorData(memberList: List<MemberBean>) {
        if (memberList.size > 3) {
            mViewModel.anchorAdapter.setList(memberList.subList(0, 3))
        } else {
            mViewModel.anchorAdapter.setList(memberList)
        }
    }

}