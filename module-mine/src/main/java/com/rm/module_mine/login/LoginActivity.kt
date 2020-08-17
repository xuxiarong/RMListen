package com.rm.module_mine.login

import android.app.ProgressDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.util.ToastUtil
import com.rm.module_mine.R
import com.rm.module_mine.databinding.ActivityLoginBinding
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
        val baseTitleModel = BaseTitleModel()
        baseTitleModel.mainTitle = getString(R.string.mine_login)
        baseTitleModel.subTitle = "我是副标题"
        baseTitleModel.leftIcon = R.drawable.base_icon_back
        baseTitleModel.leftIcon1 = R.drawable.base_icon_back
        baseTitleModel.leftText = "刷新"
        baseTitleModel.leftIconClick = {finish()}
        baseTitleModel.leftTextClick = { ToastUtil.show(this, "leftTextClick" )}
        baseTitleModel.leftIcon1Click = { ToastUtil.show(this, "leftIcon1Click" )}

        baseTitleModel.rightIcon = R.drawable.base_icon_back
        baseTitleModel.rightIcon1 = R.drawable.base_icon_back
        baseTitleModel.rightText = "刷新"
        baseTitleModel.rightIconClick = {ToastUtil.show(this, "rightTextClick" )}
        baseTitleModel.rightTextClick = { ToastUtil.show(this," rightTextClick" )}
        baseTitleModel.rightIcon1Click = { ToastUtil.show(this," rightIcon1Click" )}

        loginViewModel.baseTitleModel.value = baseTitleModel

//        databind.run {
//            viewModel = loginViewModel
//        }
    }

    private var count = 0

    override fun onResume() {
        super.onResume()
        count++
        databind.login.visibility = View.VISIBLE

        if (count % 4 == 0) {
            showContent()
        } else if (count % 4 == 1) {
            showEmpty()
        } else if (count % 4 == 2) {
            showError()
        } else {
            Log.i("llj","其他情况 !!!!")
            databind.login.visibility = View.GONE
            showLoad()
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
                    Toast.makeText(this@LoginActivity, "登陆成功", Toast.LENGTH_LONG).show()
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