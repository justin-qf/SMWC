package com.app.ssn.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

//BY JUSTIN[05-06-2023]
interface ApiServices {

//    @Headers("Content-Type: application/json")
//    @POST(Apis.login)
//    suspend fun login(
//        @Body body: LoginData,
//    ): Response<LoginResponse>
//
//    @GET(Apis.DASHBOARD)
//    suspend fun getHome(
//        @Query("graphType") graphType: String,
//        @Query("month") month: String,
//        @Query("paymentGraphType") paymentGraphType: String,
//        @Query("paymentYear") paymentYear: String,
//        @Query("user") user: String,
//        @Query("role") role: String,
//    ): Response<HomeModel>

//    @Headers("Content-Type: application/json")
//    @GET(Apis.GET_MENU)
//    suspend fun getMenuList(): Response<List<MenuResponseModel>>

}
