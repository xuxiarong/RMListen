## **听书Android客户端**



**项目架构描述**：

>项目采用组件化+mvvm架构搭建，采用阿里路由通讯。

**Project中若干Lib说明**：

- base-listen-lib： 基础模块的封装，适用于应用层开发、基础工具等
- business-lib： 组件化的业务lib，统一配置网络、style、权限、bean实体、数据库等。
- component-comm: 用于组件间路由通讯
- music-player-lib: 音频播放器

**Project中若干组件module说明**：

- app: 负责各个业务组件的组装、添加multiDex功能、一些常见的操作：混淆，签名等。
- module-main: 闪屏页、引导页、MainActivity
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


