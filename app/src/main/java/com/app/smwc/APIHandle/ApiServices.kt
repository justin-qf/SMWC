package com.app.ssn.data.api

import com.app.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Activity.CompanyInfo.CompanyData
import com.app.smwc.Activity.CompanyInfo.CompanyInfoResponse
import com.app.smwc.Activity.LoginActivity.LoginData
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.smwc.Activity.LoginActivity.VerifyOtpResponse
import com.app.smwc.Activity.OtpActivity.OtpData
import retrofit2.Response
import retrofit2.http.*

//BY JUSTIN[05-06-2023]
interface ApiServices {

    @Headers("Content-Type: application/json")
    @POST(Apis.LOGIN)
    suspend fun login(
        @Body body: LoginData,
    ): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.OTP)
    suspend fun setOtp(
        @Body body: OtpData,
    ): Response<SendOtpResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.REGISTER_OTP)
    suspend fun verifyOtp(
        @Body body: OtpData,
    ): Response<VerifyOtpResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.ADD_COMPANY)
    suspend fun addCompany(
        @Header("Authorization") token: String,
        @Body body: CompanyData,
    ): Response<CompanyInfoResponse>

}
