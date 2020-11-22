package com.razvantmz.onemove.adapters.addPrice

import android.app.Application
import android.content.Context
import android.widget.EditText
import android.widget.ImageView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.extensions.addDays
import com.razvantmz.onemove.core.extensions.addMinutes
import com.razvantmz.onemove.databinding.CellPriceEntryBinding
import com.razvantmz.onemove.extensions.setVisibilityAnimated
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.event.ScheduleEntry
import com.razvantmz.onemove.models.price.PriceEntry
import java.util.*

fun getStandardPriceEntry(context: Context): PriceEntry
{
    return PriceEntry(UUID.randomUUID(), context.getString(R.string.standardFee), Calendar.getInstance().time, Calendar.getInstance().time.addDays(7),  -1f, Currency.getInstance("RON"), "")
}

fun getNextScheduleEntry(previousEntry:PriceEntry? = null): PriceEntry
{
    if(previousEntry == null)
    {
        var currencies= Currency.getAvailableCurrencies()
        return PriceEntry(UUID.randomUUID(),"", Calendar.getInstance().time, Calendar.getInstance().time.addDays(7),  -1f, Currency.getInstance("RON"), "")
    }

    return PriceEntry(UUID.randomUUID(),"", previousEntry.endDate, previousEntry.endDate.addDays(7),  -1f, Currency.getInstance("RON"), "")
}

fun EditText.setPriceOrHint(context: Context, price:Float)
{
    if(price == -1f)
    {
        this.hint = context.getString(R.string.addPrice)
    }
    else
    {
        this.setText(price.toString())
    }
}

fun EditText.setTitleOrHint(title:String)
{
    if(title.isNullOrEmpty())
    {
        this.hint = "Add_title"
    }
    else
    {
        this.setText(title)
    }
}

fun ImageView.setDeleteVisibility(entry:PriceEntry, position:Int)
{
    this.setVisibilityAnimated((entry.price != -1f && position != 0)
           )
}

fun CellPriceEntryBinding.setDecoratorColor(context: Context, entry:PriceEntry)
{
    if(entry.price == -1f)
    {
        this.decoratorCircle.setBackgroundColor(context.getColor(R.color.table_decorations_inactive))
        this.decoratorVerticalBar.setBackgroundColor(context.getColor(R.color.table_decorations_inactive))
    }
    else
    {
        this.decoratorCircle.setBackgroundColor(context.getColor(R.color.table_decorations_active))
        this.decoratorVerticalBar.setBackgroundColor(context.getColor(R.color.table_decorations_active))
    }
}
