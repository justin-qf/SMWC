package com.vovance.securemywillcall.fragments.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vovance.securemywillcall.apiHandle.ApiServices
import com.vovance.securemywillcall.apiHandle.Apis
import com.vovance.securemywillcall.common.HELPER
import com.vovance.ssn.Utils.NetworkResult
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class HomeRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val homeResponse = MutableLiveData<NetworkResult<HomeResponse>>()
    val homeResponseLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = homeResponse

    suspend fun home(token: String) {
        try {
            homeResponse.postValue(NetworkResult.Loading())
            val response = apiServices.home(token)
            handleHomeResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
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
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }
}