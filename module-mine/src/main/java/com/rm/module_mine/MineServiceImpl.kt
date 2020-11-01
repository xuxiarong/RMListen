package com.rm.module_mine

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_mine.activity.MineMemberActivity
import com.rm.module_mine.fragment.MineHomeFragment
import com.rm.module_mine.login.LoginActivity

/**
 * desc   : mine module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_MINE_SERVICE)
class MineServiceImpl : MineService {
    override fun routerLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return MineApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }

    override fun getMineFragment(): Fragment {
        return MineHomeFragment.newInstance()
    }

    override fun toMineMember(context: Context, memberId: String) {
        MineMemberActivity.newInstance(context, memberId)
    }

    override fun toMineCommentFragment(context: Context, memberId: String) {
        MineMemberActivity.toMineCommentFragment(context, memberId)
    }



}