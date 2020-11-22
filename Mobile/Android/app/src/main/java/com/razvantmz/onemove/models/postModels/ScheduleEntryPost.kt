package com.razvantmz.onemove.models.postModels

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class ScheduleEntryPost(
    var id:String,
    var eventId:String,
    var startDateIso: String,
    var endDateIso: String,
    var description:String) {
}