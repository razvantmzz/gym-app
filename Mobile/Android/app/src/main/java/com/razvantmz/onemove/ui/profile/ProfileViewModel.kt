package com.razvantmz.onemove.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.razvantmz.onemove.models.UserCore
import com.razvantmz.onemove.ui.base.BaseViewModel

class ProfileViewModel : BaseViewModel() {

    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _category = MutableLiveData<Int>()
    private val _gender = MutableLiveData<Int>()
    private val _profileImageUrl = MutableLiveData<String>()
    private val _coverPhotoUrl = MutableLiveData<String>()

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

    //region private methods

    fun initUserData()
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

    //endregion
}