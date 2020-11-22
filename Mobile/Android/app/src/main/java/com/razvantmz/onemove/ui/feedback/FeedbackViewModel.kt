package com.razvantmz.onemove.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.repository.FeedbackRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedbackViewModel : BaseViewModel()
{
    private var _userEmail:MutableLiveData<String> = MutableLiveData()
    private var _feedbackText: MutableLiveData<String> = MutableLiveData()

    init {
        _userEmail.value = UserCore.Instance.user?.email
    }

    fun getEmail() : LiveData<String>
    {
        return  _userEmail
    }

    fun setFeedBackText(value:String)
    {
        _feedbackText.value = value
    }

    fun submitFeedback()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            FeedbackRepository.submitFeedback(UserCore.Instance.userId, _feedbackText.value!!, object : ApiCallback<StringApiResponse>
            {
                override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                    onSuccessCode.postValue(responseCode)
                }

                override fun onFailure(responseCode: ResponseCode) {
                    onErrorCode.postValue(responseCode)
                }
            })
        }
    }
}