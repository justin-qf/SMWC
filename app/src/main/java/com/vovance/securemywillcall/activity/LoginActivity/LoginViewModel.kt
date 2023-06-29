package com.vovance.ssn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovance.securemywillcall.activity.LoginActivity.LoginData
import com.vovance.securemywillcall.activity.LoginActivity.LoginResponse
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = loginRepository.loginResponseLiveData

    fun login(loginRequest: LoginData) {
        viewModelScope.launch { loginRepository.loginUser(loginRequest) }
    }

}
