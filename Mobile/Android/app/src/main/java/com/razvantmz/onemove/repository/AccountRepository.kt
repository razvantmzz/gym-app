package com.razvantmz.onemove.repository

import android.graphics.Bitmap
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.core.responseHandlers.ResponseCode
import com.razvantmz.onemove.extensions.process
import com.razvantmz.onemove.models.RegisterAccount
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.models.UserDataModel
import com.razvantmz.onemove.services.ServiceBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object AccountRepository {
    suspend fun registerAccount(account:RegisterAccount, callback: ApiCallback<StringApiResponse>)
    {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.FirstName, account.firstName)
            .addFormDataPart(RepositoryConstants.LastName, account.lastName)
            .addFormDataPart(RepositoryConstants.Email, account.email)
            .addFormDataPart(RepositoryConstants.RegisterPassword, account.password)
            .addFormDataPart(RepositoryConstants.ConfirmPassword, account.confirmPassword)
            .addFormDataPart(RepositoryConstants.SubscriptionNumber, account.subscriptionNumber)
            .build()
        try {
            ServiceBuilder.instance.registerAccount(requestBody).process(callback)
        }catch (e: Exception)
        {
            callback.onFailure(ResponseCode.AnErrorOccurred)
        }
    }

    suspend fun signIn(email: String, password: String, callback: ApiCallback<UserDataModel?>)
    {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.Email, email)
            .addFormDataPart(RepositoryConstants.Password, password)
            .build()

        try {
            ServiceBuilder.instance.signIn(requestBody).process(callback)
        }catch (e: Exception)
        {
            callback.onFailure(ResponseCode.AnErrorOccurred)
        }
    }

    suspend fun editUserProfile(account:UserDataModel, file: File?, callback: ApiCallback<StringApiResponse>)
    {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(RepositoryConstants.UserId, account.id.toString())
            .addFormDataPart(RepositoryConstants.Email, account.email)
            .addFormDataPart(RepositoryConstants.UserType, account.type.toString())
//            .addFormDataPart(RepositoryConstants.ImageUrl, account.imageUrl)
            .addFormDataPart(RepositoryConstants.CoverPhoto, account.coverPhotoUrl)
            .addFormDataPart(RepositoryConstants.FirstName, account.firstName)
            .addFormDataPart(RepositoryConstants.LastName, account.lastName)
            .addFormDataPart(RepositoryConstants.SubscriptionNumber, account.subscriptionNumber)
            .addFormDataPart(RepositoryConstants.Gender, account.gender.toString())
            .addFormDataPart(RepositoryConstants.Category, account.category.toString())

        if(file != null)
        {
            val filePart = MultipartBody.Part.createFormData(
                RepositoryConstants.ProfilePicture,
                file.name,
                RequestBody.create(MediaType.parse("image/*"), file)

            )
            multipartBody.addPart(filePart)
        }

        try {
            ServiceBuilder.instance.editUserProfile(multipartBody.build()).process(callback)
        }catch (e: Exception)
        {
            callback.onFailure(ResponseCode.AnErrorOccurred)
        }
    }
}