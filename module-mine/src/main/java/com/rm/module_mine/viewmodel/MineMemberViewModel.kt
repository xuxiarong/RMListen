package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.bean.MineInfoDetail
import com.rm.module_mine.repository.MineRepository

class MineMemberViewModel(private val repository: MineRepository) : BaseVMViewModel() {
    //主播是否关注
    var isAttention = ObservableBoolean(false)

    //是否显示关注按钮
    val attentionVisibility = ObservableField<Boolean>(true)

    val memberId = ObservableField<String>("")

    var detailInfoData = ObservableField<MineInfoDetail>()

    var memberFans = ObservableField<String>()

    var memberFollows = ObservableField<String>()

    var isVisible = ObservableBoolean(false)

    /**
     * 获取个人/主播详情
     */
    fun getInfoDetail(memberId: String) {
        launchOnUI {
            repository.memberDetail(memberId).checkResult(
                onSuccess = {
                    showContentView()
                    detailInfoData.set(it)
                    memberFans.set("粉丝：" + it.fans)
                    memberFollows.set("关注：" + it.follows)
                    DLog.i("getInfoDetail", "" + it.toString())

                }, onError = {
                    showContentView()
                }
            )
        }
    }

    /**
     * 关注主播
     */
    private fun attentionAnchor(followId: String) {
        showLoading()
        launchOnIO {
            repository.attentionAnchor(followId).checkResult(
                onSuccess = {
                    showContentView()
                    isAttention.set(true)
                    showToast("关注成功")
                },
                onError = {
                    showContentView()
                })
        }
    }

    /**
     * 取消关注主播
     */
    private fun unAttentionAnchor(followId: String) {
        showLoading()
        launchOnIO {
            repository.unAttentionAnchor(followId).checkResult(
                onSuccess = {
                    showContentView()
                    isAttention.set(false)
                    showToast("取消关注成功")
                },
                onError = {
                    showContentView()
                })
        }
    }


    /**
     * 关注点击事件
     */
    fun clickAttentionFun(context: Context, followId: String) {
        getActivity(context)?.let {
            if (!isLogin.get()) {
                quicklyLogin(it)
            } else {
                DLog.i("--->", "${loginUser.get()!!.id}")
                if (isAttention.get()) {
                    unAttentionAnchor(followId)
                } else {
                    attentionAnchor(followId)
                }
            }
        }
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(this, it, loginSuccess = {
                getInfoDetail(memberId.get()!!)
            })
    }

}