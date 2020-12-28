package com.rm.component_comm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.google.gson.Gson
import com.rm.baselisten.ktx.toIntSafe
import com.rm.baselisten.util.DLog
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.bean.BannerJumpBean
import com.rm.business_lib.bean.BannerUrlBean
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.main.MainService
import com.rm.component_comm.router.RouterHelper
import com.tencent.bugly.proguard.t

/**
 * desc   :
 * date   : 2020/10/09
 * version: 1.0
 */

object BannerJumpUtils {
    fun onBannerClick(context: Context, url: String, adId: String? = null) {
        try {
            if (adId != null && adId.isNotEmpty()) {
                BusinessInsertManager.doInsertKeyAndAd(
                    BusinessInsertConstance.INSERT_TYPE_AD_CLICK,
                    adId
                )
            }
            if (TextUtils.isEmpty(url)) {
                return
            }
            if (url.startsWith("http")) {
                BaseWebActivity.startBaseWebActivity(context = context, url = url)
                return
            }

            val uri: Uri = Uri.parse(url)
            val authority = uri.authority
            var page: String? = null
            var param: String?
            var ids = ""
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
                                ids = jumpBean.ids
                            }
                        }
                        if (!TextUtils.isEmpty(page)) {
                            if (page == "home") {
                                val mainService = RouterHelper.createRouter(MainService::class.java)
                                mainService.startMainActivity(context = context, selectTab = 0)
                            } else if (page == "search") {
                                val mainService = RouterHelper.createRouter(MainService::class.java)
                                mainService.startMainActivity(context = context, selectTab = 1)
                            } else if (page == "listen") {
                                val mainService = RouterHelper.createRouter(MainService::class.java)
                                mainService.startMainActivity(context = context, selectTab = 2)
                            } else if (page == "mine") {
                                val mainService = RouterHelper.createRouter(MainService::class.java)
                                mainService.startMainActivity(context = context, selectTab = 3)
                            } else if (page == "audioDetail") {
                                val homeService = RouterHelper.createRouter(HomeService::class.java)
                                if (!TextUtils.isEmpty(ids)) {
                                    homeService.startDetailActivity(context = context, audioID = ids)
                                }
                            } else if (page == "sheetInfo") {
                                val homeService = RouterHelper.createRouter(HomeService::class.java)
                                if (!TextUtils.isEmpty(ids)) {
                                    if (context is Activity) {
                                        homeService.startHomeSheetDetailActivity(context = context, sheetId = ids)
                                    }
                                }
                            } else if (page == "topic") {
                                val homeService = RouterHelper.createRouter(HomeService::class.java)
                                if (!TextUtils.isEmpty(ids)) {
                                    homeService.startTopicActivity(context = context, topicId = ids.toIntSafe(),blockName = "")
                                }
                            } else if (page == "listenList") {
                                val homeService = RouterHelper.createRouter(HomeService::class.java)
                                if (!TextUtils.isEmpty(ids)) {
                                    homeService.startHomeMenuActivity(context = context)
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
                        DLog.d("suolong", "authority = $authority")
                    }
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}