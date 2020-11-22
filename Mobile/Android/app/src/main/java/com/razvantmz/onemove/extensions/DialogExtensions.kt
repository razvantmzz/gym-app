package com.razvantmz.onemove.extensions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.razvantmz.onemove.R
import com.razvantmz.onemove.dialogs.SentRouteDialog
import com.razvantmz.onemove.models.RouteModel
import kotlinx.android.synthetic.main.dialog_sent_route.*

inline fun FragmentManager.showMarkAsSentDialog(route: RouteModel, saveClickListener: SentRouteDialog.OnSaveClickListener) : SentRouteDialog
{
    val ft: FragmentTransaction = this.beginTransaction()
    val prev: Fragment? = this.findFragmentByTag("dialog")
    if (prev != null) {
        ft.remove(prev)
    }
    ft.addToBackStack(null)

    return SentRouteDialog(route.name, route.grade, saveClickListener).apply {
        show(ft, "dialog")
    }
}