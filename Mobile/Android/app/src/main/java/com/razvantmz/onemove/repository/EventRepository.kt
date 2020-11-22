package com.razvantmz.onemove.repository

import com.google.gson.GsonBuilder
import com.razvantmz.onemove.R
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.utils.toISOString
import com.razvantmz.onemove.extensions.process
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.models.event.Event
import com.razvantmz.onemove.models.postModels.EventPost
import com.razvantmz.onemove.models.postModels.FeeEntryPost
import com.razvantmz.onemove.models.postModels.ScheduleEntryPost
import com.razvantmz.onemove.services.ServiceBuilder
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object EventRepository {
    suspend fun addEvent(event: Event, coverPhoto: File, callback: ApiCallback<StringApiResponse>)
    {
        val schedulePostList = arrayListOf<ScheduleEntryPost>()
        for (schedule in event.scheduleList)
        {
            for (scheduleEntry in schedule.entrys)
            {
                if(scheduleEntry.description.isNullOrEmpty())
                {
                    continue
                }
                schedulePostList.add(ScheduleEntryPost(
                    scheduleEntry.id.toString(),
                    event.id.toString(),
                    scheduleEntry.startDate.time.toISOString(),
                    scheduleEntry.endDate.time.toISOString(),
                    scheduleEntry.description
                ))
            }
        }

        val feesPostList = arrayListOf<FeeEntryPost>()
        for (fee in event.feesList)
        {
            if(fee.title.isNullOrEmpty() && fee.price == -1f)
            {
                continue
            }
            feesPostList.add(FeeEntryPost(
                fee.id.toString(),
                event.id.toString(),
                fee.title,
                fee.price,
                fee.currency.currencyCode,
                fee.startDate.toISOString(),
                fee.endDate.toISOString(),
                fee.details
            ))
        }

        val eventPost = EventPost(
            event.id.toString(),
            event.title,
            event.startDate.toISOString(),
            event.endDate.toISOString(),
            event.isAllDay,
            schedulePostList,
            feesPostList,
            event.formLink,
            event.description,
            event.rules,
            ""
        )

        val serialisedEvent:String = Json.stringify(EventPost.serializer(), eventPost)

        val ser = GsonBuilder().create().toJson(event)

        var multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.SerializedEvent, serialisedEvent)
            val filePart = MultipartBody.Part.createFormData(
                RepositoryConstants.EventCoverPhoto,
                coverPhoto.name,
                RequestBody.create(MediaType.parse("image/*"), coverPhoto)
            )
            multipartBody.addPart(filePart)

        val request = multipartBody.build()
        try {
            ServiceBuilder.instance.addEvent(request).process(callback)
        }catch (e: Exception)
        {
            callback.onFailure(ResponseCode.AnErrorOccurred)
        }
    }

    suspend fun getEvents(callback: ApiCallback<ArrayList<EventPost>>)
    {
        val response = ServiceBuilder.instance.getEvents()
        if (response.isSuccessful)
        {
            callback.onSuccess(ResponseCode.fromInt(response.responseCode), response.data)
        }
        else
        {
            callback.onFailure(ResponseCode.fromInt(response.responseCode))
        }
    }

    suspend fun getEventById(eventId:UUID, userId:UUID, callback: ApiCallback<EventPost?>)
    {
        val params = hashMapOf<String, String>().apply{
            this[RepositoryConstants.UserId] = userId.toString()
            this[RepositoryConstants.EventId] = eventId.toString()
        }
        val response = ServiceBuilder.instance.getEventById(params)
        if (response.isSuccessful)
        {
            callback.onSuccess(ResponseCode.fromInt(response.responseCode), response.data)
        }
        else
        {
            callback.onFailure(ResponseCode.fromInt(response.responseCode))
        }
    }
}