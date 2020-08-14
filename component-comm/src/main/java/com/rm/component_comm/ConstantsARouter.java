package com.rm.component_comm;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: ConstantsARouter
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/13/20 12:06 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/13/20 12:06 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
public interface ConstantsARouter {


    //主页路径
    interface Home {

    }

    //个人路径
    interface Mine {
        String F_TEST = "/rm/module_mine/TestNot";
        String F_MAIN = "/rm/module_mine/TestMineActivity";
        String F_LOGIN_PATH = "/listen/login.Login";
        String F_NEED_LOGIN = "/rm/module_mine/NeedLoginActivity";
    }

    //搜索路径
    interface Search {

    }

    //听路径
    interface Listen {

    }

}