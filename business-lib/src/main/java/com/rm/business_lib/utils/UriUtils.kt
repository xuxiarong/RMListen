package com.rm.business_lib.utils

import android.net.Uri

/**
 * desc   :
 * date   : 2020/10/09
 * version: 1.0
 */
object UriUtils {

    var uri =
        "http://www.zpan.com:8080/lujing/path.htm?id=10&name=zhangsan&old=24#zuihoude"
    var mUri: Uri = Uri.parse(uri)
//
//    // 协议
//    var scheme: String = mUri.getScheme()
//
//    // 域名+端口号+路径+参数
//    var scheme_specific_part: String = mUri.getSchemeSpecificPart()
//
//    // 域名+端口号
//    var authority: String = mUri.getAuthority()
//
//    // fragment
//    var fragment: String = mUri.getFragment()
//
//    // 域名
//    var host: String = mUri.getHost()
//
//    // 端口号
//    var port: Int = mUri.getPort()
//
//    // 路径
//    var path: String = mUri.getPath()
//
//    // 参数
//    var query: String = mUri.getQuery()

    fun getScheme(url : String){
        var uri : Uri = Uri.parse(url)
        var scheme = uri.scheme
    }

}