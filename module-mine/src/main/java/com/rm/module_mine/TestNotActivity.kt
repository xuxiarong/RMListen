package com.rm.module_mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter

@Route(path = ConstantsARouter.Mine.F_TEST)
class TestNotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_not)
    }
}