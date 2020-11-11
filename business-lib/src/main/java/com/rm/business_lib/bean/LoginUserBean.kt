package com.rm.business_lib.bean

/**
 * desc   : 登陆用户个人基本信息
 * date   : 2020/09/03
 * version: 1.0
 */
data class LoginUserBean(
    var id: String? = "",  // id
    var area_code: String? = "",  // 区号
    var account: String? = "", // 账户
    var nickname: String? = "", // 昵称
    var gender: Int? = 0, //性别 0=保密，1=男，2=女,3=未填写
    var birthday: String? = "", // 生日
    var address: String? = "", // 地址
    var signature: String? = "", // 个性签名
    var avatar: String? = "", // 头像路径
    var avatar_url: String? = "", // 头像链接
    var fans: String? = "", // 粉丝数
    var follows: String? = "" // 关注数
)