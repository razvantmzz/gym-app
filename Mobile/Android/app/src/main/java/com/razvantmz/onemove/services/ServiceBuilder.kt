package com.razvantmz.onemove.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    private const val PRODUCTION_URL = "http://192.168.1.2:8000/api/"
//    private const val PRODUCTION_URL = "http://razvantmz.pythonanywhere.com/api/"

//    private val AUTH = "Basic "+ Base64.encodeToString("belalkhan:123456".toByteArray(), Base64.NO_WRAP)


//    private val okHttpClient = OkHttpClient.Builder().callTimeout(5000, TimeUnit.SECONDS)
//        .addInterceptor { chain ->
//            val original = chain.request()
//            val requestBuilder = original.newBuilder()
////                .addHeader("Authorization", AUTH)
//                .addHeader("x-device-type", Build.DEVICE)
//                .addHeader("Accept-Language", Locale.getDefault().language)
//                .method(original.method(), original.body())
//
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }.build()
//    val retrofitBuilder: Retrofit.Builder by lazy {
//        Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
//    }
//
//    val instance: Api by lazy{
//        retrofitBuilder.build().create(Api::class.java)
////        val retrofit = Retrofit.Builder()
////            .baseUrl(URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .client(okHttpClient)
////            .build()
////
////        retrofit.create(Api::class.java)
//    }

    val retrofitBuilder: Retrofit.Builder by lazy {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(600, TimeUnit.SECONDS)
            .connectTimeout(600, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder().client(okHttpClient)
            .baseUrl(PRODUCTION_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val instance: Api by lazy {
        retrofitBuilder
            .build()
            .create(Api::class.java)
    }
}