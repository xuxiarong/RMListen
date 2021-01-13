## **第三方库,避免重复造轮子**

### **FlycoRoundView**
**FlycoRoundView解决问题**：
> 控件的边角问题
###**作用**
**解决80%控件的背景边角度问题**

- RoundFrameLayout
- RoundTextView
- RoundLinearLayout
- RoundRelativeLayout

###Attributes

> rv_backgroundColor	color	background color
> rv_backgroundPressColor	color	background press color
> rv_cornerRadius	dimension	background rectangle corner radius,unit dp
> rv_strokeWidth	dimension	background rectangle stroke width,unit dp
> rv_strokeColor	color	background rectangle stroke color
> rv_strokePressColor	color	background rectangle stroke press color
> rv_textPressColor	color	text press color
> rv_isRadiusHalfHeight	boolean	corner radius is half of height
> rv_isWidthHeightEqual	boolean	width and height is the same size which is the max value of them
> rv_cornerRadius_TL	dimension	corner radius top left,unit dp
> rv_cornerRadius_TR	dimension	corner radius top right,unit dp
> rv_cornerRadius_BL	dimension	corner radius bottom left,unit dp
> rv_cornerRadius_BR	dimension	corner radius bottom right,unit dp
> rv_isRippleEnable	boolean	is ripple effect enable for Api21+

**注意点**：
- 详细文档https://github.com/H07000223/FlycoRoundView
- 能用代码的不要用xml,能用xml不要用图片资源

### **BaseRecyclerViewAdapterHelper**
### **解决问题**
### **RecyclerView adapter万能适配器**
- 支持下拉刷新，加载更多，多类型 item
### 详细文档###
- https://github.com/CymChad/BaseRecyclerViewAdapterHelper
**注意点**
- 不要在 convert 方法里面绑定item 事件
— 通过 addChildClickViewIds 在adapter绑定



## **XBanner**

#### 详细文档

https://github.com/xiaohaibin/XBanner

#### 主要功能：

- 支持一屏显示多个
- 支持根据服务端返回的数据动态设置广告条的总页数
- 支持大于等于1页时的无限循环自动轮播、手指按下暂停轮播、抬起手指开始轮播
- 支持自定义状态指示点位置 左 、中 、右
- 支持自定义状态指示点
- 支持监听 item 点击事件
- 支持设置图片轮播间隔
- 支持指示器背景的修改及隐藏/显示
- 支持显示提示性文字功能
- 支持图片切换动画,目前支持10种切换动画，亦可设置自定义动画效果
- 支持设置图片切换速度
- 支持设置数字指示器
- 支持设置图片框架整体占位图
- 支持Glide/Fresco等主流图片加载框架加载图片
- 支持自定义布局
- 支持AndroidX

### ****自定义View****
###****Arrow****
### 用途：自定义箭头 支持上下左右方向
###用法（向下的箭头）
```
        <com.rm.business_lib.wedgit.arrow.ArrowView
            android:id="@+id/report_why_arrow"
            app:arrow_direction="top"
            android:layout_marginLeft="5dp"
            app:arrow_line_color="@color/business_text_color_b1b1b1"
            android:layout_width="25dp"
            android:layout_height="15dp"
            tools:ignore="MissingConstraints" />
```
###****MenuPointView****
### 用途：自定义多点按钮 支持横竖方向
```
     <com.rm.business_lib.wedgit.menupoint.MenuPointView
                    android:id="@+id/music_play_point"
                    android:layout_width="20dp"
                    android:layout_height="20dp"
                    app:menu_number="3"
                    app:menu_point_color="@color/business_text_color_333333"
                    app:menu_point_width="3dp"
                    app:menu_type="horizontal"
                    tools:visibility="visible" />
                    ```