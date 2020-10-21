package com.rm.module_mine.viewmodel

import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineFragmentMemberMainViewMode(private val repository: MineRepository) : BaseVMViewModel() {

    //创建听单adapter
    val createSheetAdapter by lazy {

    }

    //发布书籍adapter
    val releaseSheetAdapter by lazy {

    }


    /**
     * 获取: 发布书籍/听单/收藏听单列表
     */
    fun getMemberProfile(memberId: String) {
        launchOnUI {
            repository.memberProfile(memberId).checkResult(
                onSuccess = {
                    showContentView()
                    DLog.i("getMemberProfile", "" + it.toString())

                }, onError = {
                    showContentView()
                }
            )
        }
    }

}