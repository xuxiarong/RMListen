package com.rm.baselisten.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.rm.baselisten.R
import com.rm.baselisten.mvvm.BaseActivity
import kotlinx.android.synthetic.main.activity_base_web.*

class BaseWebActivity : BaseActivity() {

    private var mHttpUrl: String? = ""


    override fun initData() {
        mHttpUrl = intent.getStringExtra(KEY_URL)
        baseWebView.loadUrl(mHttpUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        super.initView()
        val settings: WebSettings = baseWebView.settings
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.defaultTextEncodingName = "utf-8"
        settings.loadsImagesAutomatically = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.javaScriptEnabled = true
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.blockNetworkImage = true

        baseWebView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                val forwardList: WebBackForwardList = view.copyBackForwardList()
                val item: WebHistoryItem? = forwardList.currentItem
                if (item != null) {
                    baseWebTitle.visibility = View.VISIBLE
                    baseWebTitle.text = item.title
                } else {
                    baseWebTitle.visibility = View.GONE
                }
            }
            override fun onProgressChanged(webView: WebView?, i: Int) {
                super.onProgressChanged(webView, i)
                if (i < 100) {
                    business_version_upload_dialog_progress.visibility = View.VISIBLE
                    business_version_upload_dialog_progress.progress = i
                } else {
                    business_version_upload_dialog_progress.visibility = View.GONE
                }
            }

        }
        baseWebView.webViewClient = object : WebViewClient() {

        }
        baseWebBack.setOnClickListener {
            if (baseWebView.canGoBack()) {
                baseWebView.goBack()
            }else{
                finish()
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_base_web

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && baseWebView.canGoBack()) {
            baseWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        baseWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        baseWebView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseWebView.destroy()
    }


    companion object {
        const val KEY_URL = "url"
        fun startBaseWebActivity(context: Context, url: String) {
            val intent = Intent(context, BaseWebActivity::class.java)
            intent.putExtra(KEY_URL,url)
            context.startActivity(intent)
        }
    }
}