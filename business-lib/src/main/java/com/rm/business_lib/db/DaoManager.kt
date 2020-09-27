package com.rm.business_lib.db

import android.database.sqlite.SQLiteDatabase
import com.rm.baselisten.util.Cxt
import org.greenrobot.greendao.query.QueryBuilder

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class DaoManager private constructor() {

    val DB_NAME = "listen.db"
    var devOpenHelper: SQLiteDatabase? = null
    var daoSession: DaoSession? = null

    companion object {
        val daoManager: DaoManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DaoManager()
        }

    }

    fun initDaoManger() {
        //创建数据库
        devOpenHelper = DaoMaster.DevOpenHelper(Cxt.context, DB_NAME, null).writableDatabase
        //对数据库的添加,删除，修改，查询等操作
        daoSession = DaoMaster(devOpenHelper).newSession()
    }

    //到开日志输入，默认关闭
    fun setBug(isDebug: Boolean = true) {
        QueryBuilder.LOG_SQL = isDebug
        QueryBuilder.LOG_VALUES = isDebug
    }

    //使用完成后需要关闭
    fun closeConnection() {
        devOpenHelper?.close()
        daoSession?.clear()
    }

}