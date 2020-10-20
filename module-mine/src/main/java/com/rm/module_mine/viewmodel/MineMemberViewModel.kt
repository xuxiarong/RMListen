package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.HomeDetailBean
import com.rm.module_mine.bean.MineInfoDetail
import com.rm.module_mine.repository.MineRepository

class MineMemberViewModel(private val repository: MineRepository): BaseVMViewModel() {

    var detailInfoData = ObservableField<MineInfoDetail>()
    var memberFans = ObservableField<String>()
    var memberFollows = ObservableField<String>()
    /**
     * 获取个人/主播详情
     */
    fun getInfoDetail(memberId:String){
        launchOnUI {
            repository.memberDetail(memberId).checkResult(
                onSuccess = {
                    showContentView()
                    detailInfoData.set(it)
                    memberFans.set("粉丝："+it.fans)
                    memberFollows.set("关注："+it.follows)
                    DLog.i("getInfoDetail",""+it.toString())

                },onError = {
                    showContentView()
                }
            )
        }
    }

    /**
     * 获取: 发布书籍/听单/收藏听单列表
     */
    fun getmemberProfile(memberId: String){

    }

}