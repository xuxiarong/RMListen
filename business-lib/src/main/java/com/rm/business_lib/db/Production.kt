package com.rm.business_lib.db

/**
 *
 * @ClassName: Production
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/14/20 4:21 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/14/20 4:21 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
interface Production<out T> {
    fun  produce() :T
}