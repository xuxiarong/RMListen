package com.rm.component_comm.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.google.gson.Gson
import com.rm.baselisten.util.DLog
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.bean.BannerJumpBean
import com.rm.business_lib.bean.BannerUrlBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.main.MainService
import com.rm.component_comm.router.RouterHelper

/**
 * desc   :
 * date   : 2020/10/09
 * version: 1.0
 */

object BannerJumpUtils {
    fun onBannerClick(context: Context, url: String) {
        if (url.startsWith("http")) {
            BaseWebActivity.startBaseWebActivity(context = context, url = url)
            return
        }
        val uri: Uri = Uri.parse(url)
        val authority = uri.authority
        var page: String? = null
        var param: String?
        var audioId = ""

        val params = uri.queryParameterNames
        if (!TextUtils.isEmpty(authority)) {
            when (authority) {
                "native" -> {
                    for (uriParam in params) {
                        if (uriParam == "page") {
                            page = uri.getQueryParameter("page")
                        } else if (uriParam == "param") {
                            param = uri.getQueryParameter("param")
                            val jumpBean = Gson().fromJson(param, BannerJumpBean::class.java)
                            audioId = jumpBean.audio_id
                        }
                    }
                    if (!TextUtils.isEmpty(page)) {
                        if (page == "home") {
                            val mainService = RouterHelper.createRouter(MainService::class.java)
                            mainService.startMainActivity(context = context)
                        } else if (page == "audioDetail") {
                            val homeService = RouterHelper.createRouter(HomeService::class.java)
                            if (!TextUtils.isEmpty(audioId)) {
                                homeService.toDetailActivity(context = context, audioID = audioId)
                            }
                        }
                    }

                }
                "h5" -> {
                    for (uriParam in params) {
                        if (uriParam == "param") {
                            param = uri.getQueryParameter("param")
                            val jumpBean = Gson().fromJson(param, BannerUrlBean::class.java)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(jumpBean.url))
                            context.startActivity(intent)
                        }
                    }
                }
                else -> {
                    DLog.d("suolong", "a&$authority")
                }
            }
        }
    }
}