package com.rm.module_main.activity.guide

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_main.R
import com.rm.module_main.activity.MainMainActivity
import kotlinx.android.synthetic.main.activity_main_guide.*

class MainGuideActivity : BaseActivity() {

    override fun initData() {
        main_guide_to_main.setOnClickListener {
            MainMainActivity.startMainActivity(this)
            finish()
        }
    }

    override fun getLayoutId() = R.layout.activity_main_guide

    companion object{
        fun startGuideActivity(context: Context){
            context.startActivity(Intent(context,MainGuideActivity::class.java))
        }
    }
}