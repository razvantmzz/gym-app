package com.razvantmz.onemove.models.postModels

import kotlinx.serialization.Serializable

@Serializable
class EventPost(
    var id:String,
    var title:String,
    var startDateIso:String,
    var endDateIso:String,
    var isAllDay:Boolean,
    var scheduleList:ArrayList<ScheduleEntryPost>,
    var feesList:ArrayList<FeeEntryPost>,
    var formLink:String,
    var description:String,
    var rules:String,
    var coverImgUrl:String
) {
}