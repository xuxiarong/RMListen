package com.rm.listen.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.router.ConstantsARouter
import com.rm.listen.R

@Route(path = ConstantsARouter.F_NEED_LOGIN)
class NeedLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_login)
    }
}