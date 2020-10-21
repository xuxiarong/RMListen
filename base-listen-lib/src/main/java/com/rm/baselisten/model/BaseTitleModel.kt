package com.rm.baselisten.model

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.R

/**
 * desc   :
 * date   : 2020/08/06
 * version: 1.0
 */
class BaseTitleModel {

    var noTitle = false
    var noBack = false
    var hasDivider = false
    var leftIcon = R.drawable.base_icon_back
    var leftIcon1 = 0
    var leftText = ""
    var mainTitle = ""
    var subTitle = ""
    var rightBackground = 0
    var rightIcon = 0
    var rightIcon1 = 0
    var rightText = ""
    var leftIconClick: (() -> Unit)? = null
    var leftIcon1Click: (() -> Unit)? = null
    var leftTextClick: (() -> Unit)? = null
    var rightContainerClick: (() -> Unit)? = null
    var rightIconClick: (() -> Unit)? = null
    var rightIcon1Click: (() -> Unit)? = null
    var rightTextClick: (() -> Unit)? = null
    var rightTextColorRes = R.color.base_666666
    var rightTextEnabled = true


    fun setHasDivider(hasDivider: Boolean): BaseTitleModel {
        this.hasDivider = hasDivider
        return this
    }

    fun setNoTitle(noTitle: Boolean): BaseTitleModel {
        this.noTitle = noTitle
        return this
    }

    fun setNoBack(noBack: Boolean): BaseTitleModel {
        this.noBack = noBack
        return this
    }

    fun setLeftIcon(@DrawableRes leftIcon: Int): BaseTitleModel {
        this.leftIcon = leftIcon
        return this
    }

    fun setLeftIconClick(leftIconClick: () -> Unit): BaseTitleModel {
        this.leftIconClick = leftIconClick
        return this
    }


    fun setLeftIcon1(@DrawableRes leftIcon1: Int): BaseTitleModel {
        this.leftIcon1 = leftIcon1
        return this
    }

    fun setLeftIcon1Click(leftIcon1Click: () -> Unit): BaseTitleModel {
        this.leftIcon1Click = leftIcon1Click
        return this
    }


    fun setLeftText(leftText: String): BaseTitleModel {
        this.leftText = leftText
        return this
    }

    fun setLeftTextClick(leftTextClick: () -> Unit): BaseTitleModel {
        this.leftTextClick = leftTextClick
        return this
    }

    fun setRightBackground(@DrawableRes rightBackground: Int): BaseTitleModel {
        this.rightBackground = rightBackground
        return this
    }

    fun setRightContainerClick(rightContainerClick: () -> Unit): BaseTitleModel {
        this.rightContainerClick = rightContainerClick
        return this
    }

    fun setRightIcon(@DrawableRes rightIcon: Int): BaseTitleModel {
        this.rightIcon = rightIcon
        return this
    }

    fun setRightIconClick(rightIconClick: () -> Unit): BaseTitleModel {
        this.rightIconClick = rightIconClick
        return this
    }


    fun setRightIcon1(@DrawableRes rightIcon1: Int): BaseTitleModel {
        this.rightIcon1 = rightIcon1
        return this
    }

    fun setRightIcon1Click(rightIcon1Click: () -> Unit): BaseTitleModel {
        this.rightIcon1Click = rightIcon1Click
        return this
    }


    fun setRightText(leftText: String): BaseTitleModel {
        this.rightText = leftText
        return this
    }

    fun setRightTextClick(rightTextClick: () -> Unit): BaseTitleModel {
        this.rightTextClick = rightTextClick
        return this
    }

    fun setRightTextColor(rightColor: Int): BaseTitleModel {
        this.rightTextColorRes = ContextCompat.getColor(BaseApplication.CONTEXT, rightColor)
        return this
    }


    fun setRightEnabled(enabled: Boolean): BaseTitleModel {
        this.rightTextEnabled = enabled
        return this
    }


    fun setTitle(mainTitle: String): BaseTitleModel {
        this.mainTitle = mainTitle
        return this
    }

    fun setSubTitle(subTitle: String): BaseTitleModel {
        this.subTitle = subTitle
        return this
    }
}