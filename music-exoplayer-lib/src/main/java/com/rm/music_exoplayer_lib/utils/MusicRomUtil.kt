package com.rm.music_exoplayer_lib.utils

import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.os.EnvironmentCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MusicRomUtil private constructor() {
    fun check(str: String): Boolean {
        if (sName != null) {
            return sName == str
        }
        var prop: CharSequence? =
            getProp(KEY_VERSION_MIUI)
        if (null != prop) {
            try {
                sVersion =
                    prop as String?
                if (TextUtils.isEmpty(prop)) {
                    prop =
                        getProp(KEY_VERSION_EMUI)
                    sVersion =
                        prop
                    if (TextUtils.isEmpty(prop)) {
                        prop =
                            getProp(KEY_VERSION_OPPO)
                        sVersion =
                            prop
                        if (TextUtils.isEmpty(prop)) {
                            prop =
                                getProp(KEY_VERSION_VIVO)
                            sVersion =
                                prop
                            if (TextUtils.isEmpty(prop)) {
                                prop =
                                    getProp(KEY_VERSION_SMARTISAN)
                                sVersion =
                                    prop
                                if (TextUtils.isEmpty(prop)) {
                                    sVersion =
                                        Build.DISPLAY
                                    if (sVersion?.toUpperCase()
                                            ?.contains(ROM_FLYME) == true
                                    ) {
                                        sName =
                                            ROM_FLYME
                                    } else {
                                        sVersion =
                                            EnvironmentCompat.MEDIA_UNKNOWN
                                        sName =
                                            Build.MANUFACTURER.toUpperCase()
                                    }
                                } else {
                                    sName =
                                        ROM_SMARTISAN
                                }
                            } else {
                                sName =
                                    ROM_VIVO
                            }
                        } else {
                            sName =
                                ROM_OPPO
                        }
                    } else {
                        sName =
                            ROM_EMUI
                    }
                } else {
                    sName =
                        ROM_MIUI
                }
                return sName == str
            } catch (e: RuntimeException) {
            }
            return false
        }
        return false
    }

    val name: String?
        get() {
            if (sName == null) {
                check("")
            }
            return sName
        }

    fun getProp(str: String?): String? {
        return try {
            var bufferedReader: BufferedReader?
            var e: Throwable?
            var str2: String
            var stringBuilder: StringBuilder
            var th: Throwable?
            var bufferedReader2: BufferedReader? = null
            try {
                val runtime = Runtime.getRuntime()
                val stringBuilder2 = StringBuilder()
                stringBuilder2.append("getprop ")
                stringBuilder2.append(str)
                bufferedReader = BufferedReader(
                    InputStreamReader(
                        runtime.exec(stringBuilder2.toString()).inputStream
                    ), 1024
                )
                try {
                    val readLine = bufferedReader.readLine()
                    bufferedReader.close()
                    try {
                        bufferedReader.close()
                    } catch (e2: IOException) {
                        e2.printStackTrace()
                    }
                    readLine
                } catch (e3: IOException) {
                    e = e3
                    try {
                        str2 = TAG
                        stringBuilder = StringBuilder()
                        stringBuilder.append("Unable to read prop ")
                        stringBuilder.append(str)
                        Log.e(str2, stringBuilder.toString(), e)
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close()
                            } catch (e22: IOException) {
                                e22.printStackTrace()
                            }
                        }
                        null
                    } catch (th2: Throwable) {
                        th = th2
                        bufferedReader2 = bufferedReader
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close()
                            } catch (e4: IOException) {
                                e4.printStackTrace()
                            }
                        }
                        throw th
                    }
                }
            } catch (e5: IOException) {
                e = e5
                bufferedReader = null
                str2 = TAG
                stringBuilder = StringBuilder()
                stringBuilder.append("Unable to read prop ")
                stringBuilder.append(str)
                Log.e(str2, stringBuilder.toString(), e)
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
                null
            } catch (th3: Throwable) {
                th = th3
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
                null
            }
        } catch (e: RuntimeException) {
            null
        }
    }

    val version: String?
        get() {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

    fun is360(): Boolean {
        if (!check(ROM_QIKU)) {
            if (!check("360")) {
                return false
            }
        }
        return true
    }

    val isEmui: Boolean
        get() = check(ROM_EMUI)

    val isFlyme: Boolean
        get() = check(ROM_FLYME)

    val isMiui: Boolean
        get() = check(ROM_MIUI)

    val isOppo: Boolean
        get() = check(ROM_OPPO)

    val isSmartisan: Boolean
        get() = check(ROM_SMARTISAN)

    val isVivo: Boolean
        get() = check(ROM_VIVO)

    companion object {
        private const val KEY_VERSION_EMUI = "ro.build.version.emui"
        private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
        private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
        private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
        private const val KEY_VERSION_VIVO = "ro.vivo.os.version"
        const val ROM_EMUI = "EMUI"
        const val ROM_FLYME = "FLYME"
        const val ROM_MIUI = "MIUI"
        const val ROM_OPPO = "OPPO"
        const val ROM_QIKU = "QIKU"
        const val ROM_SMARTISAN = "SMARTISAN"
        const val ROM_VIVO = "VIVO"
        private const val TAG = "Rom"
        private var sName: String? = null
        private var sVersion: String? = null

        @Volatile
        private var mInstance: MusicRomUtil? = null

        @get:Synchronized
        val instance: MusicRomUtil?
            get() {
                synchronized(MusicRomUtil::class.java) {
                    if (null == mInstance) {
                        mInstance =
                            MusicRomUtil()
                    }
                }
                return mInstance
            }
    }
}