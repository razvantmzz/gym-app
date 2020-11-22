package com.razvantmz.onemove.repository

import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.services.ServiceBuilder
import okhttp3.MultipartBody
import java.util.*

object FeedbackRepository {

    suspend fun submitFeedback(userId: UUID, message:String, callback: ApiCallback<StringApiResponse>)
    {
        var request = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.User, userId.toString())
            .addFormDataPart(RepositoryConstants.Message, message)
            .build()

        var response = ServiceBuilder.instance.submitFeedback(request)
        if (response.isSuccessful)
        {
            callback.onSuccess(ResponseCode.fromInt(response.responseCode), response)
        }
        else
        {
            callback.onFailure(ResponseCode.fromInt(response.responseCode))
        }
    }
}