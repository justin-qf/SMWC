package com.app.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.app.smwc.Activity.LoginActivity.VerifyOtpResponse
import com.app.smwc.Activity.OtpActivity.OtpData
import com.app.smwc.Activity.OtpActivity.SignUpRepository
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
) : ViewModel() {

    val setOtpLiveData: LiveData<NetworkResult<SendOtpResponse>>
        get() = signUpRepository.otpResponseLiveData

    val verifyOtpLiveData: LiveData<NetworkResult<VerifyOtpResponse>>
        get() = signUpRepository.verifyResponseLiveData

    fun getOtp(getOtpRequest: OtpData) {
        viewModelScope.launch { signUpRepository.senOtp(getOtpRequest) }
    }

    fun verifyOtp(verifyOtpRequest: OtpData) {
        viewModelScope.launch { signUpRepository.verifyOtp(verifyOtpRequest) }
    }
}
