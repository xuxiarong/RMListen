package com.rm.component_comm;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
interface IApplicationProvider extends IProvider {

    interface IApplication{
        void onCreate();
    }



}
