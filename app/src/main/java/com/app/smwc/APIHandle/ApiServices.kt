package com.app.smwc.APIHandle

import com.app.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.app.smwc.Activity.CompanyInfo.CompanyData
import com.app.smwc.Activity.CompanyInfo.CompanyInfoResponse
import com.app.smwc.Activity.EditProfile.EditProfileResponse
import com.app.smwc.Activity.EditProfile.UpdateProfileData
import com.app.smwc.Activity.LoginActivity.LoginData
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.smwc.Activity.LoginActivity.VerifyOtpResponse
import com.app.smwc.Activity.OtpActivity.OtpData
import com.app.smwc.Activity.ScannerActivity.ScannerResponse
import com.app.smwc.Activity.ScannerActivity.ScannerResponseData
import com.app.smwc.fragments.History.HistoryResponse
import com.app.smwc.fragments.Home.HomeResponse
import com.app.smwc.fragments.Notification.NotificationResponse
import com.app.smwc.fragments.Profile.ProfileFragment.ProfileResponse
import okhttp3.MultipartBody
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


    @Headers("Content-Type: application/json")
    @POST(Apis.HOME)
    suspend fun home(
        @Header("Authorization") token: String,
    ): Response<HomeResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.CREATE_TOKEN)
    suspend fun CREATE_TOKEN(
        @Header("Authorization") token: String,
        @Body body: ScannerResponseData,
    ): Response<ScannerResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.PROFILE)
    suspend fun profile(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.HISTORY)
    suspend fun history(
        @Header("Authorization") token: String,
    ): Response<HistoryResponse>

    @Multipart
    @Headers("Content-Type: application/json")
    @POST(Apis.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("body") body: UpdateProfileData,
    ): Response<EditProfileResponse>

    @Headers("Content-Type: application/json")
    @POST(Apis.NOTIFICATION)
    suspend fun notification(
        @Header("Authorization") token: String,
    ): Response<NotificationResponse>

}
