package com.razvantmz.onemove.repository

import androidx.lifecycle.LiveData
import com.razvantmz.onemove.models.AddRouteData
import com.razvantmz.onemove.models.RouteDataPost
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.services.ServiceBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object AddProblemRepository  {
    var job: CompletableJob? = null
    var imageJob: CompletableJob? = null

    fun getData() : LiveData<AddRouteData>
    {
        job = Job()
        return object : LiveData<AddRouteData>()
        {
            override fun onActive() {
                super.onActive()
                job?.let { theJob->
                    CoroutineScope(IO + theJob).launch {
                        val data = ServiceBuilder.instance.getAddRouteData()
                        withContext(Main)
                        {
                            value = data
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    suspend fun saveProblemImages(fullImgFile:File?, previewImgFile:File?) : List<String>
    {
        if(fullImgFile == null || previewImgFile == null)
        {
            return emptyList()
        }
        val fullImgUrl = UtilsRepository.uploadFile(fullImgFile)
        val previewImgUrl = UtilsRepository.uploadFile(previewImgFile)
        val list = arrayListOf<String>()
        list.add(fullImgUrl)
        list.add(previewImgUrl)
        return list
    }

    suspend fun saveProblem(data:RouteDataPost, callback: ApiCallback<StringApiResponse>)
    {
       ServiceBuilder.instance.addRoute(data).enqueue(object : Callback<StringApiResponse>
       {
           override fun onFailure(call: Call<StringApiResponse>, t: Throwable) {
               callback.onFailure(ResponseCode.ServerError)
           }

           override fun onResponse(call: Call<StringApiResponse>, response: Response<StringApiResponse>) {
               if(response.body() == null)
               {
                   callback.onFailure(ResponseCode.ServerError)
                   return
               }
               val apiResponse = response.body()
               if ( !apiResponse!!.isSuccessful)
               {
                   callback.onFailure(ResponseCode.fromInt(apiResponse.responseCode))
               }
               println("DEBUG: Save problem endpoint returned ${apiResponse.isSuccessful}")
               //TODO:take response code from server
//               callback.onSuccess(ResponseCode.fromInt(apiResponse.responseCode), apiResponse)
               callback.onSuccess(ResponseCode.ProblemAdded, apiResponse)
           }
       })
    }

    suspend fun editProblem(data:RouteDataPost, callback: ApiCallback<StringApiResponse>)
    {
        ServiceBuilder.instance.editRoute(data).enqueue(object : Callback<StringApiResponse>
        {
            override fun onFailure(call: Call<StringApiResponse>, t: Throwable) {
                callback.onFailure(ResponseCode.ServerError)
            }

            override fun onResponse(call: Call<StringApiResponse>, response: Response<StringApiResponse>) {
                if(response.body() == null)
                {
                    callback.onFailure(ResponseCode.ServerError)
                    return
                }
                val apiResponse = response.body()
                if ( !apiResponse!!.isSuccessful)
                {
                    callback.onFailure(ResponseCode.fromInt(apiResponse.responseCode))
                }
                println("DEBUG: edit problem endpoint returned ${apiResponse.isSuccessful}")
               callback.onSuccess(ResponseCode.fromInt(apiResponse.responseCode), apiResponse)
            }
        })
    }

    fun cancelJobs()
    {
        job?.cancel()
    }
}