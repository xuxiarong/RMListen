package com.rm.component_comm;

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

    // 主工程路径
    interface Main {
        String PATH_MAIN_SERVICE = "/main/service";
    }

    //主页路径
    interface Home {
        String PATH_HOME_SERVICE = "/module_home/service";
    }

    //个人路径
    interface Mine {
        String PATH_MINE_SERVICE = "/module_mine/service";
        String F_TEST = "/rm/module_mine/TestNot";
        String F_MAIN = "/rm/module_mine/TestMineActivity";
        String F_LOGIN_PATH = "/listen/login.Login";
        String F_NEED_LOGIN = "/rm/module_mine/NeedLoginActivity";
    }

    //搜索路径
    interface Search {
        String PATH_SEARCH_SERVICE = "/module_search/service";
    }

    // 播放路径
    interface Play {
        String PATH_PLAY_SERVICE = "/module_play/service";
    }

    //听路径
    interface Listen {
        String PATH_LISTEN_SERVICE = "/module_listen/service";
    }

    // 登陆路径
    interface Login {
        String PATH_LOGIN_SERVICE = "/module_login/service";
    }

}