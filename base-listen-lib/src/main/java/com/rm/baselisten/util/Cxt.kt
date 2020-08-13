package com.rm.baselisten.util

import android.content.Context
import kotlin.properties.Delegates

/**
 *
 * @ClassName: Cxt
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/13/20 5:44 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/13/20 5:44 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
class Cxt {
    companion object {
        var context: Context by Delegates.notNull()
    }
}