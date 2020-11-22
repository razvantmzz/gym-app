package com.razvantmz.onemove.ui.addEvent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.addPrice.PriceEntryListAdapter
import com.razvantmz.onemove.adapters.addSchedule.ScheduleListAdapter
import com.razvantmz.onemove.core.extensions.addMinutes
import com.razvantmz.onemove.core.helpers.showErrorSnackbar
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.core.utils.InitLiveEvent
import com.razvantmz.onemove.core.validations.IValidationProvider
import com.razvantmz.onemove.core.validations.commands.EmailCommand
import com.razvantmz.onemove.core.validations.commands.MinMaxLengthCommand
import com.razvantmz.onemove.models.Grade
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.models.event.Event
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.price.PriceEntry
import com.razvantmz.onemove.repository.EventRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import com.razvantmz.onemove.zzzmockdata.SampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddEventViewModel : BaseViewModel() {

    private val coverPhoto: MutableLiveData<File> = MutableLiveData()

    var eventId:UUID = UUID.randomUUID()
    val startDate: MutableLiveData<Calendar> = MutableLiveData()
    val endDate: MutableLiveData<Calendar> = MutableLiveData()
    val isAllDay: MutableLiveData<Boolean> = MutableLiveData()

    val schedule: MutableLiveData<ArrayList<Schedule>> = MutableLiveData()
    val feesList: MutableLiveData<ArrayList<PriceEntry>> = MutableLiveData()

    val eventTitle : InitLiveEvent<String> = InitLiveEvent()
    val formLink : InitLiveEvent<String> = InitLiveEvent()
    val description : InitLiveEvent<String> = InitLiveEvent()
    val rules : InitLiveEvent<String> = InitLiveEvent()

    init {
        startDate.value = Calendar.getInstance()
        isAllDay.value = false
        endDate.value = Calendar.getInstance().addMinutes(30)
        schedule.value = arrayListOf()
        feesList.value = arrayListOf()
    }

    fun setCoverPhoto(file:File?)
    {
        coverPhoto.value = file
    }

    fun getCoverPhoto() : LiveData<File>
    {
        return coverPhoto
    }

    override fun onValidationFieldsAdded(validationFields: List<IValidationProvider>) {
        validationFields[0].validationCommands.add(MinMaxLengthCommand(R.string.addTitle, 3, 50))
        validationFields[1].validationCommands.add(MinMaxLengthCommand(R.string.formLink, 3, 200))
//        validationFields[2].validationCommands.add(MinMaxLengthCommand(R.string.addDescription, 3, 2000))
//        validationFields[3].validationCommands.add(MinMaxLengthCommand(R.string.rules, 3, 2000))
    }

    override fun areInputsValid(displaySnackbar: Boolean): Boolean {
        val areCommandsValid =  super.areInputsValid(displaySnackbar)

        if(coverPhoto.value == null)
        {
            //todo add validation string
            return false
        }

        return areCommandsValid
    }

    fun addEventAsync()
    {
        isUpdating.value = true
        viewModelScope.launch(Dispatchers.IO)
        {
            val event = Event(
                eventId,
                eventTitle.value!!,
                startDate.value!!.time,
                endDate.value!!.time,
                isAllDay.value!!,
                schedule.value!!,
                feesList.value!!,
                formLink.value!!,
                description.value!!,
                rules.value!!,
                ""
            )

            EventRepository.addEvent(event, coverPhoto.value!!, object : ApiCallback<StringApiResponse>
            {
                override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                    isUpdating.postValue(false)

                }

                override fun onFailure(responseCode: ResponseCode) {
                    isUpdating.postValue(false)
                }
            })
        }
    }
}