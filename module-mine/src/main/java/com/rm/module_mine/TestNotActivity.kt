package com.rm.module_mine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.router.ConstantsARouter

@Route(path = ConstantsARouter.F_TEST)
class TestNotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_not)
    }
}