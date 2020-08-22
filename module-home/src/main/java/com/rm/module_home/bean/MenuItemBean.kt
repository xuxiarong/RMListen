package com.rm.module_home.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
data class MenuItemBean(
    val name: String,
    val authorName: String,
    val authorIcon: String,
    val collectionNumber: String,
    val totalNumber: Int,
    val bookList: List<BookBean>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createTypedArrayList(BookBean)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(authorName)
        parcel.writeString(authorIcon)
        parcel.writeString(collectionNumber)
        parcel.writeInt(totalNumber)
        parcel.writeTypedList(bookList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuItemBean> {
        override fun createFromParcel(parcel: Parcel): MenuItemBean {
            return MenuItemBean(parcel)
        }

        override fun newArray(size: Int): Array<MenuItemBean?> {
            return arrayOfNulls(size)
        }
    }

}