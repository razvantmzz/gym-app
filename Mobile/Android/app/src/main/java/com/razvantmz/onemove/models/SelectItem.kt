package com.razvantmz.onemove.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SelectItem(
    val id: Int,
    val name:String
) : Parcelable {

}