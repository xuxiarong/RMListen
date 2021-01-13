## **component-comm库技术文档**

- **component-comm公用库概述**：

  >项目中组件化通信的依赖库，主要功能包括Module之间通信，koin框架依赖注入的初始化，module中Application的生命周期回调。

  **Module间通信**：

  - 采用Arouter框架实现module与module之间的通信

    在component-comm模块中定义最上层的module暴露给其他module调用的接口，该接口需要实现阿里Arouter框架中

    的IProvider接口(以MineModule为例子)

    ```kotlin
    interface MineService : ApplicationProvider {
        fun routerLogin(context: Context)
    }
    ```

  - 在对应的实体module中，创建实现component-comm中定义的该module接口的实现类，在类名上加上Arouter的跳转路径

    的注解@Path

    ```kotlin
    @Route(path = ARouterModuleServicePath.PATH_MINE_SERVICE)
    class MineServiceImpl : MineService {
        override fun routerLogin(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
        override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
            return MineApplicationDelegate::class.java
        }
    
        override fun init(context: Context?) {
        }
    }
    ```

  **Koin依赖注入框架的使用**：

  ​	koin框架是一个kotlin官方提供的轻量级依赖注入框架，配置简单，使用起来高效方便。

  ​    1:集成koin的依赖环境，在Application创建时初始化koin

  ```kotlin
  api rootProject.ext.dependencies["koin_android"]
  api rootProject.ext.dependencies["koin_androidx_scope"]
  api rootProject.ext.dependencies["koin_androidx_viewmodel"]
  ```

  

  ```kotlin
  startKoin {
      androidLogger(Level.DEBUG)
      androidContext(this@ComponentApplication)
  }
  ```

  

  ​	2:在每个module初始化时，将module需要用到的ViewModel，repository等都先声明注册到koin框架中

  ​		注册module需要用到的viewModel，repository到koin中

  ```
  class MineApplicationDelegate : IApplicationDelegate {
      override fun onCreate() {
          DLog.d(TAG,"Module Mine onCreate()!!!")
          loadKoinModules(mineModule)
      }
  
      override fun onTerminate() {
          DLog.d(TAG,"Module Mine onTerminate()!!!")
      }
  
      override fun onLowMemory() {
          DLog.d(TAG,"Module Mine onLowMemory()!!!")
      }
  
      override fun onTrimMemory(level: Int) {
          DLog.d(TAG,"Module Mine onTrimMemory(),---level--->>>$level")
      }
  }
  ```

  ​		声明module所包含的viewModel，repository对象

  ```
  val viewModelModule = module {
      viewModel {
          LoginViewModel(get())
      }
  }
  
  val repositoryModule = module {
      single { LoginRepository(get()) }
  }
  
  val mineModule = listOf(viewModelModule, repositoryModule)
  ```



​	3:在具体业务中使用koin框架获取已注册的viewmodel或者repository

```kotlin
class LoginActivity : BaseNetActivity<ActivityLoginBinding, LoginViewModel>() {

    private val loginViewModel by viewModel<LoginViewModel>()
}
```





**Module中Application生命周期的回调**：

1:在component-comm中定义Application生命周期回调的接口

```kotlin
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
```

2:每个module都创建一个实现了IApplicationDelegate的实现类

```kotlin
/**
 * desc   : Mine 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MineApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Mine onCreate()!!!")
        loadKoinModules(mineModule)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Mine onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Mine onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Mine onTrimMemory(),---level--->>>$level")
    }
}
```

3:在component-comm的Application中进行生命周期的回调

```kotlin
/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
open class ComponentApplication : BaseApplication() {
    private lateinit var applicationManager: ApplicationManager
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ComponentApplication)
        }
        initARouter(this)
        initApplications()
        Cxt.context=CONTEXT
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        this.applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.trimMemory, level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.lowMemory)
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.terminate)
    }

    private fun initApplications() {
        applicationManager = ApplicationDelegate.with(this)
            ?.handleEvent(ApplicationDelegate.ApplicationEvent.create)!!
    }

}
```

