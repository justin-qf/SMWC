package com.app.smwc.Activity.ScannerActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smwc.Activity.LoginActivity.LoginData
import com.app.smwc.Activity.LoginActivity.LoginResponse
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
) : ViewModel() {

    val scannerResponseLiveData: LiveData<NetworkResult<ScannerResponse>>
        get() = scannerRepository.scannerResponseLiveData

    fun createToken(loginRequest: ScannerResponseData, token: String) {
        viewModelScope.launch { scannerRepository.createToken(loginRequest, token) }
    }
}
