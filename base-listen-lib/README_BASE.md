## **BaseModel技术分析文档**



**Base公用库概述**：

>项目中公用的基类，管理类，工具类等等的集合依赖库，与业务逻辑无关，可以随时提供给其余项目使用。

**Base库基本说明(按照功能分包)**：

- activity: BaseActivity和BaseNetActivity的封装
- binding: 使用databinding的自定义属性绑定的基类封装
- json: 用于解析json数据
- model: 用于存放BaseViewModel中包含的model
- mvvm: 对Mvvm模式的Activity和Fragment的封装
- net: 对网络模块的封装，本项目采用的网络框架为:retrofit + 协程
- util: 通用工具类的统一目录
- view: 通用自定义View的目录
- viewmodel: 基类的viewmodel封装

**Base模块中部分功能使用注意事项和示例**：

- BaseNetActivity: 包含了网络请求的基类Activity，封装成了Mvvm+databind+liveData的模式,包含childBiew。Enpty,error,load等状态Biew，其中

  module-main: 闪屏页、引导页、MainActivity

- module-home: 首页具体业务

- module-search: 搜索具体业务

- module-play:  播放具体业务

- module-listen: 我听具体业务

- module-mine: 我的具体业务  

### **深色模式介绍**

**整体思路**

>采用google官方文档的提供深色模式方案，其中利用AppCompatDelegate.setDefaultNightMode去设置App中全局的主题模式
>并针对国内不同手机厂商做出一些适配方案，具体如下：
Android 10.0 : 默认跟随系统主题属性，设置页面不显示深色模式入口(google官方文档是Android 10.0才有的深色模式)
Android 6.0-9.0： 设置里面页面深色模式开关，用户可以手动切换主题模式，App跟随用户习惯
Android 6.0以下：默认不显示深色模式入口，也不提供深色模式功能

**注意点**：

- 每个组件的资源，以组件名为前缀，防止resource资源冲突。例如，登录组件，添加一个图片，命名为login_xxx.png。
- 每个组件单独除开业务功能，额外添加的主口或者application子类放到组件的debug文件夹下。好处：切换到集成模式(以lib形式)时候，会编译排除debug文件夹下的代码。
- 在config.gradle中切换模式，后需要刷新Project，才能生效。


