package com.razvantmz.onemove.core.responseHandlers

interface IResponseHandler {
    fun showErrorMessage(responseCode: ResponseCode)

    fun showMessage(responseCode: ResponseCode)

    fun showValidationMessage(responseCode: ResponseCode, validationField: ValidationField, param:String? = null)

}