package com.rm.module_home.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
data class BookBean(
    val name:String,
    val icon:String,
    val tips:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(tips)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookBean> {
        override fun createFromParcel(parcel: Parcel): BookBean {
            return BookBean(parcel)
        }

        override fun newArray(size: Int): Array<BookBean?> {
            return arrayOfNulls(size)
        }
    }
}