package com.razvantmz.onemove.ui.profileEdit

import android.content.ContextWrapper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.repository.AccountRepository
import com.razvantmz.onemove.ui.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class ProfileEditViewModel: BaseViewModel() {
    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _category = MutableLiveData<Int>()
    private val _gender = MutableLiveData<Int>()
    private val _profileImageUrl = MutableLiveData<String>()
    private val _coverPhotoUrl = MutableLiveData<String>()

    private val _profileImageFile = MutableLiveData<File>()


    init {
        initUserData()
    }

    val firstName: LiveData<String> = _firstName
    val lastName: LiveData<String> = _lastName
    val email: LiveData<String> = _email
    val category: LiveData<Int> = _category
    val gender: LiveData<Int> = _gender
    val profileImageUrl: LiveData<String> = _profileImageUrl
    val coverPhotoUrl: LiveData<String> = _coverPhotoUrl
    val profileImageFile:LiveData<File> = _profileImageFile


    fun setCategory(category: Int)
    {
        _category.value = category!!
    }

    fun setGender(gender: Int)
    {
        _gender.value = gender!!
    }

    fun setProfileImageFile(file:File)
    {
        _profileImageFile.value = file
    }

    fun setFirstName(value:String)
    {
        _firstName.value = value
    }

    fun setLastName(value:String)
    {
        _lastName.value = value
    }

    fun setEmail(value:String)
    {
        _email.value = value
    }

    fun saveUserProfile(context: ContextWrapper)
    {
        isUpdating.value = true
        val user = UserCore.Instance.user
        user?:return

        viewModelScope.launch {
            async {

                user.firstName = firstName.value!!
                user.lastName = lastName.value!!
                user.email = email.value!!
                user.category = category.value!!
                user.gender = gender.value!!
                AccountRepository.editUserProfile(user, profileImageFile.value, object : ApiCallback<StringApiResponse>{
                    override fun onSuccess(responseCode: ResponseCode, data: StringApiResponse) {
                        isUpdating.postValue(false)
                        UserCore.Instance.user?.firstName = firstName.value!!
                        UserCore.Instance.user?.lastName = lastName.value!!
                        UserCore.Instance.user?.email = email.value!!
                        UserCore.Instance.user?.category = category.value!!
                        UserCore.Instance.user?.gender = gender.value!!

                        if(!data.data.isNullOrEmpty())
                        {
                            UserCore.Instance.user?.imageUrl = data.data
                            _profileImageUrl.value = data.data
                        }
                        onSuccessCode.setValue(responseCode)
                    }

                    override fun onFailure(responseCode: ResponseCode) {
                        onErrorCode.setValue(responseCode)
                    }
                })
            }
        }
    }

    private fun initUserData()
    {
        val user = UserCore.Instance.user
        user?:return

        _firstName.value = user.firstName
        _lastName.value = user.lastName
        _email.value = user.email
        _category.value = user.category
        _gender.value = user.gender
        _profileImageUrl.value = user.imageUrl
        _coverPhotoUrl.value = user.coverPhotoUrl
    }
}