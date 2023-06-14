package com.app.smwc.Activity.CompanyInfo

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

class CompanyInfoRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val addCompanyResponse = MutableLiveData<NetworkResult<CompanyInfoResponse>>()
    val companyResponseLiveData: LiveData<NetworkResult<CompanyInfoResponse>>
        get() = addCompanyResponse

    suspend fun addCompany(userRequest: CompanyData, token: String) {
        addCompanyResponse.postValue(NetworkResult.Loading())
        HELPER.print("PASSING_DATA", Gson().toJson(userRequest))
        val response = apiServices.addCompany(token,userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<CompanyInfoResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                addCompanyResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    addCompanyResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    addCompanyResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                addCompanyResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}