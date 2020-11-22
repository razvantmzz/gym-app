package com.razvantmz.onemove.core.validations

object ValidationResultFactory {
    fun successValidationResult() : ValidationResult
    {
        return ValidationResult(true)
    }

    fun errorValidationResult(errorMessage: String) : ValidationResult
    {
        return ValidationResult(false, errorMessage)
    }
}