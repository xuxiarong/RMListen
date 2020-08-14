package com.rm.listen.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter
import com.rm.listen.R

@Route(path = ConstantsARouter.Mine.F_NEED_LOGIN)
class NeedLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_login)
    }
}