package com.razvantmz.onemove.ui.routeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.*
import com.razvantmz.onemove.repository.RouteRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class RouteDetailsViewModel : BaseViewModel() {

    interface Interaction
    {
        fun refreshCurrentView(position:Int)
        fun setCurrentItem(position:Int)
    }

    private val _routeDataPosition:MutableLiveData<Int> = MutableLiveData()
    private val _routeList:MutableLiveData<List<RouteModel>> = MutableLiveData(emptyList())

    var interactionListener: Interaction? = null

    fun getRouteData() : LiveData<Int>
    {
        return  _routeDataPosition
    }

    fun getRouteListData() : LiveData<List<RouteModel>>
    {
        return _routeList
    }

    fun setRoutePositionById(routeId: UUID)
    {
        interactionListener?.setCurrentItem( getRouteListData().value?.indexOfFirst { routeModel -> routeModel.id == routeId}!!)
        _routeDataPosition.value = getRouteListData().value?.indexOfFirst { routeModel -> routeModel.id == routeId}
    }

    fun setRouteDataByPosition(position:Int)
    {
        _routeDataPosition.value = position
    }

    fun setRouteList(routeType:Int)
    {
        val routeList = UserCore.Instance.routeList.value?.filter {
            it.type == routeType
        }
        _routeList.value = routeList
    }

    fun setIsFavoriteStatus(route:RouteModel, isFavorite:Boolean)
    {
        RouteRepository.setIsRouteFavorite(route.id, UserCore.Instance.userId, isFavorite, object : ApiCallback<StringApiResponse>{
            override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                onSuccessCode.setValue(responseCode)
                val route = UserCore.Instance.routeList.value?.first { routeModel ->
                    routeModel.id == route.id
                }
                route?:return
                route.isFavorite = isFavorite
                route.likesCount = if (isFavorite)
                    route.likesCount + 1
                else  route.likesCount - 1
                val pos = _routeDataPosition.value!!
                _routeList.value!![pos].likesCount = route.likesCount
                interactionListener?.refreshCurrentView(pos)
            }

            override fun onFailure(responseCode: ResponseCode) {
                onErrorCode.setValue(responseCode)
            }
        })
    }

    fun markRouteAsSent(routeId:UUID, date:Date?, grade: Grade?, attempt: Attempt?, apiCallback: ApiCallback<StringApiResponse>)
    {
        if(date == null || grade == null || attempt == null)
        {
            apiCallback.onSuccess(ResponseCode.AnErrorOccurred, StringApiResponse(22, true, "dasd"))
            onErrorCode.setValue(ResponseCode.AnErrorOccurred)
            return
        }

        val intermediaryCallback = object :ApiCallback<StringApiResponse>
        {
            override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                apiCallback.onSuccess(responseCode, data)
                val pos = _routeDataPosition.value!!
                _routeList.value!![pos].triesCount = attempt.value
                UserCore.Instance.routeList.value = _routeList.value
                interactionListener?.refreshCurrentView(pos)
            }

            override fun onFailure(responseCode: ResponseCode) {
                apiCallback.onFailure(responseCode)
            }
        }

        viewModelScope.launch{
            RouteRepository.markRouteAsSent(routeId, UserCore.Instance.userId, date, grade, attempt, intermediaryCallback)
        }
    }
}