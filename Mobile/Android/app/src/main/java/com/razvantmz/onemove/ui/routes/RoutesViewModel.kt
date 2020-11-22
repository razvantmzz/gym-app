package com.razvantmz.onemove.ui.routes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.*
import com.razvantmz.onemove.repository.RouteRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import com.razvantmz.onemove.ui.routeDetails.RouteDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class RoutesViewModel : BaseViewModel() {

    interface Interaction
    {
        fun refreshCurrentView(position:Int)
        fun itemChanged(route: RouteModel, position:Int)

    }
    var interactionListener: Interaction? = null

    private val searchedString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var routeList: ArrayList<RouteModel> = arrayListOf()
    val filteredList: MutableLiveData<List<RouteModel>>  = MutableLiveData()

    init {
        routeList = arrayListOf<RouteModel>()
        getRouteListAsync()
    }

    fun getFilteredRouteList() : LiveData<List<RouteModel>>
    {
        return filteredList
    }

    fun markRouteAsSent(routeId:UUID, date:Date?, grade: Grade?, attempt: Attempt?, apiCallback: ApiCallback<StringApiResponse>)
    {
        if(date == null || grade == null || attempt == null)
        {
            apiCallback.onSuccess(ResponseCode.AnErrorOccurred, StringApiResponse(22, true, "dasd"))
            onErrorCode.setValue(ResponseCode.AnErrorOccurred)
            return
        }
        viewModelScope.launch{
            RouteRepository.markRouteAsSent(routeId, UserCore.Instance.userId, date, grade, attempt, apiCallback)
            val routeFromCore = UserCore.Instance.routeList.value?.first { routeModel ->
                routeModel.id == routeId
            }
            routeFromCore?.triesCount = attempt.value
            val pos =routeList.indexOfFirst { routeModel -> routeModel.id == routeId }
            routeList[pos].triesCount = attempt.value
            search(searchedString.value)
        }
    }

    fun setIsFavoriteStatus(route:RouteModel, isFavorite:Boolean)
    {
        RouteRepository.setIsRouteFavorite(route.id, UserCore.Instance.userId, isFavorite, object : ApiCallback<StringApiResponse>{
            override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                onSuccessCode.setValue(responseCode)
                val routeFromCore = UserCore.Instance.routeList.value?.first { routeModel ->
                    routeModel.id == route.id
                }
                routeFromCore?:return
                routeFromCore.isFavorite = isFavorite
                routeFromCore.likesCount = if (isFavorite)
                    routeFromCore.likesCount + 1
                else  routeFromCore.likesCount - 1
                val pos =routeList.indexOfFirst { routeModel -> routeModel.id == route.id }
                routeList[pos].likesCount = routeFromCore.likesCount
                search(searchedString.value)
            }

            override fun onFailure(responseCode: ResponseCode) {
                onErrorCode.setValue(responseCode)
            }
        })
    }

    fun search(text:String?)
    {
        searchedString.value = text
        if(text.isNullOrEmpty())
        {
            filteredList.value = routeList
            return
        }
        val list = routeList!!.filter<RouteModel> { data ->
                    data.name.contains(text, true) ||
                    data.setter.name.contains(text, true) ||
                    data.grade.contains(text, true)}
        filteredList.value = list
    }

    fun getRouteListAsync()
    {
        RouteRepository.getRouteAsync(UserCore.Instance.userId, object : ApiCallback<ArrayList<RouteModel>>{
            override fun onSuccess(responseCode: ResponseCode, data: ArrayList<RouteModel>) {
                routeList = data
                UserCore.Instance.routeList.value = data
                search(searchedString.value)
            }

            override fun onFailure(responseCode: ResponseCode) {
                onFailure(responseCode)
            }
        })
    }
}