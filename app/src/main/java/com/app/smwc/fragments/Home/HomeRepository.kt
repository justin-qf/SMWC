package com.app.smwc.fragments.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Common.HELPER
import com.app.ssn.Utils.NetworkResult
import com.app.ssn.data.api.ApiServices
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class HomeRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val homeResponse = MutableLiveData<NetworkResult<HomeResponse>>()
    val homeResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = homeResponse

    suspend fun home(token: String) {
        homeResponse.postValue(NetworkResult.Loading())
        val response = apiServices.home(token)
        handleHomeResponse(response)
    }

    private fun handleHomeResponse(response: Response<HomeResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                homeResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    homeResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    homeResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                homeResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}