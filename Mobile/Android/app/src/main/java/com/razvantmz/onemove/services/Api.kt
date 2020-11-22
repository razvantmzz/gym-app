package com.razvantmz.onemove.services

import com.razvantmz.onemove.models.*
import com.razvantmz.onemove.models.postModels.EventPost
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @GET("route")
    suspend fun getRoutesList(@QueryMap filter: HashMap<String, String>): ArrayList<RouteModel>

    @GET("getRouteById")
    fun getRouteDataById(@QueryMap filter: HashMap<String, String>): Call<ApiResponse<RouteModel>>

    @GET("setRouteFavoriteForUser")
    fun setRouteFavoriteForUser(@QueryMap filter: HashMap<String, String>): Call<StringApiResponse>

    @GET("getAddRouteData")
    suspend fun getAddRouteData(): AddRouteData

    @Multipart
    @POST("uploadImage")
    suspend fun uploadImage(@Part file:MultipartBody.Part): StringApiResponse

    @POST("addRoute")
     fun addRoute(@Body routeData: RouteDataPost): Call<StringApiResponse>

    @POST("route/editRoute")
     fun editRoute(@Body routeData: RouteDataPost): Call<StringApiResponse>

    @POST("account/editUserProfile")
    suspend fun editUserProfile(@Body body: RequestBody): StringApiResponse

    @POST("account/registerUser")
    suspend fun registerAccount(@Body body: RequestBody): StringApiResponse

    @POST("account/signInUser")
    suspend fun signIn(@Body body: RequestBody): ApiResponse<UserDataModel?>

    @POST("feedback/submitFeedback")
    suspend fun submitFeedback(@Body body: RequestBody): StringApiResponse

    @POST("route/markRouteAsSent")
    suspend fun markRouteAsSent(@Body body: RequestBody): StringApiResponse

    @POST("event/addEvent")
    suspend fun addEvent(@Body body: RequestBody): StringApiResponse

    @GET("event/getEvents")
    suspend fun getEvents(): ApiResponse<ArrayList<EventPost>>

    @GET("event/getEventById")
    suspend fun getEventById(@QueryMap filter: HashMap<String, String>): ApiResponse<EventPost?>

//    @GET("destination/{id}")
//    fun getDestination(@Path("id") id: Int): Call<Destination>
//
//    @POST("destination")
//    fun addDestination(@Body newDestination: Destination): Call<Destination>
//
//    @FormUrlEncoded
//    @PUT("destination/{id}")
//    fun updateDestination(
//        @Path("id") id: Int,
//        @Field("city") city: String,
//        @Field("description") desc: String,
//        @Field("country") country: String
//    ): Call<Destination>
//
//    @DELETE("destination/{id}")
//    fun deleteDestination(@Path("id") id: Int): Call<Unit>
}