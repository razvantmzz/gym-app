package com.razvantmz.onemove.ui.signIn

import androidx.lifecycle.MutableLiveData
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.validations.IValidationProvider
import com.razvantmz.onemove.core.validations.commands.EmailCommand
import com.razvantmz.onemove.core.validations.commands.MinMaxLengthCommand
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.models.UserDataModel
import com.razvantmz.onemove.repository.AccountRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel() {

    private var email: MutableLiveData<String> = MutableLiveData()
    private var password: MutableLiveData<String> = MutableLiveData()

    private val signInJob = Job()
    private val signInContext = CoroutineScope(Dispatchers.IO + signInJob)

    fun setEmail(value:String)
    {
        email.value = value
    }

    fun setPassword(value:String)
    {
        password.value = value
    }

    override fun onValidationFieldsAdded(validationFields: List<IValidationProvider>) {
        validationFields[0].validationCommands.add(EmailCommand(R.string.email))
        validationFields[1].validationCommands.add(MinMaxLengthCommand(R.string.email, 3, 20))
    }

    fun signIn()
    {
        signInContext.launch {
            isUpdating.postValue(true)
            AccountRepository.signIn(email.value!!, password.value!!, object : ApiCallback<UserDataModel?>{
                override fun onSuccess(responseCode: ResponseCode, data: UserDataModel?) {
                    isUpdating.postValue(false)
                    onSuccessCode.postValue(responseCode)
                    UserCore.Instance.user = data
                }

                override fun onFailure(responseCode: ResponseCode) {
                    isUpdating.postValue(false)
                    onErrorCode.postValue(responseCode)
                }
            })
        }
    }
}