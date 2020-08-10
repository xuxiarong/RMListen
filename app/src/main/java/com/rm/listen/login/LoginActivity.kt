package com.rm.listen.login

import android.app.ProgressDialog
import android.widget.Toast
import androidx.lifecycle.Observer
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.listen.R
import com.rm.listen.bean.Title
import com.rm.listen.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
 class LoginActivity : BaseNetActivity() {

    private val loginViewModel by viewModel<LoginViewModel>()
    private val loginBinding by initChildModule<ActivityLoginBinding>()
     override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        loginBinding.run {
            viewModel = loginViewModel
            title = Title(R.string.login, R.drawable.arrow_back) { onBackPressed() }
        }
    }

    private var count = 0

    override fun onResume() {
        super.onResume()
        count++
        if(count %4 == 0){
            showContent()
        }else if(count %4 == 1){
            showDataEmpty()
        }else if(count %4 == 2){
            showNetError()
        }else{
            showLoading()
        }

    }

    override fun initData() {
    }

    override fun startObserve() {
        loginViewModel.apply {
            uiState.observe(this@LoginActivity, Observer {
                if (it.isLoading) showProgressDialog()

                it.isSuccess?.let {
                    dismissProgressDialog()
                    Toast.makeText(this@LoginActivity,"登陆成功",Toast.LENGTH_LONG).show()
                }

                it.isError?.let { err ->
                    dismissProgressDialog()
                }
            })
        }
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