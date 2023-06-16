package com.app.smwc.fragments.Profile.ProfileFragment

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

class ProfileRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val profileResponse = MutableLiveData<NetworkResult<ProfileResponse>>()
    val profileResponseLiveData: LiveData<NetworkResult<ProfileResponse>>
        get() = profileResponse

    suspend fun userProfile(token: String) {
        profileResponse.postValue(NetworkResult.Loading())
        HELPER.print("Token", token)
        val response = apiServices.profile(token)
        handleProfileResponse(response)
    }

    private fun handleProfileResponse(response: Response<ProfileResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                profileResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    profileResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    profileResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                profileResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}