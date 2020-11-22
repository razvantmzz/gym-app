package com.razvantmz.onemove.ui.register

import androidx.lifecycle.MutableLiveData
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.ValidationConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.validations.IValidationProvider
import com.razvantmz.onemove.core.validations.ValidationCommand
import com.razvantmz.onemove.core.validations.commands.EmailCommand
import com.razvantmz.onemove.core.validations.commands.MinLengthCommand
import com.razvantmz.onemove.core.validations.commands.SameAsCommand
import com.razvantmz.onemove.models.RegisterAccount
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.repository.AccountRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.*

class RegisterViewModel : BaseViewModel() {

    private var firstName: MutableLiveData<String> = MutableLiveData<String>()
    private var lastName: MutableLiveData<String> = MutableLiveData<String>()
    private var email: MutableLiveData<String> = MutableLiveData<String>()
    private var password: MutableLiveData<String> = MutableLiveData<String>()
    private var confirmPassword: MutableLiveData<String> = MutableLiveData<String>()
    private var subscribtionNumber: MutableLiveData<String> = MutableLiveData<String>()

    fun setFirstName(value:String)
    {
        firstName.value = value
    }

    fun setLastName(value:String)
    {
        lastName.value = value
    }

    fun setEmail(value:String)
    {
        email.value = value
    }

    fun setPassword(value:String)
    {
        password.value = value
    }

    fun setConfirmPassword(value:String)
    {
        confirmPassword.value = value
    }

    fun setSubscribtionNumber(value:String)
    {
        subscribtionNumber.value = value
    }

    private val registerJob = Job()
    private var registerScope = CoroutineScope(Dispatchers.IO + registerJob)

    override fun onValidationFieldsAdded(validationFields: List<IValidationProvider>) {
        validationFields[0].validationCommands.add(MinLengthCommand(R.string.name, 3))
        validationFields[1].validationCommands.add(MinLengthCommand(R.string.last_name, 3))
        validationFields[2].validationCommands.add(EmailCommand(R.string.email))
        validationFields[3].validationCommands.add(MinLengthCommand(R.string.password, 4))
        validationFields[4].validationCommands.add(MinLengthCommand(R.string.confirm_password, 4))
        validationFields[4].validationCommands.add(SameAsCommand(R.string.password, R.string.confirm_password, validationFields[3]))
    }

    fun registerUser()
    {
        if(!areInputsValid())
        {
            return;
        }

        isUpdating.postValue(true)
        registerScope.launch {
            val account = RegisterAccount(
                firstName.value!!,
                lastName.value!!,
                email.value!!,
                password.value!!,
                confirmPassword.value!!,
                subscribtionNumber.value!!
            )
            AccountRepository.registerAccount(account, object : ApiCallback<StringApiResponse>{
                override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                    isUpdating.postValue(false)
                    onSuccessCode.postValue(responseCode)
                }

                override fun onFailure(responseCode: ResponseCode) {
                    isUpdating.postValue(false)
                    onErrorCode.postValue(responseCode)
                }
            })
        }
    }

    override fun areInputsValid(displaySnackbar: Boolean): Boolean {
        return super.areInputsValid(displaySnackbar)
    }

//    private fun areInputsValid(): Boolean
//    {
//        if(firstName.value!!.length < ValidationConstants.MIN_LENGTH)
//        {
//            onErrorCode.setValue(ResponseCode.InvalidInputs)
//            return false;
//        }
//
//        if(lastName.value!!.length < ValidationConstants.MIN_LENGTH)
//        {
//            onErrorCode.setValue(ResponseCode.InvalidInputs)
//            return false;
//        }
//
//        val emailRegex = ValidationConstants.EMAIL_REGEX.toRegex()
//        if(!emailRegex.matches(email.value!!))
//        {
//            onErrorCode.setValue(ResponseCode.InvalidInputs)
//            return false;
//        }
//
//        if(password.value!!.length < ValidationConstants.MIN_PASSWORD_LENGTH)
//        {
//            onErrorCode.setValue(ResponseCode.InvalidInputs)
//            return false;
//        }
//
//        if(confirmPassword.value!!.length < ValidationConstants.MIN_LENGTH || !confirmPassword.value.equals(password.value))
//        {
//            onErrorCode.setValue(ResponseCode.InvalidInputs)
//            return false;
//        }
//
//        return true
//    }

    //

}