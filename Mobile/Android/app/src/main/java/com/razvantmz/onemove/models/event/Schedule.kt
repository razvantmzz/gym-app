package com.razvantmz.onemove.models.event

import java.util.*
import kotlin.collections.ArrayList

class Schedule(
    var id:UUID,
    var date: Calendar,
    var entrys:ArrayList<ScheduleEntry>
) {
    init {
        id = UUID.randomUUID()
    }
}