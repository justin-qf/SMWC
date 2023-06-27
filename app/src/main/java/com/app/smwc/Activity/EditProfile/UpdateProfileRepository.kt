package com.app.smwc.Activity.EditProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.smwc.APIHandle.ApiServices
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Common.HELPER
import com.app.ssn.Utils.NetworkResult
import com.google.gson.Gson
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class UpdateProfileRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val updateProfileResponse = MutableLiveData<NetworkResult<EditProfileResponse>>()
    val updateProfileResponseLiveData: LiveData<NetworkResult<EditProfileResponse>>
        get() = updateProfileResponse

    suspend fun updateProfile(
        userRequest: UpdateProfileData,
        token: String,
        image: MultipartBody.Part
    ) {
        try {
            updateProfileResponse.postValue(NetworkResult.Loading())
            HELPER.print("PASSING_DATA", Gson().toJson(userRequest))
            val response = apiServices.updateProfile(token, image, userRequest)
            handleUpdateProfileResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleUpdateProfileResponse(response: Response<EditProfileResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                updateProfileResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    updateProfileResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    updateProfileResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                updateProfileResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}