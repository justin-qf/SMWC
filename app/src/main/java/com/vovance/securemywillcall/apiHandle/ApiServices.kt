package com.vovance.securemywillcall.apiHandle

import com.vovance.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.vovance.securemywillcall.activity.CompanyInfo.CompanyData
import com.vovance.securemywillcall.activity.CompanyInfo.CompanyInfoResponse
import com.vovance.securemywillcall.activity.EditProfile.EditProfileResponse
import com.vovance.securemywillcall.activity.EditProfile.UpdateProfileData
import com.vovance.securemywillcall.activity.LoginActivity.LoginData
import com.vovance.securemywillcall.activity.LoginActivity.LoginResponse
import com.vovance.securemywillcall.activity.LoginActivity.VerifyOtpResponse
import com.vovance.securemywillcall.activity.OtpActivity.OtpData
import com.vovance.securemywillcall.activity.ScannerActivity.ScannerResponse
import com.vovance.securemywillcall.activity.ScannerActivity.ScannerResponseData
import com.vovance.securemywillcall.fragments.History.HistoryResponse
import com.vovance.securemywillcall.fragments.Home.HomeResponse
import com.vovance.securemywillcall.fragments.Notification.NotificationResponse
import com.vovance.securemywillcall.fragments.Profile.ProfileFragment.ProfileResponse
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
