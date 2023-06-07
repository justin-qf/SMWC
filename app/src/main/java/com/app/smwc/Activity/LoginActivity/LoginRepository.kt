package com.app.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.ssn.Utils.NetworkResult
import com.app.ssn.data.api.ApiServices
import javax.inject.Inject
import javax.inject.Named

class LoginRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val _checkResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _checkResponse

//    suspend fun loginUser(userRequest: LoginData) {
//        _checkResponse.postValue(NetworkResult.Loading())
//        HELPER.print("PASSING_DATA", Gson().toJson(userRequest))
//        val response = apiServices.login(userRequest)
//        handleResponse(response)
//    }

//    private fun handleResponse(response: Response<LoginResponse>) {
//        try {
//            if (response.isSuccessful && response.body() != null) {
//                _checkResponse.postValue(NetworkResult.Success(response.body()))
//            } else if (response.errorBody() != null) {
//                HELPER.print("errorCode", Gson().toJson(response.code()))
//                HELPER.print("errorCode", Gson().toJson(response.message()))
//                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
//                HELPER.print("errorCode", Gson().toJson(response.body()))
//                val error = JSONObject(response.errorBody()!!.charStream().readText())
//                if (error.has("error") && error.getString("error").isNotEmpty()) {
//                    _checkResponse.postValue(NetworkResult.Error(error.getString("error")))
//                } else {
//                    _checkResponse.postValue(NetworkResult.Error("Something went wrong."))
//                }
//            } else {
//                _checkResponse.postValue(NetworkResult.Error("Something went wrong."))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}