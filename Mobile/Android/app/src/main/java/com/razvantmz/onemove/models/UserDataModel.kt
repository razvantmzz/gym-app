package com.razvantmz.onemove.models

import android.telephony.euicc.DownloadableSubscription
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class UserDataModel(
    @Expose
    @SerializedName("id")
    var id:UUID,

    @Expose
    @SerializedName("email")
    var email:String,

    @Expose
    @SerializedName("type")
    var type:Int,

    @Expose
    @SerializedName("image_url")
    var imageUrl:String,

    @Expose
    @SerializedName("cover_photo_url")
    var coverPhotoUrl:String,

    @Expose
    @SerializedName("first_name")
    var firstName:String,

    @Expose
    @SerializedName("last_name")
    var lastName:String,

    @Expose
    @SerializedName("subscription_number")
    var subscriptionNumber:String,

    @Expose
    @SerializedName("token")
    var token:String,

    @Expose
    @SerializedName("gender")
    var gender:Int,

    @Expose
    @SerializedName("category")
    var category:Int?
) : Any()
{
    val fullName = "$firstName $lastName"

}
