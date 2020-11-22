package com.razvantmz.onemove.core.responseHandlers

 interface ApiCallback<T>
{
    fun onSuccess(responseCode: ResponseCode, data:T)

    fun onFailure(responseCode: ResponseCode)
}
