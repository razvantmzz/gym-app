package com.razvantmz.onemove.core.helpers

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.razvantmz.onemove.ui.base.CoreApplication

fun showErrorSnackbar(text:String, autoShow:Boolean = true)
    {
        if(CoreApplication.currentActivity == null)
        {
            Log.e("DEBUG", "Current activity null")
            return
        }
        if(CoreApplication.currentActivity!!.coordinatorLayout == null)
        {
            Log.e("DEBUG", "Coordinator layout in current activity is null")
            return
        }
        val snackbar = Snackbar.make(CoreApplication.currentActivity!!.coordinatorLayout!!, text, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.RED)
        val view  = snackbar.view

        val params = CoordinatorLayout.LayoutParams(view.layoutParams.width, view.layoutParams.height)
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        if(autoShow)
        {
            snackbar.show()
        }
    }

    fun showMessageSnackbar(text:String, autoShow:Boolean = true)
    {
        if(CoreApplication.currentActivity == null)
        {
            Log.e("DEBUG", "Current activity null")
            return
        }
        if(CoreApplication.currentActivity!!.coordinatorLayout == null)
        {
            Log.e("DEBUG", "Coordinator layout in current activity is null")
            return
        }
        val snackbar = Snackbar.make(CoreApplication.currentActivity!!.coordinatorLayout!!, text, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.GREEN)
        val view  = snackbar.view

        val params = CoordinatorLayout.LayoutParams(view.layoutParams.width, view.layoutParams.height)
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        if(autoShow)
        {
            snackbar.show()
        }
    }