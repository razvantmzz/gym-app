package com.razvantmz.onemove.models.postModels

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class FeeEntryPost(
    var id:String,
    var eventId:String,
    var title:String,
    var priceValue:Float,
    var currency: String,
    var startDateIso:String,
    var endDateIso:String,
    var additionalData:String
)