package com.rm.business_lib.utils

import android.annotation.SuppressLint
import android.provider.Settings
import android.text.TextUtils
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.getStringMMKV
import com.rm.baselisten.util.putMMKV
import java.util.*

/**
 * <pre>
 *
 * blog  : http://blankj.com
 * time  : 2016/8/1
 * desc  : utils about device
</pre> *
 */
class DeviceUtils private constructor() {
    companion object {
        /**
         * Return the android id of device.
         *
         * @return the android id of device
         */
        @get:SuppressLint("HardwareIds")
        val androidID: String
            get() {
                val id = Settings.Secure.getString(
                    BaseApplication.CONTEXT.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                return if ("9774d56d682e549c" == id) {
                    ""
                } else id ?: ""
            }


        private const val KEY_UDID = "KEY_UDID"

        @Volatile
        private var udid: String = ""

        /**
         * Return the unique device id.
         * <pre>{1}{UUID(macAddress)}</pre>
         * <pre>{2}{UUID(androidId )}</pre>
         * <pre>{9}{UUID(random    )}</pre>
         *
         * @return the unique device id
         */
        @get:SuppressLint("MissingPermission", "HardwareIds")
        val uniqueDeviceId: String
            get() = getUniqueDeviceId("")

        /**
         * Return the unique device id.
         * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
         * <pre>{prefix}{2}{UUID(androidId )}</pre>
         * <pre>{prefix}{9}{UUID(random    )}</pre>
         *
         * @param prefix The prefix of the unique device id.
         * @return the unique device id
         */
        @SuppressLint("MissingPermission", "HardwareIds")
        fun getUniqueDeviceId(prefix: String): String {
            if (TextUtils.isEmpty(udid)) {
                synchronized(DeviceUtils::class.java) {
                    val id: String = KEY_UDID.getStringMMKV()
                    if (!TextUtils.isEmpty(id)) {
                        udid = id
                        return udid
                    }
                    try {
                        val androidId = androidID
                        if (!TextUtils.isEmpty(androidId)) {
                            return saveUdid(prefix + 2, androidId)
                        }
                    } catch (ignore: Exception) { /**/
                    }
                    return saveUdid(prefix + 9, "")
                }
            }
            return udid
        }

        private fun saveUdid(prefix: String, id: String): String {
            udid = getUdid(prefix, id)
            KEY_UDID.putMMKV(udid)
            return udid
        }

        private fun getUdid(prefix: String, id: String): String {
            return if (id == "") {
                prefix + UUID.randomUUID().toString().replace("-", "")
            } else prefix + UUID.nameUUIDFromBytes(id.toByteArray()).toString()
                .replace("-", "")
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}