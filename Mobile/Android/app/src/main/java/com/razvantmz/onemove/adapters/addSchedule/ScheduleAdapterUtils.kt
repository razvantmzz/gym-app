package com.razvantmz.onemove.adapters.addSchedule

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.extensions.addMinutes
import com.razvantmz.onemove.core.extensions.setHoursMin
import com.razvantmz.onemove.databinding.CellScheduleEntryBinding
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.event.ScheduleEntry
import java.util.*

const val DEFAULT_SCHEDULE_HOUR = 7
const val DEFAULT_SCHEDULE_MIN = 0
const val DEFAULT_SCHEDULE_MIN_PLUS = 30


fun getEmptySchedule(date:Calendar): Schedule
{
    val schedule = Schedule(UUID.randomUUID(), date, arrayListOf())
    schedule.entrys.add(getEmptyScheduleEntry(schedule.date))
    return schedule;
}

fun getEmptyScheduleEntry(currendDate:Calendar): ScheduleEntry
{
    return ScheduleEntry(UUID.randomUUID(), currendDate.setHoursMin(DEFAULT_SCHEDULE_HOUR, DEFAULT_SCHEDULE_MIN), currendDate.setHoursMin(
        DEFAULT_SCHEDULE_HOUR, DEFAULT_SCHEDULE_MIN_PLUS),  "")
}

fun getNextScheduleEntry(previousEntry:ScheduleEntry? = null): ScheduleEntry
{
    if(previousEntry == null)
    {
        return ScheduleEntry(UUID.randomUUID(), Calendar.getInstance(), Calendar.getInstance(),  "")
    }

    return ScheduleEntry(UUID.randomUUID(), previousEntry.endDate, previousEntry.endDate.addMinutes(30), "")
}

fun CellScheduleEntryBinding.setDecoratorColor(context: Context, entry:ScheduleEntry)
{
    val color = if(entry.description.isNullOrEmpty())
    {
        context.getColor(R.color.table_decorations_inactive)
    }
    else
    {
        context.getColor(R.color.table_decorations_active)
    }

    decoratorVerticalBar.setBackgroundColor(color)
    val sampleDrawable = context.getDrawable(R.drawable.round_image_form)
    sampleDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    decoratorCircle.setBackground(sampleDrawable)

}