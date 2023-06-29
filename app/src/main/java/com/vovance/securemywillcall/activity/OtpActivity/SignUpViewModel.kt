package com.vovance.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.omcsalesapp.Activity.SignInActivity.SendOtpResponse
import com.vovance.securemywillcall.activity.LoginActivity.VerifyOtpResponse
import com.vovance.securemywillcall.activity.OtpActivity.OtpData
import com.vovance.securemywillcall.activity.OtpActivity.SignUpRepository
import com.vovance.ssn.Utils.NetworkResult
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
