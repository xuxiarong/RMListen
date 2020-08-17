package com.rm.listen.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.util.putMMKV
import com.rm.component_comm.IS_LOGIN
import com.rm.component_comm.router.ConstantsARouter
import com.rm.listen.R
import kotlinx.android.synthetic.main.activity_login_test.*

@Route(path = ConstantsARouter.F_LOGIN_PATH)
class LoginTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_test)
        go_login.setOnClickListener {
            IS_LOGIN.putMMKV(true)
        }
    }
}