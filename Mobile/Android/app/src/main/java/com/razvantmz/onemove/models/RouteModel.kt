package com.razvantmz.onemove.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class RouteModel(
    var id:UUID,
    var type:Int,
    var name:String,
    var setter:Setter,
    var date: Date,
    var grade:String,
    var triesCount:Int,
    var location:String,
    var locationUrl:String,
    var imageUrl:String,
    var previewImageUrl:String,
    var index:Int,
    var holdsColor:Int,
    var likesCount:Int,
    var isFavorite: Boolean) : Parcelable {
}