package com.razvantmz.onemove.core.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.core.validations.IValidationProvider
import com.razvantmz.onemove.core.validations.ValidationCommand
import com.razvantmz.onemove.core.validations.ValidationResult
import com.razvantmz.onemove.core.validations.ValidationResultFactory

class EditTextValidation : androidx.appcompat.widget.AppCompatEditText, IValidationProvider {
    override var validationCommands: ArrayList<ValidationCommand> = arrayListOf()

    var shouldDisplaySnackbar: Boolean = true

    constructor(context: Context) : this(context, null)
    {

    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init
    {
        setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
            {
                return@setOnFocusChangeListener
            }
//            if(text.isNullOrEmpty() || text.isNullOrBlank())
//            {
//                return@setOnFocusChangeListener
//            }
            setText(text!!.trim())
            performValidation(displayError = true, hasFocus = false)
        }


        doOnTextChanged { text, start, count, after ->
            performValidation(false)
        }
    }


    override fun getStringValue(): String {
        return text.toString()
    }

    override fun performValidation(displayError: Boolean, hasFocus: Boolean): ValidationResult {
        var result = isValid()
        if(result != null && result.isValid)
        {
            resetError()
            return ValidationResultFactory.successValidationResult()
        }
        setError()
        if(displayError && !hasFocus && !result.isValid)
        {
            showErrorSnackbar(result.validationMessage)
        }
        return result
    }

    fun isValid() : ValidationResult
    {
        if(validationCommands.isEmpty())
        {
            return ValidationResultFactory.successValidationResult()
        }

        for (command in validationCommands) {
            val result = command.isValid(text.toString())
            if(result.isValid)
            {
                continue
            }
            return result
        }

        return ValidationResultFactory.successValidationResult()
    }

    private fun resetError()
    {
        setTextColor(ContextCompat.getColor(context, R.color.edit_text_normal))
        super.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_bacground))
    }

    private fun setError()
    {
        setTextColor(ContextCompat.getColor(context, R.color.edit_text_error))
        setError("", null)
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_error))
    }
}