package com.razvantmz.onemove.core.validations

abstract class ValidationCommand {

    var isOptional:Boolean = false

    abstract fun isValid(value: String):ValidationResult

    protected fun checkIfOptionalConditionsAreMet(value:String) : Boolean
    {
        if(isOptional && value.isNullOrEmpty() && value.isNullOrBlank())
        {
            return true
        }
        return false
    }
}