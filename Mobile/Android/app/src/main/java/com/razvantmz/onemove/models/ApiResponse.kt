package com.razvantmz.onemove.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> (
    @Expose
    @SerializedName("responseCode")
    var responseCode: Int,

    @Expose
    @SerializedName("isSuccessful")
    var isSuccessful: Boolean,

    @Expose
    @SerializedName("data")
    var data: T
)

class StringApiResponse (
    @Expose
    @SerializedName("responseCode")
    var responseCode: Int,

    @Expose
    @SerializedName("isSuccessful")
    var isSuccessful: Boolean,

    @Expose
    @SerializedName("data")
    var data: String
)