package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.repository.MineRepository

/**
 * 个人主播/详情  /发布书籍/听单/收藏听单列表
 */
class MineMemberViewModel(private val repository: MineRepository): BaseVMViewModel() {

    val memberId = ObservableField<String>()
    val getCertistr = ObservableField<Int>()
    /**
     * 获取个人/主播详情
     */
    fun getInfoDetail(){
        launchOnUI {
            repository.memberDetail(memberId.get()!!).checkResult(
                onSuccess = {
                    showContentView()

                },onError = {
                    showContentView()
                }
            )
        }
    }

    fun getCertified(){
        when(getCertistr.get()){
            1 -> false
            2 -> true
        }
    }

}