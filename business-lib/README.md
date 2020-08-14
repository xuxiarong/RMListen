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


