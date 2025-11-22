package com.example.campusrent.data.api

import com.example.campusrent.data.model.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("signup.php")
    suspend fun signup(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Response<LoginResponse>

    @GET("get_items.php")
    suspend fun getItems(): Response<List<Item>>

    @Multipart
    @POST("add_item.php")
    suspend fun addItem(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse>
}

data class ApiResponse(
    val status: String,
    val message: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val user_id: Int?
)
