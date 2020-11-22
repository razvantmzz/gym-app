package com.razvantmz.onemove.core.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.toISOString(): String {
//    val tz = TimeZone.getTimeZone("UTC")
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
//    df.timeZone = tz
    val nowAsISO: String = df.format(this)
    return nowAsISO
}

fun String.fromISOString(): Calendar {
    val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    val cal = Calendar.getInstance()
    if(this.isNullOrEmpty())
    {
        return cal
    }

    val parsedDate = df1.parse(this)
    cal.time = parsedDate
    return cal
}

fun Date.toDisplayFormat(): String {
    val df: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Quoted "Z" to indicate UTC, no timezone offset
    return df.format(this)
}

fun Date.toScheduleFormat(): String {
    val df: DateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()) // Quoted "Z" to indicate UTC, no timezone offset
    return df.format(this)
}

fun Date.toHourMinFormat(): String {
    val df: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // Quoted "Z" to indicate UTC, no timezone offset
    return df.format(this)
}

fun Calendar.toHourMinFormat(): String {
    val df: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // Quoted "Z" to indicate UTC, no timezone offset
    return df.format(this.time)
}

fun Calendar.toDayMonthFormat(): String {
    val df: DateFormat = SimpleDateFormat("dd MMM", Locale.getDefault()) // Quoted "Z" to indicate UTC, no timezone offset
    return df.format(this.time)
}

fun Date.toSimpleDisplayFormat() : String
{
    var formatter = SimpleDateFormat("MMM dd yyyy")
    return formatter.format(this)
}

