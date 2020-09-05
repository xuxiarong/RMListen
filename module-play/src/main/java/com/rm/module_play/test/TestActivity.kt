package com.rm.module_play.test

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.databinding.Observable
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.module_play.BR
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.R
import com.rm.module_play.activity.PlayActivity
import com.rm.module_play.common.ARouterPath
import com.rm.module_play.common.ARouterPath.Companion.playRouter
import com.rm.module_play.common.ARouterPath.Companion.testPath
import com.rm.module_play.databinding.ActivityTestBinding

@Route(path = testPath)
class TestActivity : BaseVMActivity<ActivityTestBinding, TestViewModel>() {


    override fun getLayoutId(): Int = R.layout.activity_test


    override fun initView() {
        rootViewAddView(GlobalplayHelp.instance.globalView)

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        GlobalplayHelp.instance.globalView.show()
    }

    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
    }

    override fun initModelBrId(): Int = BR.viewModel


    override fun startObserve() {
        mViewModel.books.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val intent = Intent()
                val array:ArrayList<SearchResultInfo> = mViewModel.books.get()?: arrayListOf()
                intent.putParcelableArrayListExtra("books", array)
                setResult(100, intent)
                finish()
            }
        })
    }

}