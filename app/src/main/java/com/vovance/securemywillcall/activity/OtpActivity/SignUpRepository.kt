package com.vovance.securemywillcall.activity.OtpActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vovance.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.vovance.securemywillcall.activity.LoginActivity.VerifyOtpResponse
import com.vovance.securemywillcall.apiHandle.ApiServices
import com.vovance.securemywillcall.apiHandle.Apis
import com.vovance.securemywillcall.common.HELPER
import com.vovance.ssn.Utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class SignUpRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {
    private val getOtpResponse = MutableLiveData<NetworkResult<SendOtpResponse>>()
    val otpResponseLiveData: LiveData<NetworkResult<SendOtpResponse>>
        get() = getOtpResponse

    private val verifyOtpResponse = MutableLiveData<NetworkResult<VerifyOtpResponse>>()
    val verifyResponseLiveData: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = verifyOtpResponse

    suspend fun senOtp(userRequestDate: OtpData) {
        try {
            getOtpResponse.postValue(NetworkResult.Loading())
            HELPER.print("PASSING_DATA", Gson().toJson(userRequestDate))
            val response = apiServices.setOtp(userRequestDate)
            handleSendOtpResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleSendOtpResponse(response: Response<SendOtpResponse>) {
        HELPER.print("GET_OTP_URL", response.raw().request.url.toString())
        try {
            if (response.isSuccessful && response.body() != null) {
                getOtpResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    getOtpResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    getOtpResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                getOtpResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun verifyOtp(userRequestDate: OtpData) {
        try {
            verifyOtpResponse.postValue(NetworkResult.Loading())
            HELPER.print("PASSING_DATA", Gson().toJson(userRequestDate))
            val response = apiServices.verifyOtp(userRequestDate)
            handleVerifyOtpResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleVerifyOtpResponse(response: Response<VerifyOtpResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                verifyOtpResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    verifyOtpResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    verifyOtpResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                verifyOtpResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}