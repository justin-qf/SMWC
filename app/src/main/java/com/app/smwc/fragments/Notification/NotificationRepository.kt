package com.app.smwc.fragments.Notification

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

class NotificationRepository @Inject constructor(@Named(Apis.BASE) private val apiServices: ApiServices) {

    private val notificationResponse = MutableLiveData<NetworkResult<NotificationResponse>>()
    val notificationResponseLiveData: LiveData<NetworkResult<NotificationResponse>>
        get() = notificationResponse

    suspend fun notification(token: String) {
        try {
            notificationResponse.postValue(NetworkResult.Loading())
            HELPER.print("Token", token)
            val response = apiServices.notification(token)
            handleNotificationResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        }
    }

    private fun handleNotificationResponse(response: Response<NotificationResponse>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                notificationResponse.postValue(NetworkResult.Success(response.body()))
            } else if (response.errorBody() != null) {
                HELPER.print("errorCode", Gson().toJson(response.code()))
                HELPER.print("errorCode", Gson().toJson(response.message()))
                HELPER.print("errorCode", Gson().toJson(response.errorBody()))
                HELPER.print("errorCode", Gson().toJson(response.body()))
                val error = JSONObject(response.errorBody()!!.charStream().readText())
                if (error.has("error") && error.getString("error").isNotEmpty()) {
                    notificationResponse.postValue(NetworkResult.Error(error.getString("error")))
                } else {
                    notificationResponse.postValue(NetworkResult.Error("Something went wrong."))
                }
            } else {
                notificationResponse.postValue(NetworkResult.Error("Something went wrong."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}