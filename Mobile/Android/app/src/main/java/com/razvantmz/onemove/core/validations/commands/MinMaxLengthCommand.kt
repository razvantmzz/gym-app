package com.razvantmz.onemove.core.validations.commands

import androidx.annotation.StringRes
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.validations.ValidationCommand
import com.razvantmz.onemove.core.validations.ValidationResult
import com.razvantmz.onemove.core.validations.ValidationResultFactory
import com.razvantmz.onemove.ui.base.CoreApplication

class MinMaxLengthCommand(@StringRes var fieldName:Int, var minValue:Int, var maxValue:Int) : ValidationCommand() {

    override fun isValid(value: String): ValidationResult {
        var succesValidationResult = ValidationResultFactory.successValidationResult()
        val isOptionalValidation = checkIfOptionalConditionsAreMet(value)
        if (isOptionalValidation) {
            return succesValidationResult
        }
//        val input =  if (fieldName.isEmpty())
//                                value
//                            else fieldName
        val errorValidationResult =
            ValidationResultFactory.errorValidationResult(
                CoreApplication.Instance.applicationContext.getString(
                    R.string.validation_min_max_length,
                    CoreApplication.Instance.applicationContext.getString(fieldName),
                    minValue, maxValue
                )
            )
        val isInputValid = isValidInput(value)
        return when {
            value.isEmpty() ->
                errorValidationResult
            isInputValid ->
                succesValidationResult
            else ->
                errorValidationResult
        }
    }

    private fun isValidInput(value:String): Boolean
    {
        return value.length in (minValue) until maxValue
    }
}