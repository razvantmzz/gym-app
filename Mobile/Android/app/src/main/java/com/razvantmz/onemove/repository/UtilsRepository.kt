package com.razvantmz.onemove.repository

import android.graphics.Bitmap
import com.razvantmz.onemove.core.managers.BitmapManager.Companion.toFile
import com.razvantmz.onemove.services.ServiceBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object UtilsRepository {

    suspend fun uploadFile(file: File) : String
    {
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )
        val response = ServiceBuilder.instance.uploadImage(filePart)
        return response.data
    }
}