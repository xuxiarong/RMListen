//package com.rm.listen.mine
//
//import android.util.Log
//import androidx.lifecycle.Observer
//import com.lm.mvvmcore.base.BaseVMActivity
//import com.rm.listen.R
//import com.rm.listen.bean.Title
//import com.rm.listen.databinding.ActivityMineBinding
//import com.rm.listen.login.LoginViewModel
//import org.koin.androidx.viewmodel.ext.android.viewModel
//
//class MineActivity : BaseVMActivity() {
//
//    private val loginViewModel by viewModel<LoginViewModel>()
//    private val binding by binding<ActivityMineBinding>(R.layout.activity_mine)
//
//    override fun initView() {
//        binding.run {
//            viewModel = loginViewModel
//            title = Title(getString(R.string.login), R.drawable.arrow_back) { onBackPressed() }
//        }
//    }
//
//    override fun initData() {
//        Log.e("suolong", "initData: ")
//    }
//
//    override fun startObserve() {
//        loginViewModel.apply {
//            uiState.observe(this@MineActivity, Observer {
//                if (it.isLoading)  Log.e("suolong", "isLoading: ")
//
//                it.isSuccess?.let {
//                    Log.e("suolong", "finish: ")
//                    finish()
//                }
//
//                it.isError?.let { err ->
//                    Log.e("suolong", "finish: ")
//                }
//            })
//        }
//    }
//
//
//}