package debug

import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.rm.module_download.downloadModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
class DownloadApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@DownloadApplication)
        }
        Cxt.context=CONTEXT
        loadKoinModules(downloadModules)
    }
}