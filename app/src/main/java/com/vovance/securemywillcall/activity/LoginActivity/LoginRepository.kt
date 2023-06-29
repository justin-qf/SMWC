package com.vovance.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vovance.securemywillcall.apiHandle.ApiServices
import com.vovance.securemywillcall.apiHandle.Apis
import com.vovance.securemywillcall.activity.LoginActivity.LoginData
import com.vovance.securemywillcall.activity.LoginActivity.LoginResponse
import com.vovance.securemywillcall.common.HELPER
import com.vovance.ssn.Utils.NetworkResult
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class LoginRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val loginResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = loginResponse

    suspend fun loginUser(userRequest: LoginData) {
        try {
            loginResponse.postValue(NetworkResult.Loading())
            HELPER.print("PASSING_DATA", Gson().toJson(userRequest))
            val response = apiServices.login(userRequest)
            handleResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleResponse(response: Response<LoginResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                loginResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    loginResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    loginResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                loginResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}