package com.rm.baselisten.mvvm

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseViewModel() : ViewModel(),Parcelable {


    open class UiState<T>(
        val isLoading: Boolean = false,
        val isRefresh: Boolean = false,
        val isSuccess: T? = null,
        val isError: String? = null
    )

    val mException: MutableLiveData<Throwable> = MutableLiveData()

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseViewModel> {
        override fun createFromParcel(parcel: Parcel): BaseViewModel {
            return BaseViewModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseViewModel?> {
            return arrayOfNulls(size)
        }
    }
}