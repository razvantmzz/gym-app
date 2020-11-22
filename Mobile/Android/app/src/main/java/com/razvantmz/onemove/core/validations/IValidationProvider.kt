package com.razvantmz.onemove.core.validations

interface IValidationProvider {
    var validationCommands: ArrayList<ValidationCommand>

    fun getStringValue(): String

    fun performValidation(displayError:Boolean = true, hasFocus:Boolean = true): ValidationResult
}