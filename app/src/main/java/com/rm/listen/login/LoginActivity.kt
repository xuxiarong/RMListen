package com.rm.listen.login

import android.app.ProgressDialog
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.component_comm.ConstantsARouter
import com.rm.listen.R
import com.rm.listen.bean.Title
import com.rm.listen.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

class LoginActivity : BaseNetActivity<ActivityLoginBinding, LoginViewModel>() {

    private val loginViewModel by viewModel<LoginViewModel>()

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }

    override fun initView() {
        databind.run {
            viewModel = loginViewModel
            title = Title(R.string.login, R.drawable.arrow_back) { onBackPressed() }
        }
    }


    private var count = 0

    override fun onResume() {
        super.onResume()
//        count++
//        databind.login.visibility = View.VISIBLE
//
//        if (count % 4 == 0) {
//            showContent()
//        } else if (count % 4 == 1) {
//            showEmpty()
//        } else if (count % 4 == 2) {
//            showError()
//        } else {
//            Log.i("llj","其他情况 !!!!")
//            databind.login.visibility = View.GONE
//            showLoad()
//        }

    }

    override fun initData() {

    }

    override fun startObserve() {
//        loginViewModel.apply {
//            uiState.observe(this@LoginActivity, Observer {
//                if (it.isLoading) showProgressDialog()
//
//                it.isSuccess?.let {
//                    dismissProgressDialog()
//                    Toast.makeText(this@LoginActivity, "登陆成功", Toast.LENGTH_LONG).show()
//                }
//
//                it.isError?.let { err ->
//                    dismissProgressDialog()
//                }
//            })
//        }
    }

    private var progressDialog: ProgressDialog? = null
    private fun showProgressDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

}