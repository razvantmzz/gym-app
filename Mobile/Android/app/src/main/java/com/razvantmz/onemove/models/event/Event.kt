package com.razvantmz.onemove.models.event

import com.razvantmz.onemove.models.price.PriceEntry
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.serialization.Serializable

data class Event(
            var id:UUID,
            var title:String,
            var startDate: Date,
            var endDate: Date,
            var isAllDay:Boolean,
            var scheduleList:ArrayList<Schedule>,
            var feesList:ArrayList<PriceEntry>,
            var formLink:String,
            var description:String,
            var rules:String,
            var coverPhotoUrl:String
) {
}

