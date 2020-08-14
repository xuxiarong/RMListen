package com.rm.business_lib.db

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.query.QueryBuilder


/**
 *
 * @ClassName: DaoUtils
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/14/20 3:42 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/14/20 3:42 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
class DaoUtil<T, K> {

    var mSession: AbstractDao<T, K>? = null

    constructor(cls:Class<T>,key:K){
//        mSession=DaoManager.daoManager.daoSession.getDao(cls) as AbstractDao<T, K>
    }



    //保存单个数据
    fun save(item: T) {
        mSession?.insert(item)
    }
    //保存单个数据
    fun save(vararg  item: T) {
        mSession?.insertInTx(item.asList())
    }
    //保存多条数据
    fun save(items: List<T>) {
        mSession?.insertInTx(items)
    }

    //修改单条并保存
    fun saveOrUpdate(item: T) {
        mSession?.insertOrReplace(item)
    }
    fun saveOrUpdate(vararg  item: T) {
        mSession?.insertOrReplaceInTx(item.asList())
    }
    //修改多条数据
    fun saveOrUpdate(items: List<T>) {
        mSession?.insertOrReplaceInTx(items)
    }

    fun deleteByKey(key: K) {
        mSession?.deleteByKey(key)
    }

    fun delete(item: T) {
        mSession?.delete(item)
    }

    fun delete(vararg items: T) {
        mSession?.deleteInTx(items.asList())
    }

    fun delete(items: List<T>?) {
        mSession?.deleteInTx(items)
    }

    fun deleteAll() {
        mSession?.deleteAll()
    }


    fun update(item: T) {
        mSession?.update(item)
    }

    fun update(vararg items: T) {
        mSession?.updateInTx(items.asList())
    }

    fun update(items: List<T>?) {
        mSession?.updateInTx(items)
    }

    fun query(key: K): T? {
        return mSession?.load(key)
    }

    fun queryAll(): List<T>? {
        return mSession?.loadAll()
    }

    fun query(
        where: String?,
        vararg params: String?
    ): List<T>? {
        return mSession?.queryRaw(where, *params)
    }

    fun queryBuilder(): QueryBuilder<T>? {
        return mSession?.queryBuilder()
    }

    fun count(): Long? {
        return mSession?.count()
    }

    fun refresh(item: T) {
        mSession?.refresh(item)
    }

    fun detach(item: T) {
        mSession?.detach(item)
    }
}