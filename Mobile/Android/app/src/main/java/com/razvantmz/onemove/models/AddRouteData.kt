package com.razvantmz.onemove.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddRouteData(
    @Expose
    @SerializedName("setters")
    val setters:List<Setter>,

    @Expose
    @SerializedName("locations")
    val locations:List<RouteLocation>
)