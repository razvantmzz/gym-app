package com.razvantmz.onemove.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Setter(
    var id:UUID,
    var name:String,
    var imageUrl:String
) : Parcelable