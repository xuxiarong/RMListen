package com.rm.component_comm.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.rm.component_comm.router.ApplicationProvider
import com.rm.component_comm.utils.isMainProcess
import dalvik.system.DexFile
import java.io.File
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
class ApplicationDelegate {
    enum class ApplicationEvent {
        create, terminate, lowMemory, trimMemory
    }

    companion object {
        /**
         * 阿里路由必须先初始化
         * @param application
         * @return
         */
        fun with(application: Application?): ApplicationManager? {
            return ApplicationManager().resolveInit(application!!)
        }
    }


    /**
     * Copy from galaxy sdk ${com.alibaba.android.galaxy.utils.ClassUtils}
     * Scanner, find out class with any conditions, copy from google source code.
     */
    object ClassFindUtils {
        private const val TAG = "ClassUtils"
        private const val EXTRACTED_NAME_EXT = ".classes"
        private const val EXTRACTED_SUFFIX = ".zip"
        private val SECONDARY_FOLDER_NAME =
            "code_cache" + File.separator + "secondary-dexes"
        private const val PREFS_FILE = "multidex.version"
        private const val KEY_DEX_NUMBER = "dex.number"
        private const val VM_WITH_MULTIDEX_VERSION_MAJOR = 2
        private const val VM_WITH_MULTIDEX_VERSION_MINOR = 1
        private fun getMultiDexPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                PREFS_FILE,
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) Context.MODE_PRIVATE else Context.MODE_PRIVATE or Context.MODE_MULTI_PROCESS
            )
        }

        /**
         * 获取单一路径下所有实现了接口的类对象
         *
         * @param context U know
         * @param clazz   接口
         * @param path    包路径
         * @param <T>     U know
         * @return 对象列表
        </T> */
        fun <T> getObjectsWithInterface(
            context: Context,
            clazz: Class<T>,
            path: String
        ): List<T> {
            val objectList: MutableList<T> = ArrayList()
            try {
                //找出所有路径中的类名，主要用于各个组件根包名一致的情况
                val classFileNames =
                    getFileNameByPackageName(
                        context,
                        path
                    )
                for (className in classFileNames) {
                    try {
                        // TODO: 2020-02-19  暂时跳过im sdk 的包名
//                        if (!className.contains("com.leimans.imsdk")) {
                        val aClass = Class.forName(className)
                        if (clazz.isAssignableFrom(aClass) && clazz != aClass && !aClass.isInterface) {
                            objectList.add(
                                Class.forName(className).getConstructor()
                                    .newInstance() as T
                            )
                        }
//                        }
                    } catch (e: Exception) {
                        //... 忽略某些不存在的类
                    }
                }
                if (objectList.size == 0) {
                    Log.e(
                        TAG,
                        "No files were found, check your configuration please!"
                    )
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(
                    TAG,
                    "getObjectsWithInterface error, " + e.message
                )
            }
            return objectList
        }

        /**
         * 获取多路径下所有实现了接口的类对象
         *
         * @param context  U know
         * @param clazz    接口
         * @param pathList 包路径列表
         * @param <T>      U know
         * @return 对象列表
        </T> */
        fun <T> getObjectsWithInterface(
            context: Context,
            clazz: Class<T>,
            pathList: List<String>
        ): List<T> {
            val objectList: MutableList<T> = ArrayList()
            try {
                for (path in pathList) {
                    //找出所有路径中的类名，主要用于各个组件根包名不一致的情况
                    val classFileNames =
                        getFileNameByPackageName(
                            context,
                            path
                        )
                    for (className in classFileNames) {
                        val aClass = Class.forName(className)
                        if (clazz.isAssignableFrom(aClass) && clazz != aClass && !aClass.isInterface) {
                            objectList.add(
                                Class.forName(className).getConstructor()
                                    .newInstance() as T
                            )
                        }
                    }
                }
                if (objectList.size == 0) {
                    Log.e(
                        TAG,
                        "No files were found, check your configuration please!"
                    )
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(
                    TAG,
                    "getObjectsWithInterface error, " + e.message
                )
            }
            return objectList
        }

        /**
         * 通过指定包名，扫描包下面包含的所有的ClassName
         *
         * @param context     U know
         * @param packageName 包名
         * @return 所有class的集合
         */
        @Throws(PackageManager.NameNotFoundException::class, IOException::class)
        fun getFileNameByPackageName(
            context: Context,
            packageName: String
        ): List<String> {
            val classNames: MutableList<String> =
                ArrayList()
            for (path in getSourcePaths(
                context
            )) {
                var dexfile: DexFile? = null
                try {
                    dexfile = if (path.endsWith(EXTRACTED_SUFFIX)) {
                        //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                        DexFile.loadDex(path, "$path.tmp", 0)
                    } else {
                        DexFile(path)
                    }
                    val dexEntries = dexfile!!.entries()
                    while (dexEntries.hasMoreElements()) {
                        val className = dexEntries.nextElement()
                        if (className.contains(packageName)) {
                            classNames.add(className)
                        }
                    }
                } catch (ignore: Throwable) {
                    Log.e(
                        TAG,
                        "Scan map file in dex files made error.",
                        ignore
                    )
                } finally {
                    if (null != dexfile) {
                        try {
                            dexfile.close()
                        } catch (ignore: Throwable) {
                        }
                    }
                }
            }
            Log.d(
                TAG,
                "Filter " + classNames.size + " classes by packageName <" + packageName + ">"
            )
            return classNames
        }

        /**
         * get all the dex path
         *
         * @param context the application context
         * @return all the dex path
         * @throws PackageManager.NameNotFoundException Exception
         * @throws IOException                          Exception
         */
        @Throws(PackageManager.NameNotFoundException::class, IOException::class)
        fun getSourcePaths(context: Context): List<String> {
            val applicationInfo =
                context.packageManager.getApplicationInfo(context.packageName, 0)
            val sourceApk = File(applicationInfo.sourceDir)
            val sourcePaths: MutableList<String> =
                ArrayList()
            sourcePaths.add(applicationInfo.sourceDir) //add the default apk path

            //the prefix of extracted file, ie: test.classes
            val extractedFilePrefix =
                sourceApk.name + EXTRACTED_NAME_EXT

            //如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
            //通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
            if (!isVMMultidexCapable) {
                //the total dex numbers
                val totalDexNumber = getMultiDexPreferences(
                    context
                )
                    .getInt(KEY_DEX_NUMBER, 1)
                val dexDir =
                    File(
                        applicationInfo.dataDir,
                        SECONDARY_FOLDER_NAME
                    )
                for (secondaryNumber in 2..totalDexNumber) {
                    //for each dex file, ie: test.classes2.zip, test.classes3.zip...
                    val fileName =
                        extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX
                    val extractedFile = File(dexDir, fileName)
                    if (extractedFile.isFile) {
                        sourcePaths.add(extractedFile.absolutePath)
                        //we ignore the verify zip part
                    } else {
                        throw IOException("Missing extracted secondary dex file '" + extractedFile.path + "'")
                    }
                }
            }
            if (isAppDebug(
                    context
                )
            ) {
                // Search instant run support only debuggable
                sourcePaths.addAll(
                    tryLoadInstantRunDexFile(
                        applicationInfo
                    )
                )
            }
            return sourcePaths
        }

        /**
         * 判断App是否是Debug版本
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isAppDebug(context: Context): Boolean {
            return try {
                val pm = context.packageManager
                val ai = pm.getApplicationInfo(context.packageName, 0)
                ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }
        }

        /**
         * Get instant run dex path, used to catch the branch usingApkSplits=false.
         */
        private fun tryLoadInstantRunDexFile(applicationInfo: ApplicationInfo): List<String> {
            val instantRunSourcePaths: MutableList<String> =
                ArrayList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != applicationInfo.splitSourceDirs) {
                // add the splite apk, normally for InstantRun, and newest version.
                instantRunSourcePaths.addAll(Arrays.asList(*applicationInfo.splitSourceDirs))
                Log.d(TAG, "Found InstantRun support")
            } else {
                try {
                    // This man is reflection from Google instant run sdk, he will tell me where the dex files go.
                    val pathsByInstantRun =
                        Class.forName("com.android.tools.fd.runtime.Paths")
                    val getDexFileDirectory = pathsByInstantRun.getMethod(
                        "getDexFileDirectory",
                        String::class.java
                    )
                    val instantRunDexPath = getDexFileDirectory.invoke(
                        null,
                        applicationInfo.packageName
                    ) as String
                    val instantRunFilePath = File(instantRunDexPath)
                    if (instantRunFilePath.exists() && instantRunFilePath.isDirectory) {
                        val dexFile = instantRunFilePath.listFiles()
                        for (file in dexFile) {
                            if (null != file && file.exists() && file.isFile && file.name
                                    .endsWith(".dex")
                            ) {
                                instantRunSourcePaths.add(file.absolutePath)
                            }
                        }
                        Log.d(TAG, "Found InstantRun support")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "InstantRun support error, " + e.message)
                }
            }
            return instantRunSourcePaths
        }// let isMultidexCapable be false// 非YunOS原生Android// YunOS需要特殊判断

        /**
         * Identifies if the current VM has a native support for multidex, meaning there is no need for
         * additional installation by this library.
         *
         * @return true if the VM handles multidex
         */
        private val isVMMultidexCapable: Boolean
            private get() {
                var isMultidexCapable = false
                var vmName: String? = null
                try {
                    if (isYunOS) {    // YunOS需要特殊判断
                        vmName = "'YunOS'"
                        isMultidexCapable =
                            Integer.valueOf(System.getProperty("ro.build.version.sdk")!!) >= 21
                    } else {    // 非YunOS原生Android
                        vmName = "'Android'"
                        val versionString =
                            System.getProperty("java.vm.version")
                        if (versionString != null) {
                            val matcher =
                                Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?")
                                    .matcher(versionString)
                            if (matcher.matches()) {
                                try {
                                    val major = matcher.group(1).toInt()
                                    val minor = matcher.group(2).toInt()
                                    isMultidexCapable =
                                        (major > VM_WITH_MULTIDEX_VERSION_MAJOR
                                                || (major == VM_WITH_MULTIDEX_VERSION_MAJOR
                                                && minor >= VM_WITH_MULTIDEX_VERSION_MINOR))
                                } catch (ignore: NumberFormatException) {
                                    // let isMultidexCapable be false
                                }
                            }
                        }
                    }
                } catch (ignore: Exception) {
                }
                Log.i(
                    TAG,
                    "VM with name " + vmName + if (isMultidexCapable) " has multidex support" else " does not have multidex support"
                )
                return isMultidexCapable
            }

        /**
         * 判断系统是否为YunOS系统
         */
        private val isYunOS: Boolean
            private get() = try {
                val version = System.getProperty("ro.yunos.version")
                val vmName = System.getProperty("java.vm.name")
                (vmName != null && vmName.toLowerCase().contains("lemur")
                        || version != null && version.trim { it <= ' ' }.length > 0)
            } catch (ignore: Exception) {
                false
            }
    }
}

class ApplicationManager {
    // 组件统一的包名的前缀
    private val package_ = "com.rm"
    private var businessApplicationList: List<IApplicationDelegate>? = null
    fun resolve(application: Application): ApplicationManager? {
        if (isMainProcess(application)) {
            businessApplicationList =
                ApplicationDelegate.ClassFindUtils.getObjectsWithInterface(
                    application,
                    IApplicationDelegate::class.java,
                    package_
                )
        }
        return this
    }

    fun resolveInit(application: Application): ApplicationManager? {
        if (isMainProcess(application)) {
            businessApplicationList = ApplicationProvider.queryApplicationDelegate()
        }
        return this
    }

    fun handleEvent(
        applicationEvent: ApplicationDelegate.ApplicationEvent?,
        vararg levels: Int
    ): ApplicationManager? {
        if (businessApplicationList != null) {
            for (applicationDelegate in businessApplicationList!!) {
                when (applicationEvent) {
                    ApplicationDelegate.ApplicationEvent.create -> applicationDelegate.onCreate()
                    ApplicationDelegate.ApplicationEvent.terminate -> applicationDelegate.onTerminate()
                    ApplicationDelegate.ApplicationEvent.lowMemory -> applicationDelegate.onLowMemory()
                    ApplicationDelegate.ApplicationEvent.trimMemory -> applicationDelegate.onTrimMemory(
                        levels[0]
                    )
                    else -> {
                    }
                }
            }
        }
        return this
    }
}

/**
 * 一个组件的代理类 ：与Application的生命周期捆绑
 */
interface IApplicationDelegate {
    val TAG: String
        get() = "IApplicationDelegate"

    fun onCreate()
    fun onTerminate()
    fun onLowMemory()
    fun onTrimMemory(level: Int)
}