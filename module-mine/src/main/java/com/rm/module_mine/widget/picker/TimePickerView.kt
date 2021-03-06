package com.rm.module_mine.widget.picker

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.loginUser
import com.rm.module_mine.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author yuanfang
 * @date 10/13/20
 * @description
 *
 */
class TimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var yearView: WheelView
    private lateinit var monthView: WheelView
    private lateinit var dayView: WheelView

    companion object {
        const val DEFAULT_START_YEAR = 1900
        const val DEFAULT_END_YEAR = 2020
        const val DEFAULT_START_MONTH = 1
        const val DEFAULT_END_MONTH = 12
        const val DEFAULT_START_DAY = 1
        const val DEFAULT_END_DAY = 31
    }

    private val startYear = DEFAULT_START_YEAR
    private var endYear = DEFAULT_END_YEAR
    private val startMonth = DEFAULT_START_MONTH
    private var endMonth = DEFAULT_END_MONTH
    private val startDay = DEFAULT_START_DAY
    private var endDay = DEFAULT_END_DAY //表示31天的

    private var currentYear = 0

    private var mSelectChangeCallback: ISelectTimeCallback? = null
    private val calendar = Calendar.getInstance()

    init {
        endYear = calendar.get(Calendar.YEAR)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.mine_widget_time_picker, this)
        yearView = findViewById(R.id.mine_time_picker_year)
        monthView = findViewById(R.id.mine_time_picker_month)
        dayView = findViewById(R.id.mine_time_picker_day)

        var timeMillis = -1L
        loginUser.get()?.birthday?.let {
            if (!TextUtils.isEmpty(it)) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                timeMillis = TimeUtils.string2Date(it,dateFormat).time
            }
        }
        setSolar(timeMillis)
    }

    /**
     * 设置公历
     */
    private fun setSolar(timeMillis: Long = -1L) {
        when (timeMillis) {
            -1L -> {
                calendar.time = Date(System.currentTimeMillis())
            }
            else -> {
                calendar.time = Date(timeMillis)
            }
        }
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        // 添加大小月月份并将其转换为list,方便之后的判断
        val monthsBig = arrayOf("1", "3", "5", "7", "8", "10", "12")
        val monthsLittle = arrayOf("4", "6", "9", "11")
        val listBig = listOf(*monthsBig)
        val listLittle = listOf(*monthsLittle)
        currentYear = year
        // 年
        yearView.adapter = NumericWheelAdapter(
            startYear,
            endYear
        ) // 设置"年"的显示数据
        yearView.currentItem = year - startYear // 初始化时显示的数据
        // 月
        when (year) {
            startYear -> {
                //起始日期的月份控制
                monthView.adapter = NumericWheelAdapter(startMonth, 12)
                monthView.currentItem = month + 1 - startMonth
            }
            endYear -> {
                //终止日期的月份控制
    //            monthView.adapter = NumericWheelAdapter(1, endMonth)
                monthView.adapter = NumericWheelAdapter(1, month + 1)
                monthView.currentItem = month
            }
            else -> {
                monthView.adapter = NumericWheelAdapter(1, 12)
                monthView.currentItem = month
            }
        }
        // 日
        val leapYear = year % 4 == 0 && year % 100 != 0 || year % 400 == 0
        if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            when {
                listBig.contains((month + 1).toString()) -> {
                    dayView.adapter = NumericWheelAdapter(startDay, 31)
                }
                listLittle.contains((month + 1).toString()) -> {
                    dayView.adapter = NumericWheelAdapter(startDay, 30)
                }
                else -> {
                    // 闰年 29，平年 28
                    dayView.adapter = NumericWheelAdapter(startDay, if (leapYear) 29 else 28)
                }
            }
            dayView.currentItem = day - startDay
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (listBig.contains((month + 1).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                dayView.adapter = NumericWheelAdapter(1, endDay)
            } else if (listLittle.contains((month + 1).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                dayView.adapter = NumericWheelAdapter(1, endDay)
            } else {
                // 闰年
                if (leapYear) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    dayView.adapter = NumericWheelAdapter(1, endDay)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    dayView.adapter = NumericWheelAdapter(1, endDay)
                }
            }
            dayView.currentItem = day - 1
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            when {
                listBig.contains((month + 1).toString()) -> {
                    dayView.adapter = NumericWheelAdapter(1, 31)
                }
                listLittle.contains((month + 1).toString()) -> {
//                    dayView.adapter = NumericWheelAdapter(1, 30)
                    dayView.adapter = NumericWheelAdapter(1, day)
                }
                else -> {
                    // 闰年 29，平年 28
                    dayView.adapter = NumericWheelAdapter(startDay, if (leapYear) 29 else 28)
                }
            }
            dayView.currentItem = day - 1
        }


        // 添加"年"监听
        yearView.setOnItemSelectedListener { index: Int ->
            val yearNum = index + startYear
            currentYear = yearNum
            var currentMonthItem = monthView.currentItem //记录上一次的item位置
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (yearNum == startYear) { //等于开始的年
                //重新设置月份
                monthView.adapter = NumericWheelAdapter(startMonth, 12)
                if (currentMonthItem > monthView.adapter.itemsCount - 1) {
                    currentMonthItem = monthView.adapter.itemsCount - 1
                    monthView.currentItem = currentMonthItem
                }
                val month1 = currentMonthItem + startMonth
                if (month1 == startMonth) {
                    //重新设置日
                    setReDay(yearNum, month1, startDay, 31, listBig, listLittle)
                } else {
                    //重新设置日
                    setReDay(yearNum, month1, 1, 31, listBig, listLittle)
                }
            } else if (yearNum == endYear) {
                //重新设置月份
                if (calendar.get(Calendar.YEAR) == yearNum) {
                    monthView.adapter = NumericWheelAdapter(1, calendar.get(Calendar.MONTH) + 1)
                } else {
                    monthView.adapter = NumericWheelAdapter(1, endMonth)
                }
                if (currentMonthItem > monthView.adapter.itemsCount - 1) {
                    currentMonthItem = monthView.adapter.itemsCount - 1
                    monthView.currentItem = currentMonthItem
                }
                val monthNum = currentMonthItem + 1
                if (monthNum == endMonth) {
                    //重新设置日
                    setReDay(yearNum, monthNum, 1, endDay, listBig, listLittle)
                } else {
                    //重新设置日
                    setReDay(yearNum, monthNum, 1, 31, listBig, listLittle)
                }
            } else {
                //重新设置月份
                monthView.adapter = NumericWheelAdapter(1, 12)
                //重新设置日
                setReDay(yearNum, monthView.currentItem + 1, 1, 31, listBig, listLittle)
            }
            mSelectChangeCallback?.onTimeSelectChanged(getTime())
        }


        // 添加"月"监听
        monthView.setOnItemSelectedListener { index: Int ->
            var monthNum = index + 1
            if (currentYear == startYear) {
                monthNum = monthNum + startMonth - 1
                if (monthNum == startMonth) {
                    //重新设置日
                    setReDay(currentYear, monthNum, startDay, 31, listBig, listLittle)
                } else {
                    //重新设置日
                    setReDay(currentYear, monthNum, 1, 31, listBig, listLittle)
                }
            } else if (currentYear == endYear) {
                if (monthNum == endMonth) {
                    //重新设置日
                    setReDay(
                        currentYear,
                        monthView.currentItem + 1,
                        1,
                        endDay,
                        listBig,
                        listLittle
                    )
                } else {
                    setReDay(
                        currentYear,
                        monthView.currentItem + 1,
                        1,
                        31,
                        listBig,
                        listLittle
                    )
                }
            } else {
                //重新设置日
                setReDay(currentYear, monthNum, 1, 31, listBig, listLittle)
            }
            mSelectChangeCallback?.onTimeSelectChanged(getTime())
        }
        setChangedListener(dayView)
    }


    private fun setReDay(
        yearNum: Int,
        monthNum: Int,
        dayStartNum: Int,
        dayEndNum: Int,
        list_big: List<String>,
        list_little: List<String>
    ) {
        var endD = dayEndNum
        var currentItem = dayView.currentItem
        when {
            list_big.contains(monthNum.toString()) -> {
                if (endD > 31) {
                    endD = 31
                }
                dayView.adapter = NumericWheelAdapter(dayStartNum, endD)
            }
            list_little.contains(monthNum.toString()) -> {
                if (endD > 30) {
                    endD = 30
                }
                if (calendar.get(Calendar.MONTH) + 1 == monthNum && calendar.get(Calendar.YEAR) == yearNum) {
                    endD = calendar.get(Calendar.DAY_OF_MONTH)
                }
                dayView.adapter = NumericWheelAdapter(dayStartNum, endD)
            }
            else -> {
                if (yearNum % 4 == 0 && yearNum % 100 != 0
                    || yearNum % 400 == 0
                ) {
                    if (endD > 29) {
                        endD = 29
                    }
                } else {
                    if (endD > 28) {
                        endD = 28
                    }
                }
                dayView.adapter = NumericWheelAdapter(dayStartNum, endD)
            }
        }
        if (currentItem > dayView.adapter.itemsCount - 1) {
            currentItem = dayView.adapter.itemsCount - 1
            dayView.currentItem = currentItem
        }
    }

    private fun setChangedListener(wheelView: WheelView?) {
        wheelView?.setOnItemSelectedListener { mSelectChangeCallback?.onTimeSelectChanged(getTime()) }
    }

    fun setSelectChangeCallback(mSelectChangeCallback: ISelectTimeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback
    }


    fun getTime(): String {
        val sb = StringBuilder()
        if (currentYear == startYear) {
            val currentItem = monthView.currentItem
            if (currentItem + startMonth == startMonth) {
                val currentItem1 = yearView.currentItem
                val currentItem2 = monthView.currentItem
                val currentItem3 = dayView.currentItem
                sb.append(currentItem1 + startYear).append("-")
                    .append(currentItem2 + startMonth).append("-")
                    .append(currentItem3 + startDay).append(" ")
            } else {
                val currentItem1 = yearView.currentItem
                val currentItem2 = monthView.currentItem
                val currentItem3 = dayView.currentItem
                sb.append(currentItem1 + startYear).append("-")
                    .append(currentItem2 + startMonth).append("-")
                    .append(currentItem3 + 1).append(" ")
            }
        } else {
            val currentItem = yearView.currentItem
            val currentItem1 = monthView.currentItem
            val currentItem2 = dayView.currentItem
            sb.append(currentItem + startYear).append("-")
                .append(currentItem1 + 1).append("-")
                .append(currentItem2 + 1).append(" ")
        }
        return sb.toString()
    }
}

@BindingAdapter("bindDateChangeCallback")
fun TimePickerView.bindDateChangeCallback(dataBlock: ((String) -> Unit)?) {
    if (dataBlock == null) {
        return
    }
    setSelectChangeCallback(ISelectTimeCallback {
        dataBlock(it)
    })
}