package com.razvantmz.onemove.models.event

import com.razvantmz.onemove.core.extensions.getDate
import com.razvantmz.onemove.core.extensions.getDay
import com.razvantmz.onemove.core.extensions.toCalendar
import com.razvantmz.onemove.core.utils.fromISOString
import com.razvantmz.onemove.models.postModels.EventPost
import com.razvantmz.onemove.models.price.PriceEntry
import java.util.*
import kotlin.collections.ArrayList


fun EventPost.toEvent(): Event
{
    val scheduleEntries = arrayListOf<ScheduleEntry>()
    for (sch in scheduleList)
    {
        scheduleEntries.add(
            ScheduleEntry(
            UUID.fromString(sch.id),
                sch.startDateIso.fromISOString(),
                sch.endDateIso.fromISOString(),
                sch.description
            )
        )
    }

    val groupedSchedule = scheduleEntries.groupBy {
        it.startDate.getDate()
    }
    val scheduleList = arrayListOf<Schedule>()
    for (group in groupedSchedule)
    {
        scheduleList.add(Schedule(
            UUID.randomUUID(),
            group.key.toCalendar(),
            group.value as ArrayList<ScheduleEntry>
        ))
    }

    val feesEntries = arrayListOf<PriceEntry>()
    for (fee in feesList)
    {
        feesEntries.add(
            PriceEntry(
            UUID.fromString(fee.id),
                fee.title,
                fee.startDateIso.fromISOString().time,
                fee.endDateIso.fromISOString().time,
                fee.priceValue,
                Currency.getInstance(fee.currency),
                fee.additionalData
            )
        )
    }

    val event = Event(
        UUID.fromString(this.id),
        this.title,
        this.startDateIso.fromISOString().time,
        this.endDateIso.fromISOString().time,
        this.isAllDay,
        scheduleList,
        feesEntries,
        formLink,
        description,
        rules,
        coverImgUrl
    )

    return event
}