package com.razvantmz.onemove.core.extensions

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.widget.TimePicker
import com.razvantmz.onemove.core.dialogs.RangeTimePickerDialog
import java.util.*

inline fun Context.showTimePickerDialog(builder: TimePickerBuilder.() -> Unit = {}) : TimePickerDialog
{
    val builderValues = TimePickerBuilder().apply(builder)

    val dpd = RangeTimePickerDialog(
        this,
        builderValues.onDateSetListener,
        builderValues.hour,
        builderValues.minutes,
        true
    )
    if(builderValues.message.isNotEmpty())
    {
        dpd.setMessage(builderValues.message)
    }
    if(builderValues.onShowListener != null)
    {
        dpd.setOnShowListener(builderValues.onShowListener)
    }

    if(builderValues.onTimeChangedListener != null)
    {
        dpd.setOnTimeChangedListener(builderValues.onTimeChangedListener)
    }

    dpd.show()
    return dpd
}

inline fun Context.showRangeTimePickerDialog(builder: TimePickerBuilder.() -> Unit = {}) : RangeTimePickerDialog
{
    val builderValues = TimePickerBuilder().apply(builder)

    val dpd = RangeTimePickerDialog(
        this,
        builderValues.onDateSetListener,
        builderValues.hour,
        builderValues.minutes,
        true
    )
    if(builderValues.message.isNotEmpty())
    {
        dpd.setMessage(builderValues.message)
    }
    if(builderValues.onShowListener != null)
    {
        dpd.setOnShowListener(builderValues.onShowListener)
    }

    if(builderValues.onTimeChangedListener != null)
    {
        dpd.setOnTimeChangedListener(builderValues.onTimeChangedListener)
    }

    dpd.show()
    return dpd
}

inline fun Context.showTimeRangeDialog(builder: TimeRangePickerBuilder.() -> Unit = {})
{
    val builderValues = TimeRangePickerBuilder().apply(builder)
    var startHour:Int = builderValues.startHour
    var startMinute:Int = builderValues.startMinute
    var endHour:Int = builderValues.endHour
    var endMinute:Int = builderValues.endMinute
    val context = this

    val startDialog = this.showRangeTimePickerDialog()
    {
        hour = builderValues.startHour
        minutes = builderValues.startMinute
        message = "${startHour.formatHourMin()}:${startMinute.formatHourMin()} - ${endHour.formatHourMin()}:${startHour.formatHourMin()}"
        onDateSetListener = object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                startHour = hourOfDay
                startMinute = minute
                var endDialog = context.showRangeTimePickerDialog()
                {
                    hour = builderValues.endHour
                    minutes = builderValues.endMinute
                    message = "${startHour.formatHourMin()}:${startMinute.formatHourMin()} - ${endHour.formatHourMin()}:${startHour.formatHourMin()}"
                    onDateSetListener =
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            endHour = hourOfDay
                            endMinute = minute
                            builderValues.onDateSetListener?.onTimeRangeSet(startHour, startMinute, endHour, endMinute)
                        }
                }
                endDialog.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener
                {
                    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        endDialog.setMessage("${startHour.formatHourMin()}:${startMinute.formatHourMin()} - ${hourOfDay.formatHourMin()}:${minute.formatHourMin()}")
                    }
                })
                endDialog.setMin(startHour, startMinute)
            }
        }
    }
    startDialog.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener
    {
        override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
            startDialog.setMessage(" ${hourOfDay.formatHourMin()}:${minute.formatHourMin()} - ${endHour.formatHourMin()}:${startHour.formatHourMin()}")
        }
    })
}

fun Int.formatHourMin():String
{
    return if(this < 10)
    {
        "0$this"
    }
    else
    {
        this.toString()
    }
}

class TimePickerBuilder() {
    var hour:Int
    var minutes:Int
    var message:String = ""
    var onDateSetListener: TimePickerDialog.OnTimeSetListener?
    var onShowListener: DialogInterface.OnShowListener?
    var onTimeChangedListener: TimePicker.OnTimeChangedListener?

    init {
        var c: Calendar = Calendar.getInstance()
        hour = c.get(Calendar.HOUR_OF_DAY)
        minutes = c.get(Calendar.MINUTE)
        onDateSetListener = null
        onShowListener = null
        onTimeChangedListener = null
    }
}

class TimeRangePickerBuilder() {
    var startHour:Int
    var startMinute:Int
    var endHour:Int
    var endMinute:Int
    var startMessage:String = ""
    var endMessage:String = ""
    var onDateSetListener: OnTimeRangeSetListener?
    var onTimeChangedListener: TimePicker.OnTimeChangedListener?

    init {
        var c: Calendar = Calendar.getInstance()
        startHour = c.get(Calendar.HOUR_OF_DAY)
        startMinute = c.get(Calendar.MINUTE)
        endHour = startHour
        val t = c.timeInMillis
        endMinute = Date(t + (5 * 60000)).minutes
        onDateSetListener = null
        onTimeChangedListener = null
    }

    interface OnTimeRangeSetListener
    {
        fun onTimeRangeSet(startHour:Int, startMinute:Int, endHour:Int, endMin:Int)
    }
}