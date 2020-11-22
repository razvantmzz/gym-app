package com.razvantmz.onemove.repository

import android.os.Handler
import androidx.lifecycle.LiveData
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.utils.toDecimalString
import com.razvantmz.onemove.core.utils.toISOString
import com.razvantmz.onemove.core.utils.toInt
import com.razvantmz.onemove.extensions.process
import com.razvantmz.onemove.models.*
import com.razvantmz.onemove.services.ServiceBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.MediaType
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

object RouteRepository {

    private var job: CompletableJob? = null
    private var getRoutesJob:CompletableJob? = null

    fun getRouteAsync(userId:UUID, callback: ApiCallback<ArrayList<RouteModel>>) {
        getRoutesJob = Job()
        getRoutesJob?.let {theJob ->
            CoroutineScope(IO + theJob).launch {
                val params = hashMapOf<String, String>(RepositoryConstants.UserId to userId.toString())
                val data = ServiceBuilder.instance.getRoutesList(params)
                withContext(Dispatchers.Main)
                {
                    callback.onSuccess(ResponseCode.Success, data)
                    theJob.complete()
                }
            }
        }
    }

    fun getRouteDataById(routeId:UUID, userId:UUID, callback:ApiCallback<RouteModel>)
    {
        job = Job()
        job?.let { theJob->
            CoroutineScope(IO + theJob).launch {
                val params = hashMapOf<String, String>(
                    RepositoryConstants.UserId to userId.toString(),
                    RepositoryConstants.RouteId to routeId.toString()
                )
                ServiceBuilder.instance.getRouteDataById(params).enqueue(object :
                    Callback<ApiResponse<RouteModel>> {
                    override fun onFailure(call: Call<ApiResponse<RouteModel>>, t: Throwable) {
                        callback.onFailure(ResponseCode.ServerError)
                    }

                    override fun onResponse(call: Call<ApiResponse<RouteModel>>, response: Response<ApiResponse<RouteModel>>) {
                        val data = response.body()
                        data?:return
                        if(!data.isSuccessful)
                        {
                            callback.onFailure(ResponseCode.fromInt(data.responseCode))
                            theJob.complete()
                        } else {
                            callback.onSuccess(ResponseCode.Success, data.data)
                            theJob.complete()
                        }
                    }
                })
            }
        }
    }

    fun setIsRouteFavorite(routeId: UUID, userId: UUID, isFavorite:Boolean, callback: ApiCallback<StringApiResponse>)
    {
        job = Job()
        job?.let {
            CoroutineScope(IO + it).launch {
                val params = hashMapOf<String, String>(

                    RepositoryConstants.UserId to userId.toString(),
                    RepositoryConstants.RouteId to routeId.toString(),
                    RepositoryConstants.IsFavorite to isFavorite.toDecimalString()
                )
                ServiceBuilder.instance.setRouteFavoriteForUser(params).enqueue(object : Callback<StringApiResponse>
                {
                    override fun onFailure(call: Call<StringApiResponse>, t: Throwable) {
                        callback.onFailure(ResponseCode.ServerError)
                    }

                    override fun onResponse(call: Call<StringApiResponse>, response: Response<StringApiResponse>) {
                        val data = response.body()
                        if(data == null)
                        {
                            callback.onFailure(ResponseCode.ServerError)
                            return
                        }
                        if(!data.isSuccessful)
                        {
                            callback.onFailure(ResponseCode.fromInt(data.responseCode))
                            return
                        }

                        callback.onSuccess(ResponseCode.fromInt(data.responseCode), data)
                    }
                })
            }
        }
    }

    suspend fun markRouteAsSent(routeId: UUID, userId: UUID, date: Date, grade: Grade, attempt: Attempt, callback: ApiCallback<StringApiResponse>)
    {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.UserId, userId.toString())
            .addFormDataPart(RepositoryConstants.RouteId, routeId.toString())
            .addFormDataPart(RepositoryConstants.Date, date.toISOString())
            .addFormDataPart(RepositoryConstants.Grade, grade.value.toString())
            .addFormDataPart(RepositoryConstants.Attempt, attempt.value.toString())
            .build()
        try {
            ServiceBuilder.instance.markRouteAsSent(requestBody).process(callback)
        }catch (e:Exception)
        {
            callback.onFailure(ResponseCode.AnErrorOccurred)
        }
    }
}