package com.rm.music_exoplayer_lib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 *
 * @des:
 * @data: 9/14/20 11:28 AM
 * @Version: 1.0.0
 */
class CacheUtils private constructor() {
    //SP-KEY
    private val SP_KEY_NAME = "music_player_config"
    private val SP_KEY_ALARM_MODEL = "SP_ALARM_MODEL"
    private val SP_KEY_PLAYER_MODEL = "SP_KEY_PLAYER_MODEL"
    private var mSharedPreferences: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    companion object {
        val instance: CacheUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheUtils()
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun initSharedPreferencesConfig(context: Context) {
        if (null == mSharedPreferences) {
            mSharedPreferences = context.getSharedPreferences(
                context.packageName + SP_KEY_NAME,
                Context.MODE_MULTI_PROCESS
            )
            mEditor = mSharedPreferences?.edit()
        }
    }


    fun putInt(key: String?, value: Int): Boolean {
        mEditor?.putInt(key, value)
        return mEditor?.commit() == true
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return mSharedPreferences?.getInt(key, defaultValue) ?: 0
    }

    fun putString(key: String?, value: String?): Boolean {
        mEditor?.putString(key, value)
        return mEditor?.commit() == true
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return mSharedPreferences?.getString(
            key,
            defaultValue
        )
    }

    fun putLong(key: String?, value: Long): Boolean {
        mEditor?.putLong(key, value)
        return mEditor?.commit() == true
    }

    fun getLong(key: String?): Long {
        return getLong(key, 0)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return mSharedPreferences?.getLong(
            key,
            defaultValue
        ) ?: -1
    }

}