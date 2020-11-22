package com.razvantmz.onemove.extensions

import androidx.annotation.Nullable
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.ApiResponse
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.services.ServiceBuilder

fun StringApiResponse.process(callback: ApiCallback<StringApiResponse>): Boolean
{
    if(isSuccessful)
    {
        callback.onSuccess(ResponseCode.fromInt(responseCode), this)
    }
    else
    {
        callback.onFailure(ResponseCode.fromInt(responseCode))
    }
    return isSuccessful
}
fun <T> ApiResponse<T>.process(callback: ApiCallback<T>): Boolean
{
    data?: callback.onFailure(ResponseCode.fromInt(responseCode))

    if(isSuccessful)
    {
        callback.onSuccess(ResponseCode.fromInt(responseCode), data)
    }
    else
    {
        callback.onFailure(ResponseCode.fromInt(responseCode))
    }
    return isSuccessful
}
