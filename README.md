## **听书Android客户端部署文档，代码结构说明**



**项目架构描述**：

>项目采用组件化+mvvm架构搭建，采用阿里路由ARouter通讯。

**Project中若干Lib说明**：

- base-listen-lib： 基础模块的封装，适用于应用层开发、基础工具等

  关键Api和class：

  ```kotlin
  //基类Application入口
  open class BaseApplication : Application() {
  //基类的RecyclerView通用的Adapter
  abstract class BaseMultiAdapter<T : MultiItemEntity > constructor(private var brId: Int)
  //基类的Activity
  abstract class BaseActivity : FragmentActivity(), EasyPermissions.PermissionCallbacks 
  //基类的Fragment
  abstract class BaseFragment : Fragment()
  //基类的ViewModel
  open class BaseViewModel() : ViewModel(),Parcelable
  //基类的仓库，所有数据库，网络数据的获取，都应该封装在该类或者其子类中
  open class BaseRepository 
  //基类的网络响应类
  data class BaseResponse<out T>(val code: Int, val msg: String, val data: T)
  //基类的网络结果处理类
  sealed class BaseResult<out T : Any>
  ```

- business-lib： 组件化的业务lib，统一配置网络、style、权限、bean实体、数据库等。

  关键Api和class：

  ```kotlin
  //业务类Application的入口，一些第三方的库的封装，应用数据的初始化，业务模块的组装，都在该入口处理
  open class BusinessApplication : BaseApplication()
  //业务类的常量或者全局变量的入口，通用的数据模块都应该封装在该常量类中
  object BusinessConstance
  //基类网络接口地址，环境切换，网络拦截，解析，处理，转换等操作的定义
  class BusinessRetrofitClient : BaseRetrofitClient() {
      companion object {
          // 基础线上环境ip地址，用于发布项目，后期需要换成域名
        	private const val BASE_RELEASE_URL = "http://dev-api.ls.com:9602/api/v1_0/"
          // 基础测试环境ip地址，用于开发测试阶段，后期需要换成域名		  
        	private const val BASE_TEST_RUL = "http://10.1.9.197:9602/api/v1_0/"
        	// 基础MOCK环境ip地址，用于构造特殊测试数据，后期需要换成域名
  				private const val BASE_MOCK_URL = "http://192.168.11.217:3000/mock/154/api/v1_0/"
        	// 基础开发环境ip地址，用于正常业开发，务后期需要换成域名
          private const val BASE_DEVELOP_URL = "http://dev-api.ls.com:9602/api/v1_0/" 	
        	// 基础STG环境ip地址，用于新特性预研，后期需要换成域名
        	private const val BASE_STG_URL = "http://10.1.20.201:9602/api/v1_0/"
        
        	//app接口环境定义，五个Int常量分别对应上面五个ip地址
          private const val TYPE_RELEASE = 0
          private const val TYPE_TEST = 1
          private const val TYPE_MOCK = 2
          private const val TYPE_DEVELOP = 3
          private const val TYPE_STG = 4
        
        	//当前app的接口环境，只需要将currentType切换成对应的常量，重启app即可
          private var currentType = TYPE_TEST
      }
  }
  
  关键的模块
  //通用的自定义View，例如侧滑删除，下拉刷新，拖拽布局，播放动画等等
  com.rm.business_lib.wedgit 
  //通用的Utils，例如日期转换，文件管理，Uri解析，加解密等工具类
  com.rm.business_lib.utils
  //通用的数据库表管理模块
  com.rm.business_lib.db
  //通用的databinding扩展的View属性
  com.rm.business_lib.binding
  //通用的javaBean对象
  com.rm.business_lib.bean
  ...
  ```

- component-comm: 用于组件间路由通讯

  关键Api和class：

  ```kotlin
  //模块通信的Application入口，所有模块与模块通信的接口定义都应该在此声明
  open class ComponentApplication : BusinessApplication()
  //ARouter的工具类，可以通过传入class对象，跳转到已注册过的对应的Activity或者Service
  object ARouterUtil
  ```

- music-player-lib: 音频播放器

  关键Api和class：

  ```kotlin
  //音乐播放器的服务
  internal class MusicPlayerService : Service(), MusicPlayerPresenter 
  //音乐播放器的服务代理对象
  class MusicPlayerBinder constructor(val presenter: MusicPlayerPresenter) : Binder()
  //音乐播放器的管理类
  class MusicPlayerManager private constructor() : MusicPlayerPresenter
  //音频监听器，当音频焦点被其他 MediaPlayer 实例抢占，则暂停播放，重新获取到音频输出焦点，自动恢复播放
  class MusicAudioFocusManager constructor(val context: Context)
  ```

**Project中若干组件module说明**：

- app: 负责各个业务组件的组装、添加multiDex功能、一些常见的操作：混淆，签名等。

  关键Api和class：

  ​	app模块主要是一个包装其他模块的空壳，没有暴露的api和class

- module-main: 闪屏页、引导页、MainActivity

  关键Api和class：

  ```kotlin
  interface MainService : ApplicationProvider {
      /**
       *  跳转到首页
       *  @param context 上下文
       *  @param selectTab 选择跳转到首页的哪个tab (0 -> 首页  1 -> 搜索 2 -> 我听 3 -> 我的)
       */
      fun startMainActivity(context: Context,selectTab : Int = 0)
  }
  
  //项目中Main模块包含的Activity和ViewModel
  class SplashActivity : BaseVMActivity<HomeActivitySplashBinding, HomeSplashViewModel>()
  class MainMainActivity :ComponentShowPlayActivity<MainActivityMainBindingImpl, HomeMainViewModel>()
  ```

- module-home: 首页具体业务

  关键Api和class：

```kotlin
/**
 * desc   : Home工程module路由服务接口
 * date   : 2020/08/12
 * version: 1.0
 */
interface HomeService : ApplicationProvider {
    /**
     * 跳转到听单界面
     * @param context Context
     */
    fun startHomeMenuActivity(context: Context)

    /**
     * 跳转到听单详情
     * @param sheetId 听单ID
     */
    fun startHomeSheetDetailActivity(context: Activity, sheetId: String)

    /**
     * 获取主页Fragment
     */
    fun getHomeFragment(): Fragment

    /**
     * 跳转到板块
     * @param context 上下文
     * @param topicId 板块Id
     * @param blockName 板块名字
     */
    fun startTopicActivity(context: Context, topicId: Int, blockName: String)
    /**
     * 跳转详情页
     * @param context 上下文
     * @param audioID 音频Id
     */
    fun startDetailActivity(context: Context, audioID: String)

    /**
     * 弹出评论dialog
     * @param mActivity activity 对象
     * @param audio 音频Id
     * @param commentSuccessBlock 评论成功回调
     */
    fun showCommentDialog(
            mActivity: FragmentActivity,
            audio: String,
            commentSuccessBlock: () -> Unit
    )

    /**
     * 显示升级弹窗
     * @param activity FragmentActivity
     * @param versionInfo 升级信息
     * @param installCode 为止来源授权码
     * @param enforceUpdate 是否强制更新
     */
    fun showUploadDownDialog(
            activity: FragmentActivity,
            versionInfo: BusinessVersionUrlBean,
            installCode: Int,
            enforceUpdate: Boolean
    )

    /**
     * 去到未知来源设置界面
     * @param activity
     * @param path 安装包目录地址
     * @param installCode 为止来源授权码
     */
    fun gotoInstallPermissionSetting(activity: Activity, path: String, installCode: Int)
}
```

- module-search: 搜索具体业务

  关键api和class

  ```kotlin
  //搜索类的一些常量入口
  object SearchConstance
  //搜索模块暴露的接口
  @Route(path = ARouterModuleServicePath.PATH_SEARCH_SERVICE)
  class SearchServiceImpl : SearchService {
      override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
          return SearchApplicationDelegate::class.java
      }
  
      override fun init(context: Context?) {
      }
  
      override fun getSearchFragment(): Fragment {
          return SearchMainFragment()
      }
  }
  ```

- module-play:  播放具体业务

  关键api和class

- module-listen: 我听具体业务

- module-mine: 我的具体业务  

- module-download:下载模块业务


