package com.razvantmz.onemove.ui.ranks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.CompetitionModel
import com.razvantmz.onemove.models.RouteModel
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.models.postModels.EventPost
import com.razvantmz.onemove.repository.EventRepository
import com.razvantmz.onemove.repository.RouteRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanksViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val selectedCompetition: MutableLiveData<CompetitionModel> by lazy {
        MutableLiveData<CompetitionModel>()
    }

    init {
        getEventListAsync()
    }

    val eventList: MutableLiveData<ArrayList<EventPost>>  = MutableLiveData()

    val text: LiveData<String> = _text

    private fun getEventListAsync()
    {
        viewModelScope.launch(Dispatchers.IO) {
            EventRepository.getEvents(object :
                ApiCallback<ArrayList<EventPost>> {
                override fun onSuccess(responseCode: ResponseCode, data: ArrayList<EventPost>) {
                    eventList.postValue(data)
                }

                override fun onFailure(responseCode: ResponseCode) {
                    onFailure(responseCode)
                }
                })
        }
    }
}