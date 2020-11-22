package com.razvantmz.onemove.core.extensions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.datepicker.MaterialDatePicker
import com.razvantmz.onemove.R
import com.razvantmz.onemove.dialogs.SentRouteDialog
import com.razvantmz.onemove.models.RouteModel
import kotlinx.android.synthetic.main.dialog_sent_route.*
import java.util.*
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

class DatePickerBuilder() {
    var year:Int
    var month:Int
    var day:Int
    var onDateSetListener: DatePickerDialog.OnDateSetListener?

    init {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        onDateSetListener = null
    }
}

inline fun Context.showDateDialog(builder: DatePickerBuilder.() -> Unit = {})
{
    val builderValues = DatePickerBuilder().apply(builder)

    val dpd = DatePickerDialog(
        this,
        builderValues.onDateSetListener,
        builderValues.year,
        builderValues.month,
        builderValues.day
    )
    dpd.show()
}

inline fun FragmentManager.showDateRangePickerDialog(positiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<in Pair<Long, Long>>)
{
    val builder = MaterialDatePicker.Builder.dateRangePicker()
    val now = Calendar.getInstance()
//    builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.addDays(7).timeInMillis))
    val picker = builder.build()
    picker.addOnPositiveButtonClickListener(positiveButtonClickListener)
    picker.show(this, picker.toString())
}
