package com.razvantmz.onemove.ui.addProblem

import android.content.ContextWrapper
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.razvantmz.onemove.core.managers.BitmapManager.Companion.toFile
import com.razvantmz.onemove.core.utils.toISOString
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.models.*
import com.razvantmz.onemove.core.responseHandlers.*
import com.razvantmz.onemove.core.utils.InitLiveEvent
import com.razvantmz.onemove.repository.AddProblemRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import com.razvantmz.onemove.zzzmockdata.SampleData
import kotlinx.coroutines.*
import java.util.*

class AddProblemViewModel() : BaseViewModel() {

    private var routeModel:RouteModel? = null
    private val _routeId: MutableLiveData<UUID> = MutableLiveData()
    var name: InitLiveEvent<String> = InitLiveEvent()
    var index: InitLiveEvent<Int> = InitLiveEvent()
    var routeType =  InitLiveEvent<RouteType>()
    var setter = MutableLiveData<Setter>()
    var location = InitLiveEvent<RouteLocation>()
    var color = InitLiveEvent<Int>()
    var grade = InitLiveEvent<Grade>()
    var date = InitLiveEvent<Date>()

    private var fullImage = InitLiveEvent<Bitmap>()
    private var previewImage = InitLiveEvent<Bitmap>()

    val addRouteData : LiveData<AddRouteData> = Transformations.switchMap(_routeId){
        AddProblemRepository.getData()
    }

    val setterList : LiveData<List<Setter>> = Transformations.switchMap(addRouteData){data->
        MutableLiveData(data.setters)
    }

    val gradeList : LiveData<List<Grade>> = Transformations.switchMap(addRouteData){data->
        MutableLiveData(SampleData.getGrades())
    }

    val holdsColorList : LiveData<List<Int>> = Transformations.switchMap(addRouteData){data->
        MutableLiveData(SampleData.getColors())
    }

    val locationList : LiveData<List<RouteLocation>> = Transformations.switchMap(addRouteData){data->
        if(data.locations.isEmpty())
        {
            return@switchMap MutableLiveData(data.locations)
        }
        MutableLiveData(data.locations)
    }

    fun saveData(context: ContextWrapper)
    {
        addRouteAsync(context)
    }

    private fun addRouteAsync(context: ContextWrapper) {
        if (!validateInputs()) {
            return
        }
        isUpdating.postValue(true)
        viewModelScope.launch {
            Dispatchers.IO
            async {
                val fullImageName = getRouteId().value.toString() + "_problem.png"
                val fullImageFile = getFullImage().value?.toFile(context, fullImageName)

                val previewImageName = getRouteId().value.toString() + "_preview.png"
                val previewFile = getPreviewImage().value?.toFile(context, previewImageName)
                val list = AddProblemRepository.saveProblemImages(fullImageFile, previewFile)
                println("DEBUG: $list")

                val fullImgUrl = if(list.isEmpty())
                {
                    routeModel?.imageUrl
                } else
                {
                    list[0]
                }

                val previewImgUrl = if(list.isEmpty())
                {
                    routeModel?.previewImageUrl
                } else
                {
                    list[1]
                }

                val data = RouteDataPost(
                    getRouteId().value!!,
                    name.value!!,
                    getRouteType().value!!.value,
                    setter.value!!.id,
                    location.value!!.id,
                    index.value!!,
                    getDate().value!!.toISOString(),
                    color.value!!,
                    grade.value!!.grade,
                    fullImgUrl,
                    previewImgUrl
                )

                if(routeModel == null)
                {
                    AddProblemRepository.saveProblem(data,  object : ApiCallback<StringApiResponse> {
                        override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                            isUpdating.postValue(false)
                            onSuccessCode.setValue(responseCode)
                        }

                        override fun onFailure(responseCode: ResponseCode) {
                            isUpdating.postValue(false)
                            onErrorCode.setValue(responseCode)
                        }
                    })
                }
                else
                {
                    AddProblemRepository.editProblem(data,  object : ApiCallback<StringApiResponse> {
                        override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                            isUpdating.postValue(false)
                            onSuccessCode.setValue(responseCode)
                        }

                        override fun onFailure(responseCode: ResponseCode) {
                            isUpdating.postValue(false)
                            onErrorCode.setValue(responseCode)
                        }
                    })
                }
            }
        }
    }

    private fun validateInputs() : Boolean {
        if(getFullImage().value == null && routeModel == null)
        {
            onErrorCode.setValue(ResponseCode.PhotoMissing)
            return false
        }

        if (name.value.isNullOrEmpty()) {
            onErrorCode.setValue(ResponseCode.InvalidName)
            return false
        }

        if (index.value == -1) {
            onErrorCode.setValue(ResponseCode.IndexNotSet)
            return false
        }

        if (getDate().value == null) {
            onErrorCode.setValue(ResponseCode.InvalidDate)
            return false
        }
        return true
    }

    fun setRouteModel(route: RouteModel?)
    {
        if(route == null)
        {
            _routeId.value = UUID.randomUUID()
            date.value = Calendar.getInstance().time
        }
        else
        {
            routeModel = route
            _routeId.value = route.id
            name.value = route.name
            index.value = route.index
            routeType.value  =  RouteType.fromInt(route.type)
            location.value  = locationList.value?.first { loc -> loc.name == route.location }
            color.value  = route.holdsColor
            grade.value  = SampleData.getGrades().first { grade -> grade.grade == route.grade }
            date.value = route.date
        }
    }

    fun getRouteId() : LiveData<UUID>
    {
        return _routeId
    }

    fun getRouteType() : LiveData<RouteType>
    {
        return routeType
    }

    fun getDate() : LiveData<Date>
    {
        return date
    }

    fun getFullImage() : LiveData<Bitmap>
    {
        return fullImage
    }

    fun setFullImage(image:Bitmap?)
    {
        if(image == null)
        {
            return
        }
        fullImage.value = image
    }

    fun getPreviewImage() : LiveData<Bitmap>
    {
        return previewImage
    }

    fun setPreviewImage(image:Bitmap?)
    {
        if(image == null)
        {
            return
        }
        previewImage.value = image
    }
}