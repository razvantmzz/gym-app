package com.razvantmz.onemove.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.core.utils.SingleLiveEvent
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.validations.IValidationProvider

abstract class BaseViewModel : ViewModel() {

    private var _validationFields: ArrayList<IValidationProvider> = arrayListOf()

    val onErrorCode: SingleLiveEvent<ResponseCode> =
        SingleLiveEvent()

    val onSuccessCode: SingleLiveEvent<ResponseCode> =
        SingleLiveEvent()

    val isUpdating = MutableLiveData<Boolean>()

    private val _isScreenReady: MutableLiveData<Boolean> = MutableLiveData()

    fun setIsScreenReady(bool:Boolean)
    {
        _isScreenReady.value = bool
    }

    fun getIsScreenReady() : LiveData<Boolean>
    {
        return _isScreenReady
    }

    fun addValidationFields(vararg args: IValidationProvider)
    {
        _validationFields.addAll(args)
        onValidationFieldsAdded(args.toList())
    }

    open fun onValidationFieldsAdded(validationFields: List<IValidationProvider>)
    {

    }

    open fun areInputsValid(displaySnackbar: Boolean = true) : Boolean
    {
        if(_validationFields.isEmpty())
        {
           return true
        }
        for(field in _validationFields)
        {
            var result = field.performValidation(displayError = false, hasFocus = false)
            if(result.isValid)
            {
                continue
            }
            if(displaySnackbar)
            {
                showErrorSnackbar(result.validationMessage)
            }
            return false
        }
        return true
    }
}