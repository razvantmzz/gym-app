package com.razvantmz.onemove.core.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.addMinutes(minute:Int):Date
{
    val ONE_MINUTE_IN_MILLIS: Long = 60000 //millisecs
    val curTimeInMs: Long = this.getTime()
    return Date(curTimeInMs + minute * ONE_MINUTE_IN_MILLIS)
}

fun Date.fromHourMin(hour:Int, min:Int):Date
{
    val c = Calendar.getInstance()
    c[Calendar.DATE] = 2
    c[Calendar.HOUR_OF_DAY] = hour
    c[Calendar.MINUTE] = min
    c[Calendar.SECOND] = 0
    c[Calendar.MILLISECOND] = 0

    return c.time
}

fun Long.datefromMilli():Date
{
    val c = Calendar.getInstance()
    c.timeInMillis = this
    return c.time
}

fun Date.addDays(days: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DATE, days) //minus number would decrement the days
    return cal.time
}

fun Calendar.addDays(days: Int): Calendar {
    val cal = Calendar.getInstance()
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val day = get(Calendar.DAY_OF_MONTH)
    cal.set(year, month, day)
    cal.add(Calendar.DATE, days) //minus number would decrement the days
    return cal
}

fun Calendar.addMinutes(minutes:Int):Calendar
{
    val cal = Calendar.getInstance()
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val day = get(Calendar.DAY_OF_MONTH)
    val hour = get(Calendar.HOUR_OF_DAY)
    val min = get(Calendar.MINUTE)

    cal.set(year, month, day, hour, min)
    cal.add(Calendar.MINUTE, minutes)
    return cal
}

fun Calendar.addHours(hours:Int):Calendar
{
    this.add(Calendar.HOUR_OF_DAY, hours)
    return this
}

fun Calendar.setMinutes(minutes:Int):Calendar
{
    this.set(Calendar.MINUTE, minutes)
    return this
}

fun Calendar.setHours(hours:Int):Calendar
{
    this.set(Calendar.HOUR_OF_DAY, hours)
    return this
}

fun Calendar.setHoursMin(hours:Int, minutes: Int):Calendar
{
    val cal = Calendar.getInstance()
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val day = get(Calendar.DAY_OF_MONTH)
    val hour = get(Calendar.HOUR_OF_DAY)
    val min = get(Calendar.MINUTE)

    cal.set(year, month, day, hour, min)
    cal.set(Calendar.HOUR_OF_DAY, hours)
    cal.set(Calendar.MINUTE, minutes)
    return cal
}

fun Calendar.getHours():Int
{
    return get(Calendar.HOUR_OF_DAY)
}

fun Calendar.getMinutes():Int
{
    return get(Calendar.MINUTE)
}

fun Calendar.getMonth():Int
{
    return get(Calendar.MONTH)
}

fun Calendar.getMonthName():String
{
    return SimpleDateFormat("MMM").format(getTime())
}

fun Calendar.getDay():Int
{
    return get(Calendar.DAY_OF_MONTH)
}

fun Calendar.getDate():Date
{
    val cal = Calendar.getInstance()
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val day = get(Calendar.DAY_OF_MONTH)

    cal.set(year, month, day, 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.time
}

fun Date.toCalendar():Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}
