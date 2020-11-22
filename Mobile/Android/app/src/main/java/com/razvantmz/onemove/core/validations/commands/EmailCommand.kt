package com.razvantmz.onemove.core.validations.commands

import androidx.annotation.StringRes
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.validations.ValidationCommand
import com.razvantmz.onemove.core.validations.ValidationResult
import com.razvantmz.onemove.core.validations.ValidationResultFactory
import com.razvantmz.onemove.ui.base.CoreApplication

class EmailCommand(@StringRes var fieldName:Int) : ValidationCommand() {

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
                CoreApplication.Instance.applicationContext.getString(R.string.validation_email,
                CoreApplication.Instance.applicationContext.getString(fieldName))
            )
        val isEmailValid = isValidEmail(value)
        return when {
            value.isEmpty() ->
                errorValidationResult
            isEmailValid ->
                succesValidationResult
            else ->
                errorValidationResult
        }
    }

    private fun isValidEmail(email: String): Boolean
    {
        val emailRegex = ValidationConstants.EMAIL_REGEX.toRegex()
        return emailRegex.matches(email)
    }
}