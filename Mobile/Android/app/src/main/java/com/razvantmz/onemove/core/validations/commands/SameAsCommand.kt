package com.razvantmz.onemove.core.validations.commands

import androidx.annotation.StringRes
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.customViews.EditTextValidation
import com.razvantmz.onemove.core.validations.IValidationProvider
import com.razvantmz.onemove.core.validations.ValidationCommand
import com.razvantmz.onemove.core.validations.ValidationResult
import com.razvantmz.onemove.core.validations.ValidationResultFactory
import com.razvantmz.onemove.ui.base.CoreApplication

class SameAsCommand(@StringRes var fieldName:Int, @StringRes var secondFieldName:Int, var secondField: IValidationProvider) : ValidationCommand() {

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
                    R.string.validation_same_as,
                    CoreApplication.Instance.applicationContext.getString(fieldName),
                    CoreApplication.Instance.applicationContext.getString(secondFieldName))
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
        return value == secondField.getStringValue()
    }
}