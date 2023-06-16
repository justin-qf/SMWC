package com.app.smwc.fragments.Notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    val notificationResponseLiveData: LiveData<NetworkResult<NotificationResponse>>
        get() = notificationRepository.notificationResponseLiveData

    fun notification(token: String) {
        viewModelScope.launch { notificationRepository.notification(token) }
    }
}
