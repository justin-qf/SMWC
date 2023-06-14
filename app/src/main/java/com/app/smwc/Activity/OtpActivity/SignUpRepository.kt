package com.app.smwc.Activity.OtpActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Activity.LoginActivity.VerifyOtpResponse
import com.app.smwc.Common.HELPER
import com.app.ssn.Utils.NetworkResult
import com.app.ssn.data.api.ApiServices
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
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
        getOtpResponse.postValue(NetworkResult.Loading())
        HELPER.print("PASSING_DATA", Gson().toJson(userRequestDate))
        val response = apiServices.setOtp(userRequestDate)
        handleSendOtpResponse(response)
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
        verifyOtpResponse.postValue(NetworkResult.Loading())
        HELPER.print("PASSING_DATA", Gson().toJson(userRequestDate))
        val response = apiServices.verifyOtp(userRequestDate)
        handleVerifyOtpResponse(response)
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