package com.razvantmz.onemove.models.event

import java.util.*
class ScheduleEntry(
    var id:UUID,
    var startDate: Calendar,
    var endDate:Calendar,
    var description:String
) {
}