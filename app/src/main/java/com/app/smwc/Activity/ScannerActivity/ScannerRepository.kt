package com.app.smwc.Activity.ScannerActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.smwc.APIHandle.ApiServices
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Common.HELPER
import com.app.ssn.Utils.NetworkResult
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class ScannerRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val scannerResponse = MutableLiveData<NetworkResult<ScannerResponse>>()
    val scannerResponseLiveData: LiveData<NetworkResult<ScannerResponse>>
        get() = scannerResponse

    suspend fun createToken(userRequest: ScannerResponseData, token: String) {
        try {
            scannerResponse.postValue(NetworkResult.Loading())
            HELPER.print("PASSING_DATA", Gson().toJson(userRequest))
            val response = apiServices.CREATE_TOKEN(token, userRequest)
            handleScannerResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleScannerResponse(response: Response<ScannerResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                scannerResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    scannerResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    scannerResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                scannerResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}