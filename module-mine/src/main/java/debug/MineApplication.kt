package debug

import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.rm.module_mine.mineModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class MineApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MineApplication)
        }
        Cxt.context=CONTEXT
        loadKoinModules(mineModules)
    }
}