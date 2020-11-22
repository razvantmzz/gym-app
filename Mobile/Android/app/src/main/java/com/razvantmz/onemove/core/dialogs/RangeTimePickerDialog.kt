package com.razvantmz.onemove.core.dialogs

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import com.razvantmz.onemove.R
import kotlinx.android.synthetic.main.toolbar_simple.*
import java.lang.reflect.Field
import java.text.DateFormat
import java.util.*


class RangeTimePickerDialog(
    context: Context?,
    listener: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, listener, hourOfDay, minute, is24HourView) {

    private var minHour = -1
    private var minMinute = -1

    private var maxHour = 25
    private var maxMinute = 25

    private var currentHour = 0
    private var currentMinute = 0

    private val calendar: Calendar = Calendar.getInstance()
    private var dateFormat: DateFormat? = null

    private var onTimeChangedListener: TimePicker.OnTimeChangedListener? = null
    var validTime = true

    init {
        currentHour = hourOfDay
        currentMinute = minute
        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        try {
            val superclass: Class<*>? = javaClass.superclass
            val mTimePickerField: Field = superclass!!.getDeclaredField("mTimePicker")
            mTimePickerField.isAccessible = true
            val mTimePicker = mTimePickerField.get(this) as TimePicker
            mTimePicker.setOnTimeChangedListener(this)
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
    }

    fun setMin(hour: Int, minute: Int) {
        minHour = hour
        minMinute = minute
        if (currentHour < minHour || currentHour == minHour && minute < minMinute) {
            validTime = false
        }
    }

    fun setMax(hour: Int, minute: Int) {
        maxHour = hour
        maxMinute = minute
        if (currentHour > maxHour || currentHour == maxHour && minute > maxMinute) {
            validTime = false
        }
    }

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        Log.d("DADADADA", "onTimeChanged")
        validTime = true
        if (hourOfDay < minHour || hourOfDay == minHour && minute < minMinute) {
            validTime = false
        }
        if (hourOfDay > maxHour || hourOfDay == maxHour && minute > maxMinute) {
            validTime = false
        }
//        if (validTime) {
            currentHour = hourOfDay
            currentMinute = minute
//        }
        updateTime(currentHour, currentMinute)
        updateDialogTitle(view, currentHour, currentMinute)
        onTimeChangedListener?.onTimeChanged(view, hourOfDay, minute)
    }

    override fun dismiss() {
        if(validTime)
        {
            super.dismiss()
        }
        else
        {
            Toast.makeText(context, "End Time cannot be smaller then start Time", Toast.LENGTH_SHORT).show()
        }
    }

    fun setOnTimeChangedListener(listener: TimePicker.OnTimeChangedListener?)
    {
        onTimeChangedListener = listener
    }

    private fun updateDialogTitle(
        timePicker: TimePicker,
        hourOfDay: Int,
        minute: Int
    ) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        if(dateFormat == null)
            return
        val title: String = dateFormat!!.format(calendar.getTime())
        setTitle(title)
    }
}