package com.razvantmz.onemove.ui.eventDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.extensions.toCalendar
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.event.toEvent
import com.razvantmz.onemove.models.postModels.EventPost
import com.razvantmz.onemove.models.price.PriceEntry
import com.razvantmz.onemove.repository.EventRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.util.*
import kotlin.collections.ArrayList

class EventDetailsViewModel : BaseViewModel() {

    val coverImgUrl = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val startDate = MutableLiveData<Calendar>()
    val endDate = MutableLiveData<Calendar>()
    val details = MutableLiveData<String>()
    val rules = MutableLiveData<String>()
    val isAllDay = MutableLiveData<Boolean>()
    val scheduleEntry: MutableLiveData<ArrayList<Schedule>> = MutableLiveData(arrayListOf())
    val feesEntry: MutableLiveData<ArrayList<PriceEntry>> = MutableLiveData(arrayListOf())

    fun getEventByIdAsync(eventId:String?)
    {
        if(eventId.isNullOrEmpty())
        {
            return
        }
        val id = UUID.fromString(eventId)
        viewModelScope.launch(Dispatchers.IO) {
            isUpdating.postValue(true)
            EventRepository.getEventById(id, UserCore.Instance.userId, object : ApiCallback<EventPost?>
            {
                override fun onSuccess(responseCode: ResponseCode, data: EventPost?) {
                    isUpdating.postValue(false)
                    val event = data?.toEvent()
                    if(event == null)
                    {
                        onFailure(responseCode)
                        return
                    }

                    coverImgUrl.postValue(event.coverPhotoUrl)
                    title.postValue(event.title)
                    endDate.postValue(event.endDate.toCalendar())
                    isAllDay.postValue(event.isAllDay)
                    startDate.postValue(event.startDate.toCalendar())
                    val sortedScheduleList = event.scheduleList.sortedBy {
                        it.date
                    }
                    scheduleEntry.postValue(ArrayList(sortedScheduleList))
                    feesEntry.postValue(event.feesList)
                    details.postValue(event.description)
                    rules.postValue(event.rules)

                }

                override fun onFailure(responseCode: ResponseCode) {
                    isUpdating.postValue(false)
                }
            })
        }
    }

}