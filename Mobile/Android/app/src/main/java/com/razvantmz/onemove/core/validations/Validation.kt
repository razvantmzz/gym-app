package com.razvantmz.onemove.core.validations

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.helpers.showErrorSnackbar

class Validation {
    companion object{
        fun validateEmail(context: Context, coordinatorLayout: CoordinatorLayout, email: String) : Boolean
        {
            val emailRegex = ValidationConstants.EMAIL_REGEX.toRegex()
            if(!emailRegex.matches(email))
            {
                showErrorSnackbar(context.getString(R.string.error_invalid_email))
                return false
            }
            return true
        }

        fun validateMinLength(context: Context, coordinatorLayout: CoordinatorLayout, inputName:String,  text: String, minLength: Int) : Boolean
        {
            if(text.length < minLength)
            {
                showErrorSnackbar(context.getString(R.string.error_min_length, inputName, minLength))
                return false
            }
            return true
        }
    }
}